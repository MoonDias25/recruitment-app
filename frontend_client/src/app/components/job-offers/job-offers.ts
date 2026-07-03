import { CommonModule } from '@angular/common';
import { Job } from '../../services/job';
import { AllJobOffers, SpringPageResponse } from '../../models/job-offer';
import { Auth } from '../../services/auth';
import { ReactiveFormsModule, FormControl } from '@angular/forms'; // Am adăugat FormControl
import { Router } from '@angular/router';
import { debounceTime, distinctUntilChanged, Subscription } from 'rxjs';
import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';

@Component({
  selector: 'app-job-offers',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './job-offers.html',
  styleUrl: './job-offers.scss',
})
export class JobOffers implements OnInit, OnDestroy {
  private authService = inject(Auth);
  private router = inject(Router);
  private jobService = inject(Job);

  jobOffers = signal<AllJobOffers[]>([]);
  totalPages = signal<number>(0);
  currentPage = signal<number>(0);
  expandedJobId = signal<string | null>(null);

  // CONTROLUL pentru inputul de căutare
  searchControl = new FormControl('');
  private searchSubscription!: Subscription;

  ngOnInit(): void {
    this.loadJobs('', 0);

    this.searchSubscription = this.searchControl.valueChanges.pipe(
      debounceTime(350),           
      distinctUntilChanged()       
    ).subscribe(value => {
      const titleFilter = value ? value.trim() : '';
      this.loadJobs(titleFilter, 0); 
    });
  }

  loadJobs(title: string, page: number): void {
    this.jobService.getJobOffers(title, page).subscribe({
      next: (data: SpringPageResponse) => {
        console.log(`Jobs loaded for title: "${title}", page: ${page}`);
        this.jobOffers.set(data.content); 
        this.totalPages.set(data.page.totalPages);
        this.currentPage.set(data.page.number);
        this.expandedJobId.set(null);
      },
      error: (err) => {
        console.error('Something went wrong at data loading!', err);
      }
    });
  }

  getCurrentSearchValue(): string {
    return this.searchControl.value ? this.searchControl.value.trim() : '';
  }

  goToNextPage(): void {
    if (this.currentPage() < this.totalPages() - 1) {
      this.loadJobs(this.getCurrentSearchValue(), this.currentPage() + 1);
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage() > 0) {
      this.loadJobs(this.getCurrentSearchValue(), this.currentPage() - 1);
    }
  }

  toggleDescription(jobId: string) {
    this.expandedJobId.update(currentId => currentId === jobId ? null : jobId);
  }

  handleApply(jobId: string) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/apply-job', jobId]);
    } else {
      console.log('You have to be logged in to apply!');
      this.authService.login();
    }
  }

  ngOnDestroy(): void {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }
}