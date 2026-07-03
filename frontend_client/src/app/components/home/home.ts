import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../../app.config';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  
  private oauthService = inject(OAuthService);
  private router = inject(Router);
  private authService = inject(Auth);

  registrationSuccessMessage = signal<string | null>(null);

  ngOnInit(): void {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state as { infoMessage?: string } || window.history.state;
    
    if (state && state.infoMessage) {
      this.registrationSuccessMessage.set(state.infoMessage);
      window.history.replaceState({}, document.title);
    }
  }

  redirectToLogin(): void {
    this.authService.login();
  }
}
