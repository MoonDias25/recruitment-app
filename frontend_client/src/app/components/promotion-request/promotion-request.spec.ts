import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromotionRequest } from './promotion-request';

describe('PromotionRequest', () => {
  let component: PromotionRequest;
  let fixture: ComponentFixture<PromotionRequest>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromotionRequest],
    }).compileComponents();

    fixture = TestBed.createComponent(PromotionRequest);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
