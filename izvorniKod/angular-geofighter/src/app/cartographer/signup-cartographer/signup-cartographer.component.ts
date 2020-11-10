import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../auth/shared/auth.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {SignupCartographerRequestPayload} from './signup-cartographer-request-payload';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-signup-cartographer',
  templateUrl: './signup-cartographer.component.html',
  styleUrls: ['./signup-cartographer.component.css']
})
export class SignupCartographerComponent implements OnInit {

  signupCartographerRequestPayload: SignupCartographerRequestPayload;
  signupForm: FormGroup;

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {
    this.signupCartographerRequestPayload = {
      iban: '',
      idPhotoURL: ''
    };
  }

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      iban: new FormControl('', Validators.required),
      idPhotoURL: new FormControl(''
        , [Validators.required, Validators.pattern('(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?')])
    });
  }

  // tslint:disable-next-line:typedef
  cartographerSignup() {
    this.signupCartographerRequestPayload.iban = this.signupForm.get('iban').value;
    this.signupCartographerRequestPayload.idPhotoURL = this.signupForm.get('idPhotoURL').value;

    // Dodati provjeru signup?
    this.authService.cartographerSignup(this.signupCartographerRequestPayload).subscribe(data => {
      this.router.navigate(['/']);
      this.toastr.success('Successfully applied!');
    }, error => {
      console.log(error);
      this.toastr.error('Cartographer Signup Failed! Please try again');
    });
  }

}
