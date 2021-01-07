import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetWinnerComponent } from './get-winner.component';

describe('GetWinnerComponent', () => {
  let component: GetWinnerComponent;
  let fixture: ComponentFixture<GetWinnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetWinnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GetWinnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
