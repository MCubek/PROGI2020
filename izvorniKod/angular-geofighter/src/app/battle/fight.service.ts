import {Injectable} from '@angular/core';
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
    return this.httpClient.get<CardModel[]>(`${environment.apiUrl}api/fight/userCardList/` + username);
  }

  submitCards(cardList: Array<bigint>): Observable<any> {
    return this.httpClient.put<Array<bigint>>('${environment.apiUrl}api/fight/submitCards/', 'text');
  }

  startFight(fightId: number): Observable<any> {
    return this.httpClient.put('${environment.apiUrl}api/fight/startFight/' + fightId, 'text');
  }

  getWinner(fightId: number): Observable<string> {
    return this.httpClient.get<string>('${environment.apiUrl}api/fight/getWinner/' + fightId);
  }

}
