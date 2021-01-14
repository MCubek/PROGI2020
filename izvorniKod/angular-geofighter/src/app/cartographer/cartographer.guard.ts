import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from '../auth/shared/auth.service';

@Injectable({
  providedIn: 'root'
})
export class CartographerGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const isCartographer = this.authService.getRole() === 'ROLE_CARTOGRAPHER' || this.authService.getRole() === 'ROLE_ADMIN';

    if (!isCartographer) { this.router.navigateByUrl('/'); }

    return true;
  }

}
