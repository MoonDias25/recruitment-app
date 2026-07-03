import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserProfile } from '../../services/user-profile';
import { Promotion } from '../../services/promotion';
import { UserProfileDTO } from '../../models/user-profile';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-promotion-request',
  imports: [FormsModule, RouterLink],
  templateUrl: './promotion-request.html',
  styleUrl: './promotion-request.scss',
})
export class PromotionRequest {

  private userProfileService = inject(UserProfile);
  private promotionService = inject(Promotion);

  public currentStep = signal<number>(1);
  public emailInput = signal<string>('');
  public hrNotesInput = signal<string>('');

  public candidateProfile = signal<UserProfileDTO | undefined>(undefined);

  public errorMessage = signal<string>('');
  public successMessage = signal<string>('');

  public onSearchEmail(): void {
    const email = this.emailInput().trim();
    if (!email) return;
    
    this.errorMessage.set('');
    
    this.userProfileService.searchCandidate(email).subscribe({
      next: (profile) => {
        this.candidateProfile.set(profile);
        this.currentStep.set(2); 
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorMessage.set("You can only request promotion for candidates who applied to your job offers.");
        } else if (err.status === 404) {
          this.errorMessage.set("No user found with this email address.");
        } else {
          this.errorMessage.set("Something went wrong. Please try again.");
        }
      }
    });
  }

  public onSubmitRequest(): void {
    const profile = this.candidateProfile();
    if (!profile) return;
    
    this.errorMessage.set('');

    this.promotionService.submitPromotionRequest(profile.id, this.hrNotesInput()).subscribe({
      next: (response) => {
        this.successMessage.set(response.message || "Request submitted successfully!");
        this.currentStep.set(3); 
      },
      error: (err) => {
        this.errorMessage.set(err.error?.message || "Could not submit the promotion request.");
      }
    });
  }

  public resetForm(): void {
    this.currentStep.set(1);
    this.emailInput.set('');
    this.hrNotesInput.set('');
    this.candidateProfile.set(undefined);
    this.errorMessage.set('');
    this.successMessage.set('');
  }
}
