import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardApplicationsComponent } from './card-applications.component';

describe('CardApplicationsComponent', () => {
  let component: CardApplicationsComponent;
  let fixture: ComponentFixture<CardApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CardApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CardApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
