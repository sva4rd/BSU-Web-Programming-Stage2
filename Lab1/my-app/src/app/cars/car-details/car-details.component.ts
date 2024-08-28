import {Component, OnInit} from '@angular/core';
import {CarsService} from "../cars.service";
import {Car} from "../mok-cars-list";
import {ActivatedRoute, ParamMap} from "@angular/router";
import {from, of, switchMap} from "rxjs";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent implements OnInit{
  car: Car | null = null;

  constructor(private carService: CarsService, private route: ActivatedRoute) { }

  async ngOnInit() {
    const carId = +this.route.snapshot.paramMap.get('id')!
    this.car = await this.carService.getCarData(carId);
  }
}
