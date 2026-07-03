import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AllJobOffers, JobOffer, JobOfferAdminDTO, SpringPageResponse } from '../models/job-offer';


@Injectable({
  providedIn: 'root',
})
export class Job {

  private apiUrl = '/api/jobs';

  constructor(private http: HttpClient){}

  getJobOffers(title: string = '', page: number = 0, size: number = 6, sortOption: string = 'applicationEndDate-desc'): Observable<SpringPageResponse> {
    let params = new HttpParams()
      .set('title', title)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortOption', sortOption);

    return this.http.get<SpringPageResponse>(`${this.apiUrl}/all-jobs`, { params });
  }

  getJobById(id: string): Observable<JobOffer>{
    return this.http.get<any>(`${this.apiUrl}/job-offer/${id}`);
  }

  getMyJobOffers(): Observable<JobOffer[]>{
    return this.http.get<JobOffer[]>(`${this.apiUrl}/my-job-offers`);
  }

  updateJobOffer(id: string, jobData: Partial<JobOffer>): Observable<JobOffer> {
    return this.http.put<JobOffer>(`${this.apiUrl}/${id}`, jobData);
  }

  deleteJobOffer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  getAllJobOffersForAdmin(): Observable<JobOfferAdminDTO[]> {
    return this.http.get<JobOfferAdminDTO[]>(`${this.apiUrl}/admin/job-offers`);
  }

  updateAdminJobOffer(id: string, dto: JobOfferAdminDTO): Observable<JobOfferAdminDTO> {
    return this.http.put<JobOfferAdminDTO>(`${this.apiUrl}/admin/job-offers/update/${id}`, dto);
  }

  deleteAdminJobOffer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

}
