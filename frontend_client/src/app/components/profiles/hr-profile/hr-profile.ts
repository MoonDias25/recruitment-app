import { Component, inject, Input, OnInit, signal } from '@angular/core';
import { CustomUserClaims } from '../../../models/user-claims';
import { NgClass, DatePipe, SlicePipe} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Job } from '../../../services/job';
import { JobOffer } from '../../../models/job-offer';
import { JobApplication } from '../../../services/job-application';
import { CandidateApplication, UpdateJobApplicationRequest } from '../../../models/job-application';

declare var bootstrap: any;

@Component({
  selector: 'app-hr-profile',
  imports: [DatePipe, FormsModule, SlicePipe],
  templateUrl: './hr-profile.html',
  styleUrl: './hr-profile.scss',
})
export class HrProfile implements OnInit{

  @Input() info!: CustomUserClaims; 
  
  private jobService = inject(Job);
  private jobAppsService = inject(JobApplication)
  public myOffers = signal<JobOffer[]>([]);
  public isLoading = signal<boolean>(true);

  public selectedJob = signal<JobOffer | null>(null);
  public selectedJobApps = signal<CandidateApplication[]>([]);
  public editingApp = signal<CandidateApplication | null>(null);

  public errorMessage = signal<string | null>(null);
  public activeJobIdApps = signal<string | null>(null);
  public jobStatusOptions = ['ACTIVE', 'INACTIVE', 'PENDING'];
  public appStatusOptions = ['PENDING', 'ACCEPTED', 'REJECTED', 'REVIEWED'];

  ngOnInit(): void {
    this.loadMyOffers();
  }

  loadMyOffers(): void {
    this.isLoading.set(true);

    this.jobService.getMyJobOffers().subscribe({
      next: (jobs) => {
        this.myOffers.set(jobs);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error at job loading: ', err);
        this.isLoading.set(false);
      }
    });
  }

  openEditModal(job: JobOffer): void {
    this.errorMessage.set(null); 
    
    const clonedJob = { ...job };

    if (clonedJob.applicationEndDate) {
      const fullDate = new Date(clonedJob.applicationEndDate);
      if (!isNaN(fullDate.getTime())) {
        const year = fullDate.getFullYear();
        const month = String(fullDate.getMonth() + 1).padStart(2, '0');
        const day = String(fullDate.getDate()).padStart(2, '0');
        
        clonedJob.applicationEndDate = `${year}-${month}-${day}` as any;
      }
    }

    this.selectedJob.set(clonedJob);
  }

  saveChanges(): void {
    this.errorMessage.set(null);
    const jobToUpdate = this.selectedJob();
    if (!jobToUpdate) return;

    if (jobToUpdate.jobTitle && jobToUpdate.jobTitle.length > 50) {
      this.errorMessage.set('Job title cannot exceed 50 characters.');
      return;
    }

    if (jobToUpdate.description && jobToUpdate.description.length > 2000) {
      this.errorMessage.set('Description cannot exceed 2000 characters.');
      return;
    }

    if (jobToUpdate.minSalary > jobToUpdate.maxSalary) {
      this.errorMessage.set('Minimum salary cannot be greater than maximum salary!');
      return;
    }

    if (!jobToUpdate.applicationEndDate) {
      this.errorMessage.set('Please select an application end date.');
      return;
    }

    const inputDateStr = jobToUpdate.applicationEndDate as unknown as string;
  const [year, month, day] = inputDateStr.split('-').map(Number);
  
  const selectedDate = new Date(year, month - 1, day, 0, 0);
  const today = new Date();
  today.setHours(0, 0);

  if (selectedDate < today) {
    this.errorMessage.set("End date can't be in the past.");
    return;
  }

  const now = new Date();
  const finalDateTime = new Date(
    year,
    month - 1, 
    day,
    now.getHours(),
    now.getMinutes()
  );

  jobToUpdate.applicationEndDate = finalDateTime.toISOString() as any;

  this.jobService.updateJobOffer(jobToUpdate.id, jobToUpdate).subscribe({
    next: (updatedJob) => {
        this.myOffers.update(jobs => 
          jobs.map(j => j.id === updatedJob.id ? updatedJob : j)
        );
        this.selectedJob.set(null); 
      },
      error: (err) => {
        console.error('Error while saving:', err);
        jobToUpdate.applicationEndDate = inputDateStr as any;
        this.errorMessage.set(err.error?.message || 'An error occurred while saving.');
      }
    });
}


deleteJob(job: JobOffer): void {
  const confirmDelete = confirm(`Are you sure you want to delete "${job.jobTitle}"?`);
  
  if (confirmDelete) {
    this.jobService.deleteJobOffer(job.id).subscribe({
      next: () => {
        this.myOffers.update(jobs => jobs.filter(j => j.id !== job.id));
      },
      error: (err) => {
        console.error('Error while deleting job:', err);
        alert('Could not delete the job offer. Please try again.');
      }
    });
  }
}

showJobApplications(jobOfferId: string): void {
  if (this.activeJobIdApps() === jobOfferId) {
    this.activeJobIdApps.set(null);
    this.selectedJobApps.set([]);
    return;
  }

  this.jobAppsService.getApplicationsByJob(jobOfferId).subscribe({
    next: (apps) => {
      this.selectedJobApps.set(apps);
      this.activeJobIdApps.set(jobOfferId); 
    },
    error: (err) => {
      console.error('Error at applications loading: ', err);
    }
  });
}

downloadCandidateCv(applicationId: string, fileName: string): void {
    this.jobAppsService.downloadCv(applicationId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName; 
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Could not download CV:', err);
        alert('Failed to download CV. The file might be missing from the server.');
      }
    });
  }
  

startEditingApp(app: CandidateApplication) {
  this.editingApp.set({ ...app });
}

cancelEditingApp() {
  this.editingApp.set(null);
}

saveAppChanges() {
  const updated = this.editingApp();
  if (!updated || !updated.id) return;

  const requestBody: UpdateJobApplicationRequest = {
    status: updated.status,
    recruiterNotes: updated.recruiterNotes || ''
  };

  this.jobAppsService.updateCandidateApplication(updated.id, requestBody).subscribe({
    next: (savedAppFromBackend) => {
      this.selectedJobApps.update(currentApplications => 
        currentApplications.map(app => app.id === savedAppFromBackend.id ? savedAppFromBackend : app)
      );
      this.editingApp.set(null);
    },
    error: (err) => {
      console.error(err);
      alert('Error at saving. Try again.');
    }
  });
}
}
