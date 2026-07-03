import { Component, OnInit, inject, signal, input, effect } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplication } from '../../services/job-application';
import { ApplicationResponse } from '../../models/job-application';
import { FormsModule } from '@angular/forms';
import { Job } from '../../services/job';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-apply-job',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './apply-job.html',
  styleUrl: './apply-job.scss',
})
export class ApplyJob implements OnInit{
  private router = inject(Router);
  private applicationService = inject(JobApplication);
  private jobOfferService = inject(Job);
  private route = inject(ActivatedRoute);

  jobId = signal<string>(''); 
  jobTitle = signal<string>('Loading...');
  selectedFile = signal<File | null>(null);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  isSubmitting = signal<boolean>(false);

  ngOnInit(): void {
    const idFromUrl = this.route.snapshot.paramMap.get('id') || '';
    this.jobId.set(idFromUrl);

    if (this.jobId()) {
      this.jobOfferService.getJobById(this.jobId()).subscribe({
        next: (job) => {
          this.jobTitle.set(job.jobTitle);
          this.errorMessage.set('');
        },
        error: (err) => {
          this.errorMessage.set('Could not load job details from the server.');
          this.jobTitle.set('Unknown Position');
        }
      });
    } else {
      this.errorMessage.set('No job ID found in the URL.');
    }
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    
    if (inputElement.files && inputElement.files.length > 0) {
      this.selectedFile.set(inputElement.files[0]);
      this.errorMessage.set(''); 
    }
  }

  onSubmit(): void {
    const file = this.selectedFile();
    const currentJobId = this.jobId(); 

    if (!file) {
      this.errorMessage.set('Please upload your CV before submitting!');
      return;
    }

    if (!currentJobId) {
      this.errorMessage.set('Invalid Job ID. Cannot submit application.');
      return;
    }

    console.log('Sending the application for job id:', currentJobId);
    console.log('Selected file is:', file.name);

    this.isSubmitting.set(true);

    this.applicationService.applyToJob(currentJobId, file).subscribe({
      next: (response: ApplicationResponse) => {
        this.successMessage.set(response.message || 'Application submitted successfully!');
        this.errorMessage.set('');
        this.isSubmitting.set(false);

        setTimeout(() => {
          this.router.navigate(['/job-offers']);
        }, 3000);
      },
      error: (err) => {
        this.successMessage.set('');
        this.isSubmitting.set(false);
        
        if (err.error?.message) {
          this.errorMessage.set(err.error.message);
        } else {
          this.errorMessage.set('An unexpected error occurred. Please try again.');
        }
      }
    });
  }
}