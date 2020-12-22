import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CardModel} from './card.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FightService {

  constructor(private httpClient: HttpClient) {
  }

  getUserCardList(username: string): Observable<CardModel[]> {
    return this.httpClient.get<CardModel[]>(`${environment.apiUrl}api/fight/userCardList/`+ username);
  }

}
