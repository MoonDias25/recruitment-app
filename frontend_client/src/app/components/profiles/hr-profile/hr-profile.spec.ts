import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HrProfile } from './hr-profile';

describe('HrProfile', () => {
  let component: HrProfile;
  let fixture: ComponentFixture<HrProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HrProfile],
    }).compileComponents();

    fixture = TestBed.createComponent(HrProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
