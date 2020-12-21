import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { element } from 'protractor';
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
  }

  ngOnInit(): void {
    this.cardService.getAllCards().subscribe(
      (data) => {
        this.allCards = data;
        console.log(data);
        this.allCards.forEach((item: SingleCardModel) => {
          if (item.id == this.router.url.replace('/card/', '')) {
            this.imagePath = item.photoUrl;
            this.difficulty = item.difficulty;
            this.uncommonness = item.uncommonness;
            this.population = item.population;
            this.name = item.name;
            this.id = item.id;
          } else {
            this.imagePath = null;
            this.difficulty = null;
            this.uncommonness = null;
            this.population = null;
            this.name = null;
            this.id = null;
          }
        });
      },
      (error) => {
        throwError(error);
        console.log(error);
      }
    );
    console.log(this.router.url);
  }
}
