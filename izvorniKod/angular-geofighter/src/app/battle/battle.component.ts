import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import {FightService} from './fight.service';
import { CardModel } from './card.model';
import { AuthService } from '../auth/shared/auth.service';
import {Router} from '@angular/router';

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

  //constructor(private fightService: FightService) { }
  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {

    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.authService.role.subscribe((data: string) => this.role = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();

    //this.fightService.getUserCardList(this.authService.getUsername()).subscribe(data => {
      //this.userCards = data;
    //}, error => {
      //throwError(error);
    //});
  }

}
