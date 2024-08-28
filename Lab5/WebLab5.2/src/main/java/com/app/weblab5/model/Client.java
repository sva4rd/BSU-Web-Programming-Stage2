package com.app.weblab5.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "rent_client")
public class Client {
	@Id
	String passport_data;

	@Column(nullable = false)
	int id_user;

	public Client(String passport_data, int id_user) {
		this.passport_data = passport_data;
		this.id_user = id_user;
	}

	public Client() {

	}

	public String getPassportData() {
		return this.passport_data;
	}

	public int getUserID(){ return this.id_user; }

	
	@Override
	public String toString() {
	    return "Passport data: " + getPassportData();
	}
}
