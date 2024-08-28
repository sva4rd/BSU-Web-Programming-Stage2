import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CarCenterComponent} from "./cars/car-center/car-center.component";
import {CarDetailsComponent} from "./cars/car-details/car-details.component";

const routes: Routes = [
  { path: '', component: CarCenterComponent },
  { path: 'cars', redirectTo: '', pathMatch: 'full' },
  { path: 'cars/car-details/:id', component: CarDetailsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
