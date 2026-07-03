import { TestBed } from '@angular/core/testing';

import { CreateJob } from './create-job';

describe('CreateJob', () => {
  let service: CreateJob;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreateJob);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
