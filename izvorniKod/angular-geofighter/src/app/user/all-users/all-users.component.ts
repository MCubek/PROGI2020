import {Component, OnInit} from '@angular/core';
import {throwError} from 'rxjs';
import {UserService} from '../user.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from "@angular/router";
import {AuthService} from "../../auth/shared/auth.service";

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit {

  usernames: Array<string>;
  profile: Array<string>;
  username: String;

  constructor(private userService: UserService, private router: Router,
              private toastr: ToastrService, private authService: AuthService) {

  }

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.userService.getEnabledUsers().subscribe(data => {
      this.usernames = data;
    }, error => {
      this.toastr.error("Internal server error");
      throwError(error);
    });
  }

  moveToViewProfile(username: string): void{
    this.router.navigateByUrl("users/"+username);
  }
  moveToMyProfile(): void{
    this.router.navigateByUrl("users/"+this.authService.getUsername());
  }
}


