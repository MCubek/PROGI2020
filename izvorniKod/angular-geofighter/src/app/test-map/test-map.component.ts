import {AfterViewInit, Component, OnInit} from '@angular/core';

import { icon } from 'leaflet';
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
import 'leaflet-routing-machine';

declare const L;

@Component({
  selector: 'app-test-map',
  templateUrl: './test-map.component.html',
  styleUrls: ['./test-map.component.css']
})
export class TestMapComponent implements OnInit, AfterViewInit {
  private map;

  ngOnInit(): void {
    this.map = L.map('map', {
      center: [45.8150, 15.9819],
      zoom: 13
    });


    const iconDefault = icon({
      iconRetinaUrl,
      iconUrl,
      shadowUrl,
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      tooltipAnchor: [16, -28],
      shadowSize: [41, 41]
    });

    L.Marker.prototype.options.icon = iconDefault;
  }

  drawPath(): void {
    L.Routing.control({
      waypoints: [L.latLng(45.8150, 15.9819), L.latLng(45.6150, 15.9819)],
      routeWhileDragging: false,
      showAlternatives: false,
      addWaypoints: false,
      draggableWaypoints: false,
      router: L.Routing.osrmv1({
        serviceUrl: 'https://router.project-osrm.org/route/v1'
      })
    }).addTo(this.map);
  }

  ngAfterViewInit(): void {
    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    this.drawPath();
  }

}
