package com.app.weblab5.model;

import jakarta.persistence.*;

@Entity
@Table(name = "request")
public class Request {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

    @OneToOne
    @JoinColumn(name = "id_car")
    public Car car;
	
	@OneToOne
    @JoinColumn(name = "id_client")
	Client client;
	
	@Column(nullable = false)
    int rent_days;

    public Request(Client client, Car car, int rentalPeriod) {
        this.client = client;
        this.car = car;
        this.rent_days = rentalPeriod;
    }
    
    public Request(int id, Client client, Car car, int rentalPeriod) {
    	this.id = id;
        this.client = client;
        this.car = car;
        this.rent_days = rentalPeriod;
    }

    public Request() {

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
