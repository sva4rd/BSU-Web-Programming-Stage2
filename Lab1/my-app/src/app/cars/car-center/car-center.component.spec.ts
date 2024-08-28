import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarCenterComponent } from './car-center.component';

describe('CarCenterComponent', () => {
  let component: CarCenterComponent;
  let fixture: ComponentFixture<CarCenterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarCenterComponent]
    });
    fixture = TestBed.createComponent(CarCenterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
