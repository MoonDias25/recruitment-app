import { CommonModule } from '@angular/common';
import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateJob } from '../../services/create-job';
import { JobOfferRequest } from '../../models/job-offer';
import { Router } from '@angular/router';


@Component({
  selector: 'app-add-job-offer',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-job-offer.html',
  styleUrl: './add-job-offer.scss',
})
export class AddJobOffer {
  jobForm: FormGroup;
  errorMessage: string = '';

  constructor(
  private fb: FormBuilder,
  private jobService: CreateJob,
  private router: Router,
  private cdr: ChangeDetectorRef
  ){
    this.jobForm = this.fb.group({
      jobTitle: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      minSalary: [null, [Validators.required, Validators.min(2000)]], // 2000 RON minim, exact ca pe backend
      maxSalary: [null, [Validators.required]],
      applicationEndDate: ['', [Validators.required]]
    }, { validators: this.salaryValidator });
  }

  salaryValidator(group: FormGroup): any{
    const min = group.get('minSalary')?.value;
    const max = group.get('maxSalary')?.value;

    if (min !== null && max !== null && max < min) {
      return { salaryInvalid: true };
    }
    return null;
  }

  onSubmit(): void{
    if(this.jobForm.valid){
      const newJob: JobOfferRequest = this.jobForm.value;

      this.jobService.createJobOffer(newJob).subscribe({
        next: (response) =>{
          this.errorMessage = '';
          this.jobForm.reset();
          this.router.navigate(['/job-offers']);
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'An error occurred while creating the job offer!';
          this.cdr.detectChanges();
        }
      });
    }else{
      this.jobForm.markAllAsTouched();
      this.cdr.detectChanges();
    }
  }
}


