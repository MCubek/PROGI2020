import { Component, OnInit } from '@angular/core';
import {UserService} from '../user.service';
import {ToastrService} from "ngx-toastr";
import {throwError} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {LeaderboardUserModel} from "../leaderboard/leaderboard-user.model";
import index from "@angular/cli";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  username: string;
  profile: Array <string>;
  users : Array<LeaderboardUserModel>;
  i: number;

  constructor(private userService: UserService, private route: ActivatedRoute,
              private toastr: ToastrService) {

  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.username = params['username'];
    })
    this.userService.viewProfile(this.username).subscribe(data => {

      this.profile = data;
    }, error => {
      this.toastr.error("Internal server error");
      throwError(error);
    });
    this.userService.getLeaderboardUserInfo().subscribe(data => {
      this.users = data;
    }, error => {
      throwError(error);
    });
  }
}
