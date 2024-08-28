import { Injectable } from '@angular/core';
import {Car, CARS} from "./mok-cars-list";
import {catchError, map, Observable, tap, throwError} from "rxjs";
import { HttpClient, HttpHeaders } from '@angular/common/http';

export interface CarData {
  id: number;
  model: string;
  manufacturer: string;
  state: string;
}

@Injectable({
  providedIn: 'root'
})
export class CarsService {

  url = 'http://localhost:8082/CarRentalApp/cars';
  add_url = 'http://localhost:8082/CarRentalApp/cars_add';
  del_url = 'http://localhost:8082/CarRentalApp/cars_del';
  edit_url = 'http://localhost:8082/CarRentalApp/cars_edit';
  username = 'Admin';
  password = '$2a$10$i4aOwi/X/lJsBBhKZYZbreRv7K90ehHE7QLW50vMpM3Jc78DgFo8q';

  headers = new HttpHeaders({
     Authorization: 'Basic ' + btoa(this.username + ':' + this.password),
    'Content-Type': 'application/json'
  });

  constructor(private httpClient: HttpClient) { }

  getCars(): Observable<Car[]> {
    return this.httpClient.get<Car[]>(this.url, { headers: this.headers })
      .pipe(
        tap((cars: Car[]) => console.log(cars)),
        catchError((error: any) => {
          console.error(error);
          return throwError(error);
        })
      );
  }

  getCarData(id: number) {
    return this.getCars().pipe(
      map((cars: Car[]) => cars.find(car => car.id === id))
    );
  }

  addCar(model: string, state: string, manufacturer: string) {
    const carData: CarData = {
      id: 0,
      model: model,
      manufacturer: manufacturer,
      state: state
    };
    console.log('Sending request to:', this.add_url);
    console.log('Request body:', carData);
    return this.httpClient.post<any>(this.add_url, carData, {headers: this.headers,}).subscribe(
      response => console.log('Response:', response),
      error => console.error('Error:', error)
    );
  }

  editCar(id: number, model: string, state: string, manufacturer: string) {
    const carData: CarData = {
      id: id,
      model: model,
      manufacturer: manufacturer,
      state: state
    };
    console.log('Sending request to:', this.edit_url);
    console.log('Request body:', carData);
    return this.httpClient.post<any>(this.edit_url, carData, {headers: this.headers,}).subscribe(
      response => console.log('Response:', response),
      error => console.error('Error:', error)
    );
  }

  deleteCar(id: number) {
    console.log('Sending request to:', `${this.del_url}/${id}`);
    console.log('Request body:', id);
    return this.httpClient.delete(`${this.del_url}/${id}`, { headers: this.headers }).subscribe(
      response => console.log('Response:', response),
      error => console.error('Error:', error)
    );

  }

  private generateUniqueId(): number {
    const maxId = Math.max(...CARS.map((car) => car.id), 0);
    return maxId + 1;
  }

}
