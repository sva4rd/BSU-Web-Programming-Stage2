import { Component } from '@angular/core';
import {Car} from "../mok-cars-list";
import {Router} from "@angular/router";
import {CarsService} from "../cars.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent {
  cars$: Car[] | undefined;
  constructor(private carService: CarsService, private router: Router) { }

  ngOnInit() {
    this.carService.getCars().subscribe(
      (cars) => {
        this.cars$ = cars;
      }
    );
  }

  showCarDetails(car: Car): void {
    this.router.navigate(['/cars/car-details', car.id]);
  }
}
