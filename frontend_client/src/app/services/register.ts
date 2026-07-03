import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../models/register-request';
import { Response } from '../models/response';

@Injectable({
  providedIn: 'root',
})
export class Register {

  private authServerUrl = '/auth/register';

  constructor(private http: HttpClient){}

  register(request: RegisterRequest): Observable<Response>{
    return this.http.post(this.authServerUrl, request);
  }
}
