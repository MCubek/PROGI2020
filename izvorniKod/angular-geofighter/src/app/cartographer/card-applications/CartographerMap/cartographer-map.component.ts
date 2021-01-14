import {AfterViewInit, Component, Input, OnInit} from '@angular/core';

import {icon} from 'leaflet';
import 'leaflet-routing-machine';
import 'leaflet.locatecontrol';

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';

declare const L;

@Component({
  selector: 'app-cartographer-map',
  templateUrl: './cartographer-map.component.html',
  styleUrls: ['./cartographer-map.component.css']
})
export class CartographerMapComponent implements OnInit, AfterViewInit {
  private map;

  @Input() coordinatesList: Coordinates[];

  ngOnInit(): void {
    this.map = L.map('map', {
      center: [45.8150, 15.9819],
      zoom: 13
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

    L.control.locate(
    ).addTo(this.map);
  }

  drawPath(): void {
    L.Routing.control({
      waypoints: this.getWaypoints(),
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

    this.centerMap();
    this.drawPath();
  }

  getWaypoints(): any {
    const list = [];
    this.coordinatesList.forEach(value => {
      list.push(L.latLng(value.latitude, value.longitude));
    });
    return list;
  }

  centerMap(): void {
    this.map.setView(L.latLng(this.coordinatesList[0].latitude, this.coordinatesList[0].longitude));
  }

}
