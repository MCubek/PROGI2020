import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CartographerApplicantModel} from './cartographer-applications/cartographer-applicant.model';
import {environment} from '../../environments/environment';

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
}
