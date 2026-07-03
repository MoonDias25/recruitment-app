import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { JobCreationResponse, JobOfferRequest } from '../models/job-offer';

@Injectable({
  providedIn: 'root',
})
export class CreateJob {

  private apiUrl = 'http://localhost/api/jobs/create'; 

  constructor(private http: HttpClient) { }

  createJobOffer(jobData: JobOfferRequest): Observable<JobCreationResponse>{
    return this.http.post<JobCreationResponse>(this.apiUrl, jobData);
  }
}
