import { Component, OnInit } from '@angular/core';
import {throwError} from 'rxjs';
import {AdminService} from '../admin.service';
import {LocationCardModel} from './location-card.model';



@Component({
  selector: 'app-cards-page',
  templateUrl: './cards-page.component.html',
  styleUrls: ['./cards-page.component.css']
})
export class CardsPageComponent implements OnInit {

  cards: Array<LocationCardModel>;
  

  constructor(private adminService: AdminService) { }


  // Dohvati sve dostupne i odobrene karte
  ngOnInit(): void {
    this.adminService.getCardCollection().subscribe(data =>{
      this.cards = data;
    }, error => {
      throwError(error);
    });
  }

  // Brisanje karte po id-u

  delete(cardId : number): void{
    this.adminService.deleteCard(cardId).
    subscribe(data => {
    }, error =>{
      throwError(error);
    });
    window.location.reload();
  }

}
