import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LeaderboardUserModel} from './leaderboard/leaderboard-user.model';
import {environment} from '../../environments/environment';
import {SendRequestPayload} from "./nearby-users/send-request-payload";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  getLeaderboardUserInfo(): Observable<LeaderboardUserModel[]> {
      return this.httpClient.get<LeaderboardUserModel[]>(`${environment.apiUrl}api/user/leaderboard`);
  }

  getEnabledUsers(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}api/user/userList`);
  }

  viewProfile(username: string): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}api/user/userProfile/`+username);
  }

  getNearbyUsers(username: string): Observable<string[]>{
    return this.httpClient.get<string[]>(`${environment.apiUrl}api/user/nearbyUsers`+username);
  }

  sendRequest(sendRequestPayload: SendRequestPayload): Observable<any> {
    return this.httpClient.post(`${environment.apiUrl}api/fight/sendRequest`, sendRequestPayload, {responseType: 'text'});
  }

  getRequests(username: string): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}api/fight/getRequests`+username);
  }

  sendAnswer(sendRequestPayload: SendRequestPayload): Observable<any>{
    return this.httpClient.post(`${environment.apiUrl}api/fight/receiveAnswer`, sendRequestPayload, {responseType: 'text'});
  }

  getMatches(username: string): Observable<SendRequestPayload> {
    return this.httpClient.get<SendRequestPayload>(`${environment.apiUrl}api/fight/getMatches`+username);
  }

}
