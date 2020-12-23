import { Component, OnInit } from '@angular/core';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {interval, throwError} from 'rxjs';
import {AuthService} from '../../auth/shared/auth.service';
import {SendRequestPayload} from "./send-request-payload";
import {startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-nearby-users',
  templateUrl: './nearby-users.component.html',
  styleUrls: ['./nearby-users.component.css']
})
export class NearbyUsersComponent implements OnInit {

  usernames: Array<string>;
  sendRequestPayload: SendRequestPayload;
  requests: Array<String>;

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService,
              private authService: AuthService) {
    this.sendRequestPayload = {
      usernameReceiver: '',
      usernameSender: '',
      answer: false,
      battleId: 0,
      seen: false
    };
  }

  ngOnInit(): void {
    this.userService.getNearbyUsers(this.authService.getUsername()).subscribe(data => {
      this.usernames = data;
    }, error => {
      this.toastr.error('Internal server error');
      throwError(error);
    });

    interval(1000).pipe(startWith(0),switchMap(() => this.userService.getRequests(this.authService.getUsername()))
    ).subscribe(data => this.requests = data);

    interval(1000).pipe(startWith(0),switchMap(() => this.userService.getMatches(this.authService.getUsername()))
    ).subscribe(data => {
      if(data.answer){
        this.sendRequestPayload.usernameSender = data.usernameSender;
        this.sendRequestPayload.usernameReceiver = data.usernameReceiver;
        this.sendRequestPayload.battleId = data.battleId;
        this.router.navigate(["/battle"], {state: {data:this.sendRequestPayload }});
      }
    });

  }

  sendRequest(usernameReceive: string){
    this.sendRequestPayload.usernameSender = this.authService.getUsername();
    this.sendRequestPayload.usernameReceiver = usernameReceive;
    this.userService.sendRequest(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }

  acceptRequest(usernameSender: string){
    this.sendRequestPayload.usernameSender = usernameSender;
    this.sendRequestPayload.usernameReceiver = this.authService.getUsername();
    this.sendRequestPayload.answer = true;
    this.userService.sendAnswer(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }

  declineRequest(usernameSender: string){
    this.sendRequestPayload.usernameSender = usernameSender;
    this.sendRequestPayload.usernameReceiver = this.authService.getUsername();
    this.sendRequestPayload.answer = false;
    this.userService.sendAnswer(this.sendRequestPayload).subscribe(
      response => console.log(response),
      err => console.log(err));
  }
}
