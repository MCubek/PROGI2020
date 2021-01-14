import {Component, OnInit} from '@angular/core';
import {throwError} from 'rxjs';
import {LeaderboardUserModel} from './leaderboard-user.model';
import {UserService} from '../user.service';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {

  users: Array<LeaderboardUserModel>;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
      this.userService.getLeaderboardUserInfo().subscribe(data => {
        this.users = data;
      }, error => {
        throwError(error);
      });
  }
}
