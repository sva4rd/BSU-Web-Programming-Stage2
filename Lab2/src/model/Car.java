package model;

import connection.ConnectionPoolException;
import modelDAO.CarDAO;
import modelDAO.DAOException;

public class Car {
	int id;
	String model;
	String manufacturer;
	String state;
	Boolean available;

	public Car(String model, String manufacturer, String state, Boolean available) 
			throws ConnectionPoolException, DAOException {
		this.model = model;
		this.manufacturer = manufacturer;
		this.state = state;
		this.available = available;
		this.id = CarDAO.insertCar(this);
	}

	public Car(int id, String model, String manufacturer, String state, Boolean available) {
		this.id = id;
		this.model = model;
		this.manufacturer = manufacturer;
		this.state = state;
		this.available = available;
	}

	public String getModel() {
		return this.model;
	}

	public int getID() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean checkAvailable() {
		return this.available;
	}

	public void setAvailable(Boolean val) {
		this.available = val;
	}
	
	@Override
	public String toString() {
	    return "ID:\t\t" + getID() + "\n" +
	           "Model:\t\t" + getModel() + "\n" +
	           "Manufacturer:\t" + getManufacturer() + "\n" +
	           "State:\t\t" + getState() + "\n" +
	           "Available:\t" + checkAvailable();
	}
}
