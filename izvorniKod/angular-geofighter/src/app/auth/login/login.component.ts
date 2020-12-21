import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {LoginRequestPayload} from './login-request.payload';
import {AuthService} from '../shared/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {throwError} from 'rxjs';
import {UserLocationPayload} from './user-location.payload';
import {GeolocationService} from '@ng-web-apis/geolocation';
import {take} from 'rxjs/operators';
import {MyCoordinate} from '../../cartographer/card-applications/CartographerMap/MyComponent';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;
  position: Position;
  userLocationPayload: UserLocationPayload;

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService,
              private activatedRoute: ActivatedRoute, private readonly geoLocation: GeolocationService) {
    this.loginRequestPayload = {
      username: '',
      password: ''
    };
    this.userLocationPayload = {
      latitude: 0.0,
      longitude: 0.0
    };
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    this.activatedRoute.queryParams
      .subscribe(params => {
        if (params.registered !== undefined && params.registered === 'true') {
          this.toastr.success('Signup Successful');
          this.registerSuccessMessage = 'Please check your inbox for activation email. '
            + 'Activate your account before you login.';
        }
      });
  }

  // tslint:disable-next-line:typedef
  login() {
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;

    this.authService.login(this.loginRequestPayload).subscribe(data => {
      this.isError = false;
      this.router.navigateByUrl('');
      this.toastr.success('Login Successful');

      this.setLocation();

    }, error => {
      this.isError = true;
      throwError(error);
    });
  }

  // tslint:disable-next-line:typedef
  setLocation() {
    this.geoLocation.pipe(take(1)).subscribe(position => {
      this.position = position;
      this.userLocationPayload.latitude = this.position.coords.longitude;
      this.userLocationPayload.longitude = this.position.coords.latitude;

      this.authService.saveLocation(this.userLocationPayload).subscribe(
        response => console.log(response),
        err => console.log(err));
    });
  }
}
