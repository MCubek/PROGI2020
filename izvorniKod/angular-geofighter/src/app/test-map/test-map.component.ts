import {Component, OnInit} from '@angular/core';
import { latLng, tileLayer, Icon, icon, Marker } from 'leaflet';
import 'leaflet';
import 'leaflet-routing-machine';

declare let L;

@Component({
  selector: 'app-test-map',
  templateUrl: './test-map.component.html',
  styleUrls: ['./test-map.component.css']
})
export class TestMapComponent implements OnInit {

  options = {
    layers: [
      tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      })
    ],
    zoom: 13,
    center: latLng(45.8150, 15.9819)
  };

  // Override default Icons
  private defaultIcon: Icon = icon({
    iconUrl: 'assets/marker-icon.png',
    shadowUrl: 'assets/marker-shadow.png'
  });

  // tslint:disable-next-line:typedef
  ngOnInit() {
    Marker.prototype.options.icon = this.defaultIcon;
  }

  // tslint:disable-next-line:typedef
  onMapReady(map: L.Map) {
    L.Routing.control({
      waypoints: [L.latLng(57.74, 11.94), L.latLng(57.6792, 11.949)],
      routeWhileDragging: true
    }).addTo(map);
  }


}
