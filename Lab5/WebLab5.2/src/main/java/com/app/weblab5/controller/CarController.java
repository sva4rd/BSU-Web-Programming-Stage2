package com.app.weblab5.controller;

import com.app.weblab5.containers.CarData;
import com.app.weblab5.model.Car;
import com.app.weblab5.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@CrossOrigin(origins = "*")
public class CarController {
    @Autowired
    CarRepository carRep;

    @GetMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Car> getCars() {
        return carRep.getAllCars();
    }

    @PostMapping(value = "/cars_add")
    public void addCar(@RequestBody CarData data) {
        Car car = new Car(data.getModel(), data.getManufacturer(),
                data.getState(), true);
        carRep.save(car);
    }

    @PostMapping(value = "/cars_edit")
    public void editCar(@RequestBody CarData data) {
        carRep.updateCarById(data.getModel(), data.getManufacturer(),
                data.getState(), data.getId());
    }

    @DeleteMapping("/cars_del/{id}")
    public void deleteCar(@PathVariable int id) {
            carRep.deleteById((long) id);
    }
}
