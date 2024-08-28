package model;

import connection.ConnectionPoolException;
import modelDAO.DAOException;
import modelDAO.RequestDAO;

public class Request {
	int id;
	Client client;
    public Car car;
    int rent_days;

    public Request(Client client, Car car, int rentalPeriod) throws ConnectionPoolException, DAOException {
        this.client = client;
        this.car = car;
        this.rent_days = rentalPeriod;
        this.id = RequestDAO.insertRequest(this);
    }
    
    public Request(int id, Client client, Car car, int rentalPeriod) {
    	this.id = id;
        this.client = client;
        this.car = car;
        this.rent_days = rentalPeriod;
    }
    
    public int getID() {
    	return this.id;
    }
    public String getClientID() {
    	return this.client.getPassportData();
    }
    public Client getClient() {
    	return this.client;
    }
    public int getCarID() {
    	return this.car.getID();
    }
    public int getRentDays() {
    	return this.rent_days;
    }
    
    @Override
	public String toString() {
	    return "ID: " + getID() + "\n" +
	           "Client ID: " + getClientID() + "\n" +
	           "Car ID: " + getCarID() + "\n" +
	           "Rent days: " + getRentDays();
	}
}
