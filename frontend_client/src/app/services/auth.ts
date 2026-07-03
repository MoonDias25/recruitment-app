import { Injectable, computed, inject, signal } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../app.config';
import { CustomUserClaims } from '../models/user-claims';
import { filter } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {

  private oauthService = inject(OAuthService);

  public isLoggedIn = signal<boolean>(false);

  public isAdmin = signal<boolean>(false);
  public isHR = signal<boolean>(false);
  public isUser = signal<boolean>(false);
  public isSuperAdmin = signal<boolean>(false);

  constructor() {
    this.oauthService.configure(authCodeFlowConfig);
    this.oauthService.events
      .pipe(
        filter(e => e.type === 'token_received' || e.type === 'discovery_document_loaded')
      )
      .subscribe(() => {
        this.updateRoleSignals(); 
    });
  }

  private updateRoleSignals(): void {
    const hasToken = this.oauthService.hasValidAccessToken();
    this.isLoggedIn.set(hasToken);

    if (hasToken) {
      const role = this.getRole();
      console.log('CURRENT ROLE:', role);
      this.isAdmin.set(role === 'ROLE_ADMIN');
      this.isHR.set(role === 'ROLE_HR');
      this.isUser.set(role === 'ROLE_USER');
      this.isSuperAdmin.set(role === 'ROLE_SUPER_ADMIN');
    } else {
      this.isAdmin.set(false);
      this.isHR.set(false);
      this.isUser.set(false);
      this.isSuperAdmin.set(false);
    }
  }

  login() {
    this.oauthService.initLoginFlow();
  }

  logout() {
    this.oauthService.logOut();
    sessionStorage.clear();
    this.updateRoleSignals;
  }

  getRole(): string{
    const claims = this.oauthService.getIdentityClaims() as CustomUserClaims;

    if(!claims){
      return '';
    }

    return claims.role;
  }

  getUserInfo(): CustomUserClaims | null{
    return this.oauthService.getIdentityClaims() as CustomUserClaims;
  }
}
