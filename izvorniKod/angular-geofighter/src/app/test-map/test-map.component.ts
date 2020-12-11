import {AfterViewInit, Component, OnInit} from '@angular/core';

import * as L from 'leaflet';
import 'leaflet-routing-machine';

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
    }

    drawPath(): void {
      L.Routing.control({
        waypoints: [L.latLng(45.8150, 15.9819), L.latLng(45.6150, 15.9819)],
        routeWhileDragging: false,
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
