import {Component, OnInit} from '@angular/core';
import {throwError} from 'rxjs';
import {FightService} from './fight.service';
import {CardModel} from './card.model';
import {AuthService} from '../auth/shared/auth.service';
import {Router} from '@angular/router';
import {SendRequestPayload} from '../user/nearby-users/send-request-payload';
import {ToastrService} from 'ngx-toastr';

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

  cardsPicked = false;
  pickedCards: Array<bigint> = new Array<bigint>();

  constructor(private authService: AuthService, private router: Router, private fightService: FightService, private toastr: ToastrService) {
  }

  ngOnInit(): void {

    this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUsername();

    this.match = history.state.data;
    if(this.match == undefined){
      this.router.navigateByUrl('/nearbyUsers');
    }
    else {
      this.fightService.getUserCardList(this.username).subscribe(data => {
        this.userCards = data;
      }, error => {
        throwError(error);
      });

      //this.match = history.state.data;
      //console.log(this.match.usernameSender + ', ' + this.match.usernameReceiver + ' ' + this.match.battleId);
      if (this.match.usernameReceiver == this.username) {
        this.opponent = this.match.usernameSender;
      } else {
        this.opponent = this.match.usernameReceiver;
      }
    }

  }

  clickCard(event, id: bigint): void {
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
      this.fightService.submitCards(this.pickedCards).subscribe(data => {
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
    // TODO dodati provjeru za drugo igraca
    if (this.pickedCards) {
      this.fightService.startFight(this.match.battleId).subscribe(data => {
        // TODO redirect na stranicu gdje se ocekuju rezultati borbe

      }, error => {
        throwError(error);
        this.toastr.error('Fight start error!');
      });
    }
  }

}
