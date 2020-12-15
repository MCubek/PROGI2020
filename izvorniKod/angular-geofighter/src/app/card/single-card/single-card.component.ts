import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { CardService } from '../shared/card.service';
import { SingleCardModel } from '../single-card/single-card-model';

@Component({
  selector: 'app-single-card',
  templateUrl: './single-card.component.html',
  styleUrls: ['./single-card.component.css'],
})
export class SingleCardComponent implements OnInit {
  isLoggedIn: boolean;
  username: string;
  role: string;
  card: SingleCardModel;
  allCards: Array<SingleCardModel>;

  constructor(
    private cardService: CardService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.loggedIn.subscribe(
      (data: boolean) => (this.isLoggedIn = data)
    );
    this.authService.username.subscribe(
      (data: string) => (this.username = data)
    );
    this.authService.role.subscribe((data: string) => (this.role = data));
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();

    /*
    this.cardService.getLocationCard().subscribe(
      (data) => {
        this.card = data;
      },
      (error) => {
        throwError(error);
      }
    );
    */
  }
}
