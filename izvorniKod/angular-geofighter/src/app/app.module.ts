import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {SignupComponent} from './auth/signup/signup.component';
import {ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './auth/login/login.component';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';
import {HomeComponent} from './home/home.component';
import {SignupCartographerComponent} from './cartographer/signup-cartographer/signup-cartographer.component';
import {TokenInterceptor} from './token-interceptor';
import {CartographerApplicationsComponent} from './admin/cartographer-applications/cartographer-applications.component';
import {LeaderboardComponent} from './user/leaderboard/leaderboard.component';
import {UserListComponent} from './admin/user-list/user-list.component';
import {DateTimePickerDialog} from './admin/user-list/user-list.component';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {NearbyUsersComponent} from './user/nearby-users/nearby-users.component';
import {CardApplicationsComponent} from './cartographer/card-applications/card-applications.component';
import {CartographerMapComponent} from './cartographer/card-applications/CartographerMap/cartographer-map.component';
import {UserProfileComponent} from './user/user-profile/user-profile.component';
import {AllUsersComponent} from './user/all-users/all-users.component';
import {BattleComponent} from './battle/battle.component';
import {CardsPageComponent} from './admin/cards-page/cards-page.component';
import {CollectionComponent} from './card/collection/collection.component';
import {SingleCardComponent} from './card/single-card/single-card.component';
import {ApplyCardComponent} from './card/apply-card/apply-card.component';
import { NearbyComponent } from './card/nearby/nearby.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SignupComponent,
    LoginComponent,
    HomeComponent,
    SignupCartographerComponent,
    CartographerApplicationsComponent,
    CollectionComponent,
    SingleCardComponent,
    ApplyCardComponent,
    CardsPageComponent,
    LeaderboardComponent,
    UserListComponent,
    DateTimePickerDialog,
    NearbyUsersComponent,
    CardApplicationsComponent,
    CartographerMapComponent,
    UserProfileComponent,
    AllUsersComponent,
    BattleComponent,
    NearbyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxWebstorageModule.forRoot(),
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatDialogModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
