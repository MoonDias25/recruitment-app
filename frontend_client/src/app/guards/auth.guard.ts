import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) =>{
    const authService = inject(Auth);
    const router = inject(Router);

    if(authService.isLoggedIn()){
        console.log('Is logged. Access granted.');
        return true;
    }else{
        console.warn('No login. Access denied!')
        authService.login();
        return false;
    }
}