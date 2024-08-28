import {Component, OnInit} from '@angular/core';
import {CarsService} from "../cars.service";
import {Car} from "../mok-cars-list";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent implements OnInit{
  car$: Car | undefined;

  constructor(private carService: CarsService, private route: ActivatedRoute) { }

  ngOnInit() {
    const carId = +this.route.snapshot.paramMap.get('id')!
    this.carService.getCarData(carId).subscribe(
      (cars) => {
        this.car$ = cars;
      }
    );
  }
}
