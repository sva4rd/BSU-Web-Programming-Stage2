import { Component } from '@angular/core';
import {CarsService} from "../cars.service";

@Component({
  selector: 'app-car-form',
  templateUrl: './car-form.component.html',
  styleUrls: ['./car-form.component.css']
})
export class CarFormComponent {
  car: any = {};
  selectedCarId: number | null = null;
  carId: number | null = null;
  carModel: string | null = null;
  carState: string | null = null;
  carPrice: string | null = null;
  constructor(private carService: CarsService) {};

  onSubmit() {
    this.carService.addCar(this.carModel!, this.carState!, this.carPrice!);
    this.carModel = this.carState = this.carPrice = null;
  }
  async onEdit() {
    let car;
    if (this.carId !== null) {
      car = await this.carService.getCarData(this.carId);
      if (car !== null) {
        this.carService.editCar(this.carId!, this.carModel!, this.carState!, this.carPrice!);
        this.carId = this.carModel = this.carState = this.carPrice = null;
      } else {
        alert('Error, there\'s no such car.');
      }
      this.car = {};
    }
  }

  async onDelete() {
    let car;
    if (this.selectedCarId !== null){
      car = await this.carService.getCarData(this.selectedCarId);
      if (this.selectedCarId !== null && car !== null) {
        if (confirm('Are you sure you want to delete this car?')) {
          this.carService.deleteCar(this.selectedCarId);
          this.selectedCarId = null;
        }
      }
      else {
        alert('Error, there\'s no such car.');
      }
    }else {
      alert('Error, there\'s no such car.');
    }
  }
}
