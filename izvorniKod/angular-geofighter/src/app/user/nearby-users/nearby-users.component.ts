import {Component, OnInit} from '@angular/core';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {interval, throwError} from 'rxjs';
import {AuthService} from '../../auth/shared/auth.service';
import {SendRequestPayload} from './send-request-payload';
import {startWith, switchMap, take} from 'rxjs/operators';
import {GeolocationService} from "@ng-web-apis/geolocation";
import {UserLocationPayload} from "../../auth/login/user-location.payload";

@Component({
  selector: 'app-nearby-users',
  templateUrl: './nearby-users.component.html',
  styleUrls: ['./nearby-users.component.css']
})
export class NearbyUsersComponent implements OnInit {

  usernames: Array<string>;
  sendRequestPayload: SendRequestPayload;
  requests: Array<string>;
  position: Position;
  userLocationPayload: UserLocationPayload;

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService,
              private authService: AuthService, private readonly geoLocation: GeolocationService) {
    this.sendRequestPayload = {
      usernameReceiver: '',
      usernameSender: '',
      answer: false,
      battleId: 0,
      seen: false
    };
    this.userLocationPayload = {
      latitude: 0.0,
      longitude: 0.0
    };
  }

  ngOnInit(): void {
    this.setLocation();
    interval(3000).pipe(startWith(0), switchMap(() => this.userService.getNearbyUsers(this.authService.getUsername()))).subscribe(data => {
      this.usernames = data;
    }, error => {
      this.toastr.error('Internal server error');
      throwError(error);
    });

    interval(1000).pipe(startWith(0), switchMap(() => this.userService.getRequests(this.authService.getUsername()))
    ).subscribe(data => this.requests = data);

    interval(1000).pipe(startWith(0), switchMap(() => this.userService.getMatches(this.authService.getUsername()))
    ).subscribe(data => {
      if (data.answer) {
        this.sendRequestPayload.usernameSender = data.usernameSender;
        this.sendRequestPayload.usernameReceiver = data.usernameReceiver;
        this.sendRequestPayload.battleId = data.battleId;
        this.router.navigate(['/battle'], {state: {data: this.sendRequestPayload}});
      }
    });

  }

  sendRequest(usernameReceive: string): void {
    this.sendRequestPayload.usernameSender = this.authService.getUsername();
    this.sendRequestPayload.usernameReceiver = usernameReceive;
    this.userService.sendRequest(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }

  acceptRequest(usernameSender: string): void {
    this.sendRequestPayload.usernameSender = usernameSender;
    this.sendRequestPayload.usernameReceiver = this.authService.getUsername();
    this.sendRequestPayload.answer = true;
    this.userService.sendAnswer(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }

  declineRequest(usernameSender: string): void {
    this.sendRequestPayload.usernameSender = usernameSender;
    this.sendRequestPayload.usernameReceiver = this.authService.getUsername();
    this.sendRequestPayload.answer = false;
    this.userService.sendAnswer(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }

  setLocation(): void {
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
