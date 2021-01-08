import { Component, OnInit } from '@angular/core';
import { CardApplicationService } from '../shared/card-application.service';
import { CardApplicationModel } from './card-application.model';
import { throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MyCoordinate } from './CartographerMap/MyComponent';

@Component({
  selector: 'app-card-applications',
  templateUrl: './card-applications.component.html',
  styleUrls: ['./card-applications.component.css'],
})
export class CardApplicationsComponent implements OnInit {
  cardApplications: Array<CardApplicationModel>;
  editCardForm: FormGroup;

  coordinatesList: Coordinates[] = [];
  mapShowed = false;

  constructor(
    private cardApplicationService: CardApplicationService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.cardApplicationService.getAllCardApplications().subscribe(
      (data) => {
        this.cardApplications = data;
      },
      (error) => {
        throwError(error);
      }
    );
    this.editCardForm = new FormGroup({
      id: new FormControl('', Validators.required),
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      photoURL: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      createdBy: new FormControl('', Validators.required),
      needsToBeChecked: new FormControl(false),
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

  accept(id: string): void {
    if (confirm('Are you sure you want to accept this card?')) {
      this.cardApplicationService.acceptCard(id).subscribe(
        (data) => {
          this.ngOnInit();
          this.toastr.success('Card application accepted!');
        },
        (error) => {
          this.toastr.error('Internal server error!');
          throwError(error);
        }
      );
    }
  }

  decline(id: string): void {
    if (confirm('Are you sure you want to decline this card?')) {
      this.cardApplicationService.declineCard(id).subscribe(
        (data) => {
          this.ngOnInit();
          this.toastr.error('Card application declined!');
        },
        (error) => {
          this.toastr.error('Internal server error!');
          throwError(error);
        }
      );
    }
  }

  editForm(cardApplicationModel: CardApplicationModel): void {
    this.editCardForm.patchValue({
      id: cardApplicationModel.id,
      name: cardApplicationModel.name,
      description: cardApplicationModel.description,
      photoURL: cardApplicationModel.photoURL,
      location: cardApplicationModel.location,
      createdBy: cardApplicationModel.createdBy,
      needsToBeChecked: cardApplicationModel.needsToBeChecked,
      uncommonness: cardApplicationModel.uncommonness,
      difficulty: cardApplicationModel.difficulty,
      population: cardApplicationModel.population,
    });
  }

  edit(): void {
    this.cardApplicationService
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

  requestConfirmation(id: string): void {
    if (
      confirm('Are you sure you want to request confirmation for this card?')
    ) {
      this.cardApplicationService.requestConfirmation(id).subscribe(
        (data) => {
          this.ngOnInit();
          this.toastr.info('Card confirmation requested!');
        },
        (error) => {
          this.toastr.error('Internal server error!');
          throwError(error);
        }
      );
    }
  }

  toggleMapShowed(): void {
    if (!this.mapShowed) {
      this.populateCoordinatesList();
    } else {
      this.mapShowed = false;
    }
  }

  populateCoordinatesList(): void {
    this.coordinatesList = [];

    this.cardApplicationService.getAllCardToBeCheckedCoordinates().subscribe(
      (data) => {
        data.forEach((value) => {
          const myCord = new MyCoordinate(value.latitude, value.longitude);
          this.coordinatesList.push(myCord);
          this.mapShowed = true;
        });
      },
      (error) => {
        this.toastr.error('Error while getting coordinates!');
        throwError(error);
      }
    );
  }
}
