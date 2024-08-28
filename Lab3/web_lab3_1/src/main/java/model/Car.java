package model;

import jakarta.persistence.*;

import connection.ConnectionPoolException;
import modelDAO.CarDAO;
import modelDAO.DAOException;

@Entity
@Table(name = "car")
@NamedStoredProcedureQuery(
	    name = "GetCarsForClient",
	    procedureName = "GetCarsForClient",
	    parameters = {
	        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "clientPassportData")
	    },
	    resultClasses = Car.class
	)
@NamedQueries({
	@NamedQuery(
			name = "getAllCars",
			query = "SELECT c FROM Car c"
			),
	@NamedQuery(
			name = "getAllAvailableCars",
			query = "SELECT c FROM Car c WHERE c.available = true"
			),
	@NamedQuery(
			name = "getCarById",
			query = "SELECT c FROM Car c WHERE c.id = :carId"
			),
	@NamedQuery(
	        name = "releaseCarById",
	        query = "UPDATE Car c SET c.available = true WHERE c.id = :carId"
	        )
})
public class Car {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(nullable = false)
	String model;
	
	@Column(nullable = false)
	String manufacturer;
	
	@Column(nullable = false)
	String state;
	
	@Column(nullable = false)
	Boolean available;

	public Car(String model, String manufacturer, String state, Boolean available) 
			throws ConnectionPoolException, DAOException {
		this.model = model;
		this.manufacturer = manufacturer;
		this.state = state;
		this.available = available;
		CarDAO.insertCar(this);
	}

	public Car(int id, String model, String manufacturer, String state, Boolean available) {
		this.id = id;
		this.model = model;
		this.manufacturer = manufacturer;
		this.state = state;
		this.available = available;
	}

	public Car() {

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
	    return "ID:\t\t\t\t" + getID() + "\n" +
	           "Model:\t\t\t" + getModel() + "\n" +
	           "Manufacturer:\t" + getManufacturer() + "\n" +
	           "State:\t\t\t" + getState() + "\n" +
	           "Available:\t\t" + checkAvailable();
	}
}
