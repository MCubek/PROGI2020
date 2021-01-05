import { Component, OnInit } from '@angular/core';
import {throwError} from 'rxjs';
import {AdminService} from '../admin.service';
import {LocationCardModel} from './location-card.model';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';


@Component({
  selector: 'app-cards-page',
  templateUrl: './cards-page.component.html',
  styleUrls: ['./cards-page.component.css']
})
export class CardsPageComponent implements OnInit {

  cards: Array<LocationCardModel>;
  editCardForm: FormGroup;


  constructor(
    private adminService: AdminService,
    private toastr: ToastrService
  ) { }


  // Dohvati sve dostupne i odobrene karte
  ngOnInit(): void {
    this.adminService.getCardCollection().subscribe(data => {
      this.cards = data;
    }, error => {
      throwError(error);
    });
    this.editCardForm = new FormGroup({
      id: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      photoURL: new FormControl('', Validators.required),
      uncommonness: new FormControl('0', [
        Validators.required,
        Validators.min(0),
        Validators.max(10),
      ]),
      difficulty: new FormControl('0', [
        Validators.required,
        Validators.min(0),
        Validators.max(10),
      ]),
      population: new FormControl('0', [
        Validators.required,
        Validators.min(0),
        Validators.max(10),
      ]),
    });
  }

  editForm(locationCardModel: LocationCardModel): void {
    this.editCardForm.patchValue({
      id: locationCardModel.id,
      name: locationCardModel.name,
      description: locationCardModel.description,
      photoURL: locationCardModel.photoURL,
      uncommonness: locationCardModel.uncommonness,
      difficulty: locationCardModel.difficulty,
      population: locationCardModel.population,
    });
  }

  edit(): void {
    this.adminService
      .editCard(this.editCardForm.getRawValue())
      .subscribe(
        (data) => {
          this.ngOnInit();
          this.toastr.success('Card edited!');
        },
        (error) => {
          this.toastr.error('Internal server error!');
          throwError(error);
        }
      );
  }

  delete(cardId: number): void{
    this.adminService.deleteCard(cardId).
    subscribe(data => {
    }, error => {
      throwError(error);
    });
    window.location.reload();
  }

}
