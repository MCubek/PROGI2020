import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {SingleCardModel} from '../single-card/single-card-model';
import {ApplyCardRequestPayload} from '../apply-card/apply-card-request.payload';
import {LocalStorageService} from 'ngx-webstorage';
import {NearbyCardModel} from '../nearby/nearby-card.model';
import {UserLocationPayload} from '../../auth/login/user-location.payload';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  @Output() id: EventEmitter<string> = new EventEmitter();

  constructor(private httpClient: HttpClient,
              private localStorage: LocalStorageService, ) {
  }

  getAllCards(): Observable<SingleCardModel[]> {
    return this.httpClient.get<SingleCardModel[]>(
      `${environment.apiUrl}api/card/allCards`
    );
  }

  applyCard(applyCardRequestPayload: ApplyCardRequestPayload): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/card/applyCard`,
      applyCardRequestPayload,
      {responseType: 'text'}
    );
  }

  getCard(id: string): Observable<SingleCardModel> {
    return this.httpClient.get<SingleCardModel>(
      `${environment.apiUrl}api/card/` + id
    );
  }

  getNearbyCards(): Observable<NearbyCardModel[]> {
    return this.httpClient.get<NearbyCardModel[]>(
      `${environment.apiUrl}api/card/getNearby`);
  }

  collectCard(id: string): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/card/collect`,
      id,
      {responseType: 'text'}
    );
  }

  getUserPosition(): UserLocationPayload {
    return {
      longitude: this.localStorage.retrieve('long'),
      latitude: this.localStorage.retrieve('lat')
    };
  }
}
