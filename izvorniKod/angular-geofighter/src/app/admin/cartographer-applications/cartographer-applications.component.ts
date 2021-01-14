import {Component, OnInit} from '@angular/core';
import {CartographerApplicantModel} from './cartographer-applicant.model';
import {AdminService} from '../admin.service';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-cartographer-applications',
  templateUrl: './cartographer-applications.component.html',
  styleUrls: ['./cartographer-applications.component.css']
})
export class CartographerApplicationsComponent implements OnInit {

  applicants: Array<CartographerApplicantModel>;

  constructor(private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.adminService.getAllCartographerApplications().subscribe(data => {
      this.applicants = data;
    }, error => {
      throwError(error);
    });
  }

  accept(username: string): void {
    this.adminService.acceptCartographer(username).subscribe(data => {
    }, error => {
      throwError(error);
    });
    window.location.reload();
  }

  decline(username: string): void{
    this.adminService.declineCartographer(username).subscribe(data => {
    }, error => {
      throwError(error);
    });
    window.location.reload();
  }

}
