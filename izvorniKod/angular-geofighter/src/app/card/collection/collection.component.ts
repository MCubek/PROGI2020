import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
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

  constructor(private cardService: CardService) {}

  ngOnInit(): void {
    this.cardService.getAllCards().subscribe(
      (data) => {
        this.allCards = data;
      },
      (error) => {
        throwError(error);
      }
    );
  }
}
