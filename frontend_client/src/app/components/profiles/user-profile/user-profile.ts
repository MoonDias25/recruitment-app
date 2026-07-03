import { CommonModule } from '@angular/common';
import { Component, Input, OnInit, inject, signal } from '@angular/core';
import { CustomUserClaims } from '../../../models/user-claims';
import { JobApplication } from '../../../services/job-application';
import { CandidateApplication } from '../../../models/job-application';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss',
})
export class UserProfile implements OnInit{
  @Input() info: CustomUserClaims | null = null;

  private applicationService = inject(JobApplication);

  applications = signal<CandidateApplication[]>([]);
  isLoading = signal<boolean>(true);

  ngOnInit(): void {
    this.fetchApplications();
  }

  fetchApplications(): void{
    this.applicationService.getMyApplications().subscribe({
      next: (data) => {
        this.applications.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load applications:', err);
        this.isLoading.set(false);
      }
    })
  }
  downloadCandidateCv(cvId: string, fileName: string): void {
  this.applicationService.downloadCv(cvId).subscribe({
    next: (blobData: Blob) => {
      // 1. Creăm un URL temporar în browser pentru fișierul primit (PDF/Word)
      const blobUrl = window.URL.createObjectURL(blobData);
      
      // 2. Creăm un element de tip ancoră (<a>) invizibil în HTML
      const anchor = document.createElement('a');
      anchor.href = blobUrl;
      anchor.download = fileName; // Forțăm browserul să folosească numele original al fișierului
      
      // 3. Adăugăm ancora în pagină, simulăm click-ul de download și o ștergem
      document.body.appendChild(anchor);
      anchor.click();
      document.body.removeChild(anchor);
      
      // 4. Eliberăm memoria browserului ocupată de URL-ul temporar
      window.URL.revokeObjectURL(blobUrl);
    },
    error: (err) => {
      console.error('Could not download the CV file:', err);
      alert('Eroare la descărcarea CV-ului! Verificați dacă fișierul mai există pe server.');
    }
  });
}
}
