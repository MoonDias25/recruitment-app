import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { Job } from '../../../services/job';
import { JobOfferAdminDTO } from '../../../models/job-offer';
import { FormsModule, NgModel } from '@angular/forms';
import { JobApplication } from '../../../services/job-application';
import { CandidateApplication } from '../../../models/job-application';

@Component({
  selector: 'app-admin-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.scss',
})
export class AdminProfile implements OnInit{

  private jobOfferService = inject(Job);
  private jobApplicationService = inject(JobApplication);

  public jobOffers = signal<JobOfferAdminDTO[]>([]);
  public isLoading = signal<boolean>(true);

  public expandedJobId = signal<string | null>(null);

  public editingJobId = signal<string | null>(null);

  public applicationsMap = signal<{ [key: string]: CandidateApplication[] }>({});

  ngOnInit(): void {
    this.loadAllJobOffers();
  }

  private loadAllJobOffers(): void {
    this.isLoading.set(true);
    
    this.jobOfferService.getAllJobOffersForAdmin().subscribe({
      next: (offers: JobOfferAdminDTO[]) => {
        this.jobOffers.set(offers);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error at loading job offers: ', err);
        this.isLoading.set(false);
      }
    });
  }

  public toggleExpand(jobId: string): void {
    if (this.expandedJobId() === jobId) {
      this.expandedJobId.set(null);
    } else {
      this.expandedJobId.set(jobId);

      const alreadyLoaded = this.applicationsMap()[jobId];

      if (!alreadyLoaded) {
        this.jobApplicationService.getApplicationsByJob(jobId).subscribe({
          next: (apps: CandidateApplication[]) => {
            this.applicationsMap.update(currentMap => ({
              ...currentMap,
              [jobId]: apps
            }));
          },
          error: (err) => {
            console.error('Error loading applications for job:', jobId, err);
          }
        });
      }
    }
  }

  public deleteJobOffer(jobId: string): void {
    if (confirm('Are you sure you want to delete this job offer?')) {
      this.jobOfferService.deleteAdminJobOffer(jobId).subscribe({
        next: () => {
          this.jobOffers.update(offers => offers.filter(job => job.id !== jobId));
          
          if (this.expandedJobId() === jobId) {
            this.expandedJobId.set(null);
          }
        },
        error: (err) => {
          console.error('Error deleting job offer:', err);
          alert('Failed to delete the job offer. You might not have the required permissions.');
        }
      });
    }
  }

  public startEdit(jobId: string): void {
    this.editingJobId.set(jobId);
  }

  public cancelEdit(): void {
    this.editingJobId.set(null);
    this.loadAllJobOffers(); 
  }

  public saveJobOffer(job: JobOfferAdminDTO): void {
    this.jobOfferService.updateAdminJobOffer(job.id, job).subscribe({
      next: (updatedJob: JobOfferAdminDTO) => {
        this.jobOffers.update(offers => offers.map(o => o.id === updatedJob.id ? updatedJob : o));
        
        this.editingJobId.set(null);
      },
      error: (err) => {
        console.error('Error updating job offer:', err);
        alert(err.error?.message || 'Failed to update job offer. Please check the inputs.');
      }
    });
  }

  public deleteCandidateApplication(jobId: string, appId: string): void {
    if (confirm('Are you sure you want to permanently delete this candidate application and their CV?')) {
      this.jobApplicationService.deleteCandidateApplication(appId).subscribe({
        next: () => {
          this.applicationsMap.update(currentMap => {
            const updatedApps = currentMap[jobId]?.filter(app => app.id !== appId) || [];
            return {
              ...currentMap,
              [jobId]: updatedApps
            };
          });
        },
        error: (err) => {
          console.error('Error deleting application:', err);
          alert('Failed to delete the application. Please try again.');
        }
      });
    }
  }

  public downloadCv(appId: string, fileName: string): void {
    this.jobApplicationService.downloadCv(appId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName; 
        
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error downloading CV:', err);
        alert('Could not download the CV. Please try again.');
      }
    });
  }
}
