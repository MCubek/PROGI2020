import { Component, Inject, OnInit } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { throwError } from 'rxjs';
import { AdminService } from '../admin.service';
import { ToastrService } from 'ngx-toastr';
import { formatDate } from '@angular/common';
import { UserTimeoutPayload } from './user-timeout.payload';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserApplicationModel } from './user-application.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent implements OnInit {

  users: Array<UserApplicationModel>;
  userTimeoutPayload: UserTimeoutPayload;
  editUserForm: FormGroup;

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService,
    private dialog: MatDialog
  ) {
    this.userTimeoutPayload = {
      username: '',
      timeoutEnd: '',
    };
  }

  ngOnInit(): void {
    this.adminService.getEnabledUsers().subscribe(
      (data) => {
        this.users = data;
      },
      (error) => {
        this.toastr.error('Internal server error');
        throwError(error);
      }
    );

    this.editUserForm = new FormGroup({
      id: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required),
      photoURL: new FormControl('', Validators.required)
    });
  }

  promoteToAdmin(username: string): void {
    if (
      confirm('Are you sure you want to promote ' + username + ' to admin?')
    ) {
      this.adminService.promoteToAdmin(username).subscribe(
        (data) => {
          this.toastr.success('User ' + username + ' promoted to admin');
        },
        (error) => {
          this.toastr.error('Internal server error');
          throwError(error);
        }
      );
    }
  }

  deleteUser(username: string): void {
    if (confirm('Are you sure you want to delete user ' + username + '?')) {
      this.adminService.deleteUser(username).subscribe(
        (data) => {
          window.location.reload();
          this.toastr.success('User ' + username + ' deleted');
        },
        (error) => {
          this.toastr.error('Internal server error');
          throwError(error);
        }
      );
    }
  }

  disableUser(username: string): void {
    const dialogRef = this.dialog.open(DateTimePickerDialog, {
      width: '250px',
      data: { username },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === null) {
        this.toastr.error('Invalid date format');
      } else if (result !== undefined) {
        const formatted = formatDate(result, 'dd/MM/yyyy HH:mm', 'en-US');

        this.userTimeoutPayload.username = username;
        this.userTimeoutPayload.timeoutEnd = formatted;

        this.adminService.setUserTimeout(this.userTimeoutPayload).subscribe(
          (data) => {
            this.toastr.success(
              'User ' + username + ' disabled until ' + formatted
            );
          },
          (error) => {
            this.toastr.error('Bad date or internal server error');
          }
        );
      }
    });
  }

  editForm(userApplicationModel: UserApplicationModel): void {
    this.editUserForm.patchValue({
      id: userApplicationModel.id,
      username: userApplicationModel.username,
      email: userApplicationModel.email,
      photoURL: userApplicationModel.photoURL
    });
  }

  edit(): void {
    this.adminService.editUser(this.editUserForm.getRawValue()).subscribe(
      (data) => {
        this.ngOnInit();
        this.toastr.success('User edited!');
      },
      (error) => {
        this.toastr.error('Internal server error!');
        throwError(error);
      }
    );
  }
}

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'user-list-dialog',
  templateUrl: 'user-list-dialog.html',
})
// tslint:disable-next-line:component-class-suffix
export class DateTimePickerDialog {
  datetime: string;

  constructor(
    public dialogRef: MatDialogRef<DateTimePickerDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { username: string }
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
