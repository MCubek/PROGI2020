import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SingleCardModel } from '../single-card/single-card-model';
import { ApplyCardRequestPayload } from '../apply-card/apply-card-request.payload';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  @Output() id: EventEmitter<string> = new EventEmitter();
  constructor(private httpClient: HttpClient) {}

  getAllCards(): Observable<SingleCardModel[]> {
    return this.httpClient.get<SingleCardModel[]>(
      `${environment.apiUrl}api/card/allCards`
    );
  }

  applyCard(applyCardRequestPayload: ApplyCardRequestPayload): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/card/applyCard`,
      applyCardRequestPayload,
      { responseType: 'text' }
    );
  }

  getLocationCard(id: string): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}api/card/` + id, null);
  }
}
