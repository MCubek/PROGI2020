import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {CardApplicationModel} from '../card-applications/card-application.model';

@Injectable({
  providedIn: 'root',
})
export class CardApplicationService {
  @Output() id: EventEmitter<string> = new EventEmitter();
  constructor(private httpClient: HttpClient) {}

  getAllCardApplications(): Observable<CardApplicationModel[]> {
    return this.httpClient.get<CardApplicationModel[]>(
      `${environment.apiUrl}api/cardApplications/all`
    );
  }

  acceptCard(id: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/cardApplications/accept/` + id,
      null,
      {responseType: 'text'}
    );
  }

  declineCard(id: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/cardApplications/decline/` + id,
      null,
      {responseType: 'text'}
    );
  }

  editCard(cardApplicationModel: CardApplicationModel): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/cardApplications/edit`,
      cardApplicationModel,
      {responseType: 'text'}
    );
  }

  requestConfirmation(id: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/cardApplications/confirm/` + id,
      null,
      {responseType: 'text'}
    );
  }

}
