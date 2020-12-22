import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SignupComponent} from './auth/signup/signup.component';
import {LoginComponent} from './auth/login/login.component';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './auth/auth.guard';
import {SignupCartographerComponent} from './cartographer/signup-cartographer/signup-cartographer.component';
import {CartographerApplicationsComponent} from './admin/cartographer-applications/cartographer-applications.component';
import {AdminGuard} from './admin/admin.guard';
import {CollectionComponent} from './card/collection/collection.component';
import {SingleCardComponent} from './card/single-card/single-card.component';
import {ApplyCardComponent} from './card/apply-card/apply-card.component';
import {BattleComponent} from './battle/battle.component';
import {CardsPageComponent} from './admin/cards-page/cards-page.component';
import {UserListComponent} from './admin/user-list/user-list.component';
import {LeaderboardComponent} from './user/leaderboard/leaderboard.component';
import {AllUsersComponent} from './user/all-users/all-users.component';
import {UserProfileComponent} from './user/user-profile/user-profile.component';
import {NearbyUsersComponent} from './user/nearby-users/nearby-users.component';
import {CardApplicationsComponent} from './cartographer/card-applications/card-applications.component';
import {CartographerGuard} from './cartographer/cartographer.guard';

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'signup', component: SignupComponent},
  {path: 'login', component: LoginComponent},
  {path: 'battle', component: BattleComponent, canActivate: [AuthGuard]},
  {path: 'cartographerApply', component: SignupCartographerComponent, canActivate: [AuthGuard]},
  {path: 'card/allCards', component: CollectionComponent},
  {path: 'card/applyCard', component: ApplyCardComponent},
  {path: 'card/:id', component: SingleCardComponent},
  {
    path: 'admin/cartographerApplications',
    component: CartographerApplicationsComponent,
    canActivate: [AuthGuard, AdminGuard]
  },
  {path: 'admin/cards', component: CardsPageComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: 'leaderboard', component: LeaderboardComponent, canActivate: [AuthGuard]},
  {path: 'admin/users', component: UserListComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: 'nearbyUsers', component: NearbyUsersComponent, canActivate: [AuthGuard]},
  {
    path: 'cartographer/cardApplications',
    component: CardApplicationsComponent,
    canActivate: [AuthGuard, CartographerGuard]
  },
  {path: 'users', component: AllUsersComponent, canActivate: [AuthGuard]},
  {path: 'users/:username', component: UserProfileComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
