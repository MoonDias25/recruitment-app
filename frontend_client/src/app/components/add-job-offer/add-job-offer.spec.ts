import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddJobOffer } from './add-job-offer';

describe('AddJobOffer', () => {
  let component: AddJobOffer;
  let fixture: ComponentFixture<AddJobOffer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddJobOffer],
    }).compileComponents();

    fixture = TestBed.createComponent(AddJobOffer);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
