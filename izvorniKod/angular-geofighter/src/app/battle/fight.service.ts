import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CardModel} from './card.model';
import {environment} from '../../environments/environment';
import {SubmitCardModel} from "./submitCard.model";
import {GetWinnerModel} from "./get-winner/getWinner.model";

@Injectable({
  providedIn: 'root'
})
export class FightService {

  constructor(private httpClient: HttpClient) {
  }

  getUserCardList(): Observable<CardModel[]> {
      return this.httpClient.get<CardModel[]>(`${environment.apiUrl}api/fight/userCardList/`);
  }

  submitCards(cardList: Array<SubmitCardModel>): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}api/fight/submitCards/`, cardList);
  }

  startFight(fightId: number): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}api/fight/startFight/` + fightId, 'text');
  }

  getWinner(fightId: number): Observable<GetWinnerModel> {
    return this.httpClient.get<GetWinnerModel>(`${environment.apiUrl}api/fight/getWinner/` + fightId);
  }

}
