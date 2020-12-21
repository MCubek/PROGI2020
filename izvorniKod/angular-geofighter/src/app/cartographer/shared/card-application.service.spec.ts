import { TestBed } from '@angular/core/testing';

import { CardApplicationService } from './card-application.service';

describe('CardService', () => {
  let service: CardApplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardApplicationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
