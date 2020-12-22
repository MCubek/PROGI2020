import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ApplyCardRequestPayload } from './apply-card-request.payload';
import { AuthService } from '../../auth/shared/auth.service';
import { CardService } from '../shared/card.service';
import {GeolocationService} from '@ng-web-apis/geolocation';
import {take} from 'rxjs/operators';
@Component({
  selector: 'app-apply-card',
  templateUrl: './apply-card.component.html',
  styleUrls: ['./apply-card.component.css'],
})
export class ApplyCardComponent implements OnInit {
  applyCardRequestPayload: ApplyCardRequestPayload;
  applyCardForm: FormGroup;

  constructor(
    private cardService: CardService,
    private router: Router,
    private toastr: ToastrService,
    private readonly geoLocation: GeolocationService
  ) {
    this.applyCardRequestPayload = {
      name: '',
      description: '',
      photoUrl: '',
      location: '',
      createdBy: '',
      difficulty: 1,
      population: 1,
      uncommonness: 1
    };
  }

  ngOnInit(): void {
    this.applyCardForm = new FormGroup({
      name: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      photoUrl: new FormControl('', [
        Validators.required,
        Validators.pattern(
          '^https?://(?:[a-z0-9-]+.)+[a-z]{2,6}(?:/[^/#?]+)+.(?:jpg|jpeg|png)$'
        ),
      ]),
      difficulty: new FormControl('1', Validators.required),
      population: new FormControl('1', Validators.required),
      uncommonness: new FormControl('1', Validators.required)
    });
  }

  applyCardGetLocation() {
    this.geoLocation.pipe(take(1)).subscribe(position => {
      this.applyCard(position);
    })
  }

  // tslint:disable-next-line:typedef
  applyCard(position: Position) {
    this.applyCardRequestPayload.name = this.applyCardForm.get('name').value;
    this.applyCardRequestPayload.description = this.applyCardForm.get('description').value;
    this.applyCardRequestPayload.photoUrl = this.applyCardForm.get('photoUrl').value;
    this.applyCardRequestPayload.location = position.coords.longitude+" "+position.coords.latitude
    this.applyCardRequestPayload.difficulty = this.applyCardForm.get('difficulty').value;
    this.applyCardRequestPayload.population = this.applyCardForm.get('population').value;
    this.applyCardRequestPayload.uncommonness = this.applyCardForm.get('uncommonness').value;

    this.cardService.applyCard(this.applyCardRequestPayload).subscribe(
      (data) => {
        this.router.navigate(['/home'], { queryParams: {} });
      },
      (error) => {
        console.log(error);
        this.toastr.error('Card application failed');
      }
    );
  }
}
