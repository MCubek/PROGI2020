import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SignupComponent} from './auth/signup/signup.component';
import {LoginComponent} from './auth/login/login.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './auth/auth.guard';
import {SignupCartographerComponent} from './cartographer/signup-cartographer/signup-cartographer.component';
import {CartographerApplicationsComponent} from './admin/cartographer-applications/cartographer-applications.component';
import {AdminGuard} from './admin/admin.guard';
import {UserListComponent} from './admin/user-list/user-list.component';
import {LeaderboardComponent} from './user/leaderboard/leaderboard.component';
import {AllUsersComponent} from './user/all-users/all-users.component';
import {UserProfileComponent} from "./user/user-profile/user-profile.component";

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'signup', component: SignupComponent},
  {path: 'login', component: LoginComponent},
  {path: 'cartographerApply', component: SignupCartographerComponent, canActivate: [AuthGuard]},
  {path: 'cartographerApplications', component: CartographerApplicationsComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: 'leaderboard', component: LeaderboardComponent, canActivate: [AuthGuard]},
  {path: 'userList', component: UserListComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: 'users', component: AllUsersComponent, canActivate: [AuthGuard]},
  {path: 'users/:username', component: UserProfileComponent, canActivate:[AuthGuard]},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
