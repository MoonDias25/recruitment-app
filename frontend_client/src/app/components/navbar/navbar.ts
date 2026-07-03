import { Component, inject, ChangeDetectorRef, OnInit } from '@angular/core';
import { Auth } from '../../services/auth';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Promotion } from '../../services/promotion';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit{
  public authService = inject(Auth);
  public promotionService = inject(Promotion);
  private oauthService = inject(OAuthService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
  this.oauthService.loadDiscoveryDocumentAndTryLogin()
    .then(() => {      
      if (this.authService.isLoggedIn() && this.authService.isAdmin()) {
        this.promotionService.loadPendingRequestsCount();
      }
      
    })
    .catch(err => console.error('OAuth Startup Error:', err));
  }

  navigateToAdminPromotions(): void {
    window.location.assign(`${window.location.origin}/admin/promotions/pending`);
  }

  navigateToUserList(): void{
    window.location.assign(`${window.location.origin}/admin/employees`);
  }

  navigateToAllUserList(): void{
    window.location.assign(`${window.location.origin}/admin/employees/global`);
  }
}
