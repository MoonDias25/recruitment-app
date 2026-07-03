import { ApplicationConfig, provideBrowserGlobalErrorListeners} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors} from '@angular/common/http';
import { provideOAuthClient, AuthConfig} from 'angular-oauth2-oidc';
import { authInterceptor } from './interceptors/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),
    provideOAuthClient({
      resourceServer: {
        allowedUrls: ['http://localhost/api'], 
        sendAccessToken: true
      }
    }),
  ]
};

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost',
  redirectUri: 'http://localhost/callback',
  clientId: 'angular-client',
  responseType: 'code',
  scope: 'openid profile',
  postLogoutRedirectUri: 'http://localhost/job-offers',
  useSilentRefresh: true,
};
