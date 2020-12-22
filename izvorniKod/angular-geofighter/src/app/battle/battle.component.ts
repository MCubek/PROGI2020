import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import {FightService} from './fight.service';
import { CardModel } from './card.model';
import { AuthService } from '../auth/shared/auth.service';
import {Router} from '@angular/router';
import {SendRequestPayload} from "../user/nearby-users/send-request-payload";

@Component({
  selector: 'app-battle',
  templateUrl: './battle.component.html',
  styleUrls: ['./battle.component.css']
})
export class BattleComponent implements OnInit {

  isLoggedIn: boolean;
  username: string;
  role: string;
  userCards: Array<CardModel>;
  match: SendRequestPayload;

  constructor(private authService: AuthService, private router: Router, private fightService: FightService) {
  }

  ngOnInit(): void {

    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.authService.role.subscribe((data: string) => this.role = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();

    this.match = history.state.data;
    console.log(this.match.usernameSender+', '+this.match.usernameReceiver);

    this.fightService.getUserCardList(this.username).subscribe(data => {
      this.userCards = data;
    }, error => {
      throwError(error);
    });
  }

}
