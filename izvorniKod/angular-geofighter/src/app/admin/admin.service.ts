import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CartographerApplicantModel} from './cartographer-applications/cartographer-applicant.model';
import {UserTimeoutPayload} from './user-list/user-timeout.payload';
import {environment} from '../../environments/environment';
import {LocationCardModel} from './cards-page/location-card.model';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private httpClient: HttpClient) {
  }

  getAllCartographerApplications(): Observable<CartographerApplicantModel[]> {
    return this.httpClient.get<CartographerApplicantModel[]>(
      `${environment.apiUrl}api/admin/cartographerApplications`
    );
  }

  acceptCartographer(username: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/admin/cartographerApplications/accept/` +
      username,
      null
    );
  }

  getCardCollection(): Observable<LocationCardModel[]> {
    return this.httpClient.get<LocationCardModel[]>(`${environment.apiUrl}api/admin/allCards`);
  }

  deleteCard(cardId: number): Observable<any> {
    return this.httpClient.delete(`${environment.apiUrl}api/card/` + cardId);
  }

  editCard(locationCardModel: LocationCardModel): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/card/edit`,
      locationCardModel,
      {responseType: 'text'}
    );
  }

  getEnabledUsernames(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${environment.apiUrl}api/admin/userList`);
  }

  promoteToAdmin(username: string): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}api/admin/promoteToAdmin/` + username, null, {responseType: 'text'});
  }

  deleteUser(username: string): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}api/admin/deleteUser/` + username, null, {responseType: 'text'});
  }

  setUserTimeout(userTimeoutPayload: UserTimeoutPayload): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}api/admin/disableUser`, userTimeoutPayload, {responseType: 'text'});
  }

}
