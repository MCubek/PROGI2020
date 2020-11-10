import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupCartographerComponent } from './signup-cartographer.component';

describe('SignupCartographerComponent', () => {
  let component: SignupCartographerComponent;
  let fixture: ComponentFixture<SignupCartographerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SignupCartographerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SignupCartographerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
