import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CartographerApplicantModel } from './cartographer-applications/cartographer-applicant.model';
import { UserTimeoutPayload } from './user-list/user-timeout.payload';
import { environment } from '../../environments/environment';
import { LocationCardModel } from './cards-page/location-card.model';
import { UserApplicationModel } from './user-list/user-application.model';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private httpClient: HttpClient) {}

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
    return this.httpClient.get<LocationCardModel[]>(
      `${environment.apiUrl}api/admin/allCards`
    );
  }

  deleteCard(cardId: number): Observable<any> {
    return this.httpClient.delete(`${environment.apiUrl}api/admin/` + cardId);
  }

  editCard(locationCardModel: LocationCardModel): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/admin/edit`,
      locationCardModel,
      {responseType: 'text'}
    );
  }

  getEnabledUsernames(): Observable<string[]> {
    return this.httpClient.get<string[]>(
      `${environment.apiUrl}api/admin/userList`
    );
  }

  promoteToAdmin(username: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/admin/promoteToAdmin/` + username,
      null,
      { responseType: 'text' }
    );
  }

  deleteUser(username: string): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/admin/deleteUser/` + username,
      null,
      { responseType: 'text' }
    );
  }

  setUserTimeout(userTimeoutPayload: UserTimeoutPayload): Observable<any> {
    return this.httpClient.put(
      `${environment.apiUrl}api/admin/disableUser`,
      userTimeoutPayload,
      { responseType: 'text' }
    );
  }

  editUser(userApplicationModel: UserApplicationModel): Observable<any> {
    return this.httpClient.post(
      `${environment.apiUrl}api/admin/edit`,
      userApplicationModel,
      { responseType: 'text' }
    );
  }

  getAllUserModels(): Observable<UserApplicationModel[]> {
    return this.httpClient.get<UserApplicationModel[]>(
      `${environment.apiUrl}api/admin/all`
    );
  }
}
