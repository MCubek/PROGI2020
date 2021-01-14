import {AfterViewInit, Component, OnInit} from '@angular/core';
import {NearbyCardModel} from './nearby-card.model';
import {CardService} from '../shared/card.service';
import {ToastrService} from 'ngx-toastr';
import {throwError} from 'rxjs';

import * as L from 'leaflet';
import {icon} from 'leaflet';
import {UserLocationPayload} from '../../auth/login/user-location.payload';
import {AuthService} from '../../auth/shared/auth.service';

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';


// noinspection DuplicatedCode
@Component({
  selector: 'app-nearby',
  templateUrl: './nearby.component.html',
  styleUrls: ['./nearby.component.css']
})
export class NearbyComponent implements OnInit, AfterViewInit {
  nearbyCards: Array<NearbyCardModel>;

  private map;

  constructor(private cardService: CardService,
              private toastr: ToastrService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.setLocation();

    this.map = L.map('map', {
      center: [0, 0],
      zoom: 13
    });

    this.cardService.getNearbyCards().subscribe(data => {
      this.nearbyCards = data;
      this.addMarkers();
    }, error => {
      throwError(error);
    });

    L.Marker.prototype.options.icon = icon({
      iconRetinaUrl,
      iconUrl,
      shadowUrl,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      tooltipAnchor: [16, -28],
      shadowSize: [41, 41]
    });
  }

  ngAfterViewInit(): void {
    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    this.centerMap();
  }

  centerMap(): void {
    const location = this.cardService.getUserPosition();
    this.map.setView(L.latLng(location.latitude, location.longitude));

    const circle = L.circle([location.latitude, location.longitude], {
      color: 'blue',
      fillColor: '#5163d1',
      fillOpacity: 0.2,
      radius: 3000
    }).addTo(this.map);
  }

  collect(id: string): void {
    if (confirm('Are you sure you want to collect this card?')) {
      this.cardService.collectCard(id).subscribe(data => {
        window.location.reload();
      }, error => {
        throwError(error);
        this.toastr.error('Card could not be added!');
      });
    }
  }

  addMarkers(): void {
    if (this.nearbyCards !== undefined) {
      for (const card of this.nearbyCards) {
        if (card.location === undefined) {
          continue;
        }
        const marker = L.marker([card.latitude, card.longitude]).addTo(this.map);
        marker.bindPopup('<b>' + card.name + ':</b><br>' + 'uncommonness=' + card.uncommonness + '</b><br>' +
          'population=' + card.population + '</b><br>' +
          'difficulty=' + card.difficulty + '</b><br>');
      }
    }
  }

  setLocation(): void {
    let userLocationPayload: UserLocationPayload;
    navigator.geolocation.getCurrentPosition(pos => {
      userLocationPayload = {latitude: pos.coords.latitude, longitude: pos.coords.longitude};

      this.authService.saveLocation(userLocationPayload).subscribe(
        response => {
        },
        err => throwError(err));
    }, err => {
      this.toastr.error('Location not gathered!');
    });
  }
}
