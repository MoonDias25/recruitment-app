import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Home } from './components/home/home';
import { JobOffers } from './components/job-offers/job-offers';
import { RegisterComponent } from './components/register/register';
import { AddJobOffer } from './components/add-job-offer/add-job-offer';
import { ApplyJob } from './components/apply-job/apply-job';
import { authGuard } from './guards/auth.guard';
import { PromotionRequest } from './components/promotion-request/promotion-request';

export const routes: Routes = [
    {path: '', redirectTo: 'job-offers', pathMatch: 'full'},
    {path: 'callback', component: Login},
    {path: 'home', component: Home},
    {path: 'job-offers', component: JobOffers},
    {path: 'register', component: RegisterComponent},
    {path: 'add-job-offer', component: AddJobOffer},
    {path: 'apply-job/:id', component: ApplyJob},
    {path: 'profile', 
     loadComponent: () => import('./pages/profile/profile').then(m => m.Profile),
     canActivate: [authGuard]
    },
    {path: 'promotion-request', component: PromotionRequest}
];
