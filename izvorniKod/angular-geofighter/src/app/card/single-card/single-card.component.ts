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
  imagePath: string;
  difficulty: string;
  uncommonness: string;
  population: string;
  name: string;

  id: string;

  constructor(
    private cardService: CardService,
    private authService: AuthService,
    private router: Router
  ) {
    this.card = {
      id: '',
      name: '',
      description: '',
      photoUrl: '',
      location: '',
      createdBy: '',
      uncommonness: '',
      difficulty: '',
      population: '',
    };
    this.imagePath =
      'https://vlada.gov.hr/userdocsimages//Vijesti/2020/05%20svibanj/19%20svibnja/HN20200427551306.jpg?width=750&height=500&mode=crop';
    this.difficulty = '20';
    this.uncommonness = '50';
    this.population = '35';
    this.name = 'Berlin';
    this.id = '1';
  }

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

    this.cardService.getLocationCard(this.id).subscribe(
      (data) => {
        this.card = data;
      },
      (error) => {
        throwError(error);
      }
    );
  }
}
