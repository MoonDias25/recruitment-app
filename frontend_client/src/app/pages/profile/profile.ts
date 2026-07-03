import { Component, inject, OnInit } from '@angular/core';
import { UserProfile } from '../../components/profiles/user-profile/user-profile';
import { HrProfile } from '../../components/profiles/hr-profile/hr-profile';
import { AdminProfile } from '../../components/profiles/admin-profile/admin-profile';
import { Auth } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { CustomUserClaims } from '../../models/user-claims';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, UserProfile, HrProfile, AdminProfile],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class Profile implements OnInit {

  public authService = inject(Auth);
  userProfileInfo: CustomUserClaims | null = null;

  ngOnInit(): void {
    
    this.userProfileInfo = this.authService.getUserInfo();

    console.log('Profile loaded. Role: ', this.authService.getRole());
  }
}
