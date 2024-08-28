package com.app.weblab5.controller;


import com.app.weblab5.model.Car;

import java.util.Comparator;

public class CarComparator implements Comparator<Car> {
	
	@Override
    public int compare(Car c1, Car c2) {
        return c1.getModel().compareTo(c2.getModel());
    }
}

class CarIdComparator implements Comparator<Car>{

	@Override
	public int compare(Car c1, Car c2) {
		return c1.getID() - c2.getID();
	}
	
}

class CarManufacturerComparator implements Comparator<Car>{

	@Override
	public int compare(Car c1, Car c2) {
		return c1.getManufacturer().compareTo(c2.getManufacturer());
	}
	
}
