import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SignupComponent} from './auth/signup/signup.component';
import {LoginComponent} from './auth/login/login.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './auth/auth.guard';
import {SignupCartographerComponent} from './cartographer/signup-cartographer/signup-cartographer.component';
import {CartographerApplicationsComponent} from './admin/cartographer-applications/cartographer-applications.component';
import {AdminGuard} from './admin/admin.guard';

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'signup', component: SignupComponent},
  {path: 'login', component: LoginComponent},
  {path: 'cartographerApply', component: SignupCartographerComponent, canActivate: [AuthGuard]},
  {path: 'cartographerApplications', component: CartographerApplicationsComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}