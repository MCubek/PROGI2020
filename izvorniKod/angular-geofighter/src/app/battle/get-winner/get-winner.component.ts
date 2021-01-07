import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../auth/shared/auth.service";
import {FightService} from "../fight.service";
import {interval, throwError} from "rxjs";
import {startWith, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-get-winner',
  templateUrl: './get-winner.component.html',
  styleUrls: ['./get-winner.component.css']
})
export class GetWinnerComponent implements OnInit {
  battleID: number;
  winner: string;
  result: string;

  constructor(private authService: AuthService, private fightService: FightService) { }

  ngOnInit(): void {
    this.battleID = history.state.data;
    console.log(this.battleID);
    interval(1000).pipe(startWith(0), switchMap(() => this.fightService.getWinner(this.battleID))
    ).subscribe(data => {
      this.winner = data;
      console.log(this.winner);
      if (this.winner == ''){
        this.result = 'The result of the game is a draw!';
      }
      else if (this.winner == this.authService.getUsername()){
        this.result = 'You won! Congratulations!';
      }
      else {
        this.result = 'You lost. Better luck next time!';
      }
    }
    //, error => {
    //  throwError(JSON.parse(JSON.stringify(error)));}
    );
  }

}
