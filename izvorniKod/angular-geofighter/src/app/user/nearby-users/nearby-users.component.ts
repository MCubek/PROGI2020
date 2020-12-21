import { Component, OnInit } from '@angular/core';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {throwError} from 'rxjs';
import {AuthService} from '../../auth/shared/auth.service';

@Component({
  selector: 'app-nearby-users',
  templateUrl: './nearby-users.component.html',
  styleUrls: ['./nearby-users.component.css']
})
export class NearbyUsersComponent implements OnInit {

  usernames: Array<string>;

  constructor(private userService: UserService, private router: Router, private toastr: ToastrService,
              private authService: AuthService) {

  }

  ngOnInit(): void {
    this.userService.getNearbyUsers(this.authService.getUsername()).subscribe(data => {
      this.usernames = data;
    }, error => {
      this.toastr.error('Internal server error');
      throwError(error);
    });
  }
}
