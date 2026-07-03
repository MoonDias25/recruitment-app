import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { Response } from '../models/response';
import { PromotionRequestDTO } from '../models/promotion-request';

@Injectable({
  providedIn: 'root',
})
export class Promotion {

  private http = inject(HttpClient);
  private apiUrl = '/api/promotion';

  public pendingRequestsCount = signal<number>(0);

    submitPromotionRequest(targetUserId: string, hrNotes: string): Observable<Response> {
    return this.http.post<Response>(`${this.apiUrl}/submit`, {
      targetUserId,
      hrNotes
    });
  }

  loadPendingRequestsCount(): void {
    this.http.get<{ count: number }>(`${this.apiUrl}/pending-count`).subscribe({
      next: (res) => this.pendingRequestsCount.set(res.count),
      error: (err) => console.error('Could not load notification count', err)
    });
  }

  getPendingRequests(): Observable<PromotionRequestDTO[]> {
    return this.http.get<PromotionRequestDTO[]>(`${this.apiUrl}/pending-list`);
  }
}
