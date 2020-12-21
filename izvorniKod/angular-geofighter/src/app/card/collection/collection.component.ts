import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { throwError } from 'rxjs';

import { CardService } from '../shared/card.service';
import { SingleCardModel } from '../single-card/single-card-model';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
  styleUrls: ['./collection.component.css'],
})
export class CollectionComponent implements OnInit {
  isLoggedIn: boolean;
  username: string;
  role: string;
  allCards: Array<SingleCardModel>;
  id: string;
  card: SingleCardModel;

  constructor(private cardService: CardService) {
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
      },
      (error) => {
        throwError(error);
        console.log(error);
      }
    );
  }
}
