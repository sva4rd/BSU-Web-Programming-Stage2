import { Component } from '@angular/core';
import {Car} from "../mok-cars-list";
import {Router} from "@angular/router";
import {CarsService} from "../cars.service";
import {from, Observable, of} from "rxjs";

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent {
  cars$: Observable<Car[]> = of([]);
  constructor(private carService: CarsService, private router: Router) { }

  ngOnInit() {
    this.cars$ = from( this.carService.getCars()) as unknown as Observable<Car[]>;
  }

  showCarDetails(car: Car): void {
    this.router.navigate(['/cars/car-details', car.id]);
  }
}
