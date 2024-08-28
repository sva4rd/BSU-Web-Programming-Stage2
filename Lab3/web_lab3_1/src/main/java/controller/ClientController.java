package controller;

import java.util.ArrayList;
import java.util.List;

import connection.ConnectionPoolException;
import connection.ConnectionPool;
import model.*;
import modelDAO.CarDAO;
import modelDAO.ClientDAO;
import modelDAO.DAOException;

import static logger.JPALogger.logException;

public class ClientController {
	private Client client;
	
	public ClientController() {
		
	}
	
	public void setClient(String passportData, boolean isNew) throws ClientControllerException {
		try {
			if (isNew) {
				this.client = new Client(passportData, isNew);
			} else {
				this.client = ClientDAO.getClientById(passportData);
			}
		} catch (ConnectionPoolException | DAOException e) {
			logException(e);
			throw new ClientControllerException("Error: loading client failed");
		}
	}
	
	public List<Car> getClientCars() throws ClientControllerException {
		try {
			return this.client.getRentCars();
		} catch (ConnectionPoolException | DAOException e) {
			logException(e);
			throw new ClientControllerException("Error: the client doesn't have any rent cars");
		}
	}
	
	public boolean realeseClientCar(int carID) throws ClientControllerException {
		try {
			return this.client.releaseCar(carID);
		} catch (ConnectionPoolException | DAOException e) {
			logException(e);
			throw new ClientControllerException("Error: releasing car is failed");
		}
	}
	
	public List<Car> getAvailableCars() throws ClientControllerException{
		List<Car> list = Admin.getAvailableCars();
		if (list == null || list.isEmpty()) {
			ClientControllerException e = new 
					ClientControllerException("Error: no available cars");
			logException(e);
			throw e;
		}
		return list;
	}
	
	public void createClientRequest(int carID, int rentDays) throws ClientControllerException {
		if (carID <= 0) {
			ClientControllerException e = new 
					ClientControllerException("Error: carID must be greater than zero");
			logException(e);
			throw e;
		}
		try {
			Admin.processRequest(client.createRequest(CarDAO.getCarById(carID), rentDays));
		} catch (ConnectionPoolException | DAOException | IllegalArgumentException e) {
			logException(e);
			throw new ClientControllerException("Error: creating request failed");
		}
	}
	
	public List<Bill> getClientBills() throws ClientControllerException {
		try {
			return client.getBills();
		} catch (ConnectionPoolException | DAOException e) {
			logException(e);
			throw new ClientControllerException("Error: receiving bills failed");
		}
	}
	
	public List <String> payClientBill(int billID) throws ClientControllerException {
		List <String> results = new ArrayList<>();
		try {
			results = client.payBill(billID);
		} catch (ConnectionPoolException | DAOException e) {
			logException(e);
			throw new ClientControllerException("Error: paying bill failed");
		}
		return results;
	}

	public void close(){
		ConnectionPool.closeFactory();
	}
}
