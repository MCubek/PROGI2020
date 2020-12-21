import { TestBed } from '@angular/core/testing';

import { CartographerGuard } from './cartographer.guard';

describe('AdminGuard', () => {
  let guard: CartographerGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(CartographerGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
