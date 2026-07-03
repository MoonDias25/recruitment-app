import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Register } from '../../services/register';
import { RegisterRequest } from '../../models/register-request';
import { Response } from '../../models/response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class RegisterComponent {
  private registerService = inject(Register);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);
  isLoading = signal<boolean>(false);

  registerForm: FormGroup = this.fb.group({
    firstName: ['', [Validators.required, Validators.minLength(2)]],
    lastName: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]], 
    birthDate: ['', [Validators.required]]
  });

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.errorMessage.set('Please fill all the fields.');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const requestData: RegisterRequest = this.registerForm.value;

    this.registerService.register(requestData).subscribe({
      next:(response: Response) => {
        this.isLoading.set(false);
        this.registerForm.reset();
        this.router.navigate(['/home'], {
          state: { 
            infoMessage: 'Registration was successful, please login with your data!' 
          }
        });
      },
      error: (err) => {
        this.isLoading.set(false);
        
        if (err.error && typeof err.error === 'object' && err.error.message) {
          this.errorMessage.set(err.error.message);
        } else {
          this.errorMessage.set(`Error (${err.status}): ${err.message || 'Server error'}`);
        }
      }
    });
  }
}
