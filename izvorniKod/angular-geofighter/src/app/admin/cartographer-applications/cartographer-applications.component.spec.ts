import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartographerApplicationsComponent } from './cartographer-applications.component';

describe('CartographerApplicationsComponent', () => {
  let component: CartographerApplicationsComponent;
  let fixture: ComponentFixture<CartographerApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CartographerApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CartographerApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
