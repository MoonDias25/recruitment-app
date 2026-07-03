import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router} from '@angular/router';
import { authCodeFlowConfig } from '../../app.config';
import { OAuthService } from 'angular-oauth2-oidc';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  template: `<div style="text-align: center; margin-top: 50px;"><h2>Logging...</h2></div>`,
  styleUrl: './login.scss',
})
export class Login implements OnInit{
  private oauthService = inject(OAuthService);
  private router = inject(Router);
  private authService = inject(Auth);

  ngOnInit() {
    // 1. Încărcăm configurația serverului
    this.oauthService.configure(authCodeFlowConfig);

    // 2. Îi spunem librăriei să caute codul secret în URL și să ceară token-urile JWT
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then((isLoggedIn) => {
      if (isLoggedIn) {
        console.log('Authentication success! Tokens were saved.');
        
        this.authService.isLoggedIn.set(true);
        // 3. Trimitem utilizatorul pe pagina de Home, curățând URL-ul de acel "?code=..."
        this.router.navigate(['/job-offers']);
      } else {
        console.error('Could not find any authorization code in URL.');
        this.router.navigate(['/job-offers']);
      }
    }).catch(err => {
      console.error('Error at PKCE token change:', err);
    });
  }
}
