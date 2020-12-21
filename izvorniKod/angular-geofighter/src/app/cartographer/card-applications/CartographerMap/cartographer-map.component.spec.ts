import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartographerMapComponent } from './cartographer-map.component';

describe('CartographerMapComponent', () => {
  let component: CartographerMapComponent;
  let fixture: ComponentFixture<CartographerMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CartographerMapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CartographerMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
