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
  carManufacturer: string | null = null;
  constructor(private carService: CarsService) {};

  onSubmit() {
    this.carService.addCar(this.carModel!, this.carState!, this.carManufacturer!);
    this.carModel = this.carState = this.carManufacturer = null;
  }
  onEdit() {
    if (this.carId !== null && this.carService.getCarData(this.carId!)) {

      this.carService.editCar(this.carId!, this.carModel!, this.carState!, this.carManufacturer!);
      this.carId = this.carModel = this.carState = this.carManufacturer = null;
    } else {
      alert('Wrong, there\'s no such car.');
    }
    this.car = {};
  }

  onDelete() {
    if (this.selectedCarId !== null && this.carService.getCarData(this.selectedCarId)) {
      if (confirm('Are you sure you want to delete this car?')) {
        this.carService.deleteCar(this.selectedCarId);
        this.selectedCarId = null;
      }
    } else {
      alert('Wrong, there\'s no such car.');
    }
  }
}
