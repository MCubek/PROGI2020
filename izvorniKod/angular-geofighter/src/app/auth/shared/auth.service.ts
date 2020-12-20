import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SignupRequestPayload} from '../signup/signup-request.payload';
import {Observable, throwError} from 'rxjs';
import {LoginRequestPayload} from '../login/login-request.payload';
import {map, tap} from 'rxjs/operators';
import {LocalStorageService} from 'ngx-webstorage';
import {LoginResponsePayload} from '../login/login-response.payload';
import {environment} from '../../../environments/environment';
import {SignupCartographerRequestPayload} from '../../cartographer/signup-cartographer/signup-cartographer-request-payload';
import {ToastrService} from 'ngx-toastr';
import {UserLocationPayload} from "../login/user-location.payload";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();
  @Output() role: EventEmitter<string> = new EventEmitter();

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUsername()
  };

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService,
              private toastr: ToastrService) {
  }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post(`${environment.apiUrl}api/auth/signup`, signupRequestPayload, {responseType: 'text'});
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.httpClient.post<LoginResponsePayload>(`${environment.apiUrl}api/auth/login`, loginRequestPayload)
      .pipe(map(data => {
        this.localStorage.store('authorizationToken', data.authorizationToken);
        this.localStorage.store('username', data.username);
        this.localStorage.store('refreshToken', data.refreshToken);
        this.localStorage.store('expiresAt', data.expiresAt);
        this.localStorage.store('role', data.role);

        this.loggedIn.emit(true);
        this.username.emit(data.username);
        this.role.emit(data.role);

        return true;
      }));
  }

  getJwtToken(): string {
    return this.localStorage.retrieve('authorizationToken');
  }

  // tslint:disable-next-line:typedef
  refreshToken() {
    return this.httpClient.post<LoginResponsePayload>(`${environment.apiUrl}api/auth/refresh`, this.refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.clear('authorizationToken');
        this.localStorage.clear('expiresAt');
        this.localStorage.clear('role');

        this.localStorage.store('authorizationToken',
          response.authorizationToken);
        this.localStorage.store('expiresAt', response.expiresAt);
        this.localStorage.store('role', response.role);
      }));
  }

  // tslint:disable-next-line:typedef
  logout() {
    this.httpClient.post(`${environment.apiUrl}api/auth/logout`, this.refreshTokenPayload,
      {responseType: 'text'})
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      });
    this.toastr.success('Logged out!');
    this.localStorage.clear('authorizationToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
    this.localStorage.clear('role');
  }

  cartographerSignup(signupCartographerRequestPayload: SignupCartographerRequestPayload): Observable<any> {
    return this.httpClient.post(`${environment.apiUrl}api/auth/cartographer/apply`
      , signupCartographerRequestPayload, {responseType: 'text'});
  }

  getUsername(): string {
    return this.localStorage.retrieve('username');
  }

  getRefreshToken(): string {
    return this.localStorage.retrieve('refreshToken');
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }

  getRole(): string {
    return this.localStorage.retrieve('role');
  }

  saveLocation(userLocationPayload : UserLocationPayload) : Observable<any>{
    return this.httpClient.post(`${environment.apiUrl}api/auth/storeLocation`, userLocationPayload,{responseType: 'text'});
  }
}
