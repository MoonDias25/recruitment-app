import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { authCodeFlowConfig } from './app.config';
import { OAuthService } from 'angular-oauth2-oidc';
import { Navbar } from './components/navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App{

  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(authCodeFlowConfig);
    
    this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      console.log('Authentication... Success!.');
    }).catch(err => {
      console.error('Error at configuration login', err);
    });
    
    this.oauthService.setupAutomaticSilentRefresh();
  }
}
