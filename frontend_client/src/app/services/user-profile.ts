import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../models/user-profile';

@Injectable({
  providedIn: 'root',
})
export class UserProfile {

  private http = inject(HttpClient);
  private apiUrl = '/api/profile';

  searchCandidate(email: string): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(`${this.apiUrl}/search-candidate`, {
      params: { email }
    });
  }
}
