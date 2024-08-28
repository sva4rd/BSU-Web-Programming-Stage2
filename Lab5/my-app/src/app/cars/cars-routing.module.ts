import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule,Routes,Resolve } from "@angular/router";
import { CarCenterComponent } from "src/app/cars/car-center/car-center.component";


const carCenterRouters: Routes = [ 
  {path: '', component: CarCenterComponent},
];

/*@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export class CarsRoutingModule { }*/

@NgModule({
 imports: [RouterModule.forChild(carCenterRouters)], 
 exports : [RouterModule] 
}) 
export class CarsRoutingModule { }