import {Component, OnInit} from '@angular/core';
import {interval, throwError, timer} from 'rxjs';
import {FightService} from './fight.service';
import {CardModel} from './card.model';
import {AuthService} from '../auth/shared/auth.service';
import {Router} from '@angular/router';
import {SendRequestPayload} from '../user/nearby-users/send-request-payload';
import {ToastrService} from 'ngx-toastr';
import {SubmitCardModel} from './submitCard.model';
import {startWith, switchMap} from 'rxjs/operators';

@Component({
  selector: 'app-battle',
  templateUrl: './battle.component.html',
  styleUrls: ['./battle.component.css']
})
export class BattleComponent implements OnInit {

  isLoggedIn: boolean;
  username: string;
  userCards: Array<CardModel>;
  match: SendRequestPayload;
  opponent: string;
  ready: boolean;

  cardsPicked = false;
  pickedCards: Array<number> = new Array<number>();

  constructor(private authService: AuthService, private router: Router, private fightService: FightService, private toastr: ToastrService) {
    this.ready = false;
  }

  ngOnInit(): void {

    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();

    this.match = history.state.data;
    if (this.match === undefined) {
      this.router.navigateByUrl('/nearbyUsers');
    } else {
      this.fightService.getUserCardList().subscribe(data => {
        this.userCards = data;
      }, error => {
        throwError(error);
      });

      if (this.match.usernameReceiver === this.username) {
        this.opponent = this.match.usernameSender;
      } else {
        this.opponent = this.match.usernameReceiver;
      }
    }

  }

  clickCard(event, id: number): void {
    const notAdded = 'Pick card';
    const added = 'Remove card';

    const button = event.target;

    if (button.innerText === notAdded) {
      if (this.pickedCards.length < 3) {
        this.pickedCards.push(id);
        button.innerText = added;
      } else {
        this.toastr.warning('Only 3 cards allowed');
      }
    } else {
      const index = this.pickedCards.indexOf(id, 0);
      if (index > -1) {
        this.pickedCards.splice(index, 1);
      }
      button.innerText = notAdded;
    }
  }

  submitCards(): void {
    if (this.pickedCards.length !== 3) {
      this.toastr.warning('3 cards required!');
    } else {
      const array = [];
      for (const id of this.pickedCards) {
        // tslint:disable-next-line:new-parens
        array.push(new (class MyDTO implements SubmitCardModel {
          cardId = id;
        }));
      }

      this.fightService.submitCards(array).subscribe(data => {
          this.cardsPicked = true;
          this.toastr.success('Cards submitted.');
        }, error => {
          throwError(error);
          this.toastr.error('Card sending error!');
        }
      );
    }
    this.startFightIfAllPresent();
  }

  // Pozvano kada su poslane karte i igrac ima para
  startFightIfAllPresent(): void {

    if (this.pickedCards) {
      this.fightService.startFight(this.match.battleId).subscribe(data => {
        this.ready = data;
        if (this.ready) {
          setTimeout(() => {
            this.router.navigate(['battle/getWinner'], {state: {data: this.match.battleId}});
          }, 3000);
        }

      }, error => {
        throwError(error);
        this.toastr.error('Fight start error!');
      });
    }
  }

  seeMore(id: number): void {
    if (confirm('Are you sure you want leave battle page and see card details? Battle will be canceled!')) {
      this.router.navigate(['card/' + id]);
    }
  }

}
