import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {throwError} from 'rxjs';
import {AuthService} from 'src/app/auth/shared/auth.service';
import {CardService} from '../shared/card.service';

@Component({
  selector: 'app-single-card',
  templateUrl: './single-card.component.html',
  styleUrls: ['./single-card.component.css'],
})
export class SingleCardComponent implements OnInit {

  cardName: string;
  description: string;
  creator: string;
  createdTime: string;
  imageURL: string;
  difficulty: string;
  uncommonness: string;
  population: string;
  location: string;

  id: string;

  constructor(
    private cardService: CardService,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.id = this.router.url.replace('/card/', '');

    this.cardService.getCard(this.id).subscribe(
      (data) => {
        this.cardName = data.name;
        this.description = data.description;
        this.creator = data.createdBy;
        this.createdTime = data.createdTime;
        this.imageURL = data.photoURL;
        this.difficulty = data.difficulty;
        this.uncommonness = data.uncommonness;
        this.population = data.population;
        this.location = data.location;
      },
      (error) => {
        throwError(error);
      }
    );
  }
}
