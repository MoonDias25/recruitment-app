import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationResponse, CandidateApplication, UpdateJobApplicationRequest } from '../models/job-application';

@Injectable({
  providedIn: 'root',
})
export class JobApplication {
  private http = inject(HttpClient);

  private apiUrl = '/api/applications';

  applyToJob(jobOfferId: string, file: File): Observable<ApplicationResponse> {
    
    const formData = new FormData();
    
    formData.append('jobOfferId', jobOfferId);
    formData.append('file', file);

    return this.http.post<ApplicationResponse>(`${this.apiUrl}/apply`, formData);
  }

  getMyApplications(): Observable<CandidateApplication[]> {
    return this.http.get<CandidateApplication[]>(`${this.apiUrl}/my-applications`);
  }

  downloadCv(applicationId: string): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/download-cv/${applicationId}`, {
    responseType: 'blob'
    });
  }

  getApplicationsByJob(jobOfferId: string): Observable<CandidateApplication[]>{
    return this.http.get<CandidateApplication[]>(`${this.apiUrl}/job-offers/${jobOfferId}/applications`);
  }

  updateCandidateApplication(appId: string, request: UpdateJobApplicationRequest): Observable<CandidateApplication>{
    return this.http.put<CandidateApplication>(`${this.apiUrl}/${appId}/review`, request);
  }

  deleteCandidateApplication(appId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${appId}`);
  }
}
