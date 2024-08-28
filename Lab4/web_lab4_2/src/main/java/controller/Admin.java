package controller;

import java.util.List;

import connection.ConnectionPoolException;
import model.Bill;
import model.Car;
import model.Client;
import model.Order;
import model.Request;
import modelDAO.CarDAO;
import modelDAO.DAOException;


public class Admin {
	
	private static List<Car> availableCars = null;
	
	static {
		try {
			updateAvailableCars();
		} catch (ConnectionPoolException | DAOException ignored) {
			// TODO Auto-generated catch block
		}
    }
	
	public static void updateAvailableCars() throws ConnectionPoolException, DAOException {
		try{
			availableCars = CarDAO.getAllAvailableCars();
		}catch (Exception ignored){}
	}

    public static int processRequest(Request request) throws ConnectionPoolException, DAOException {
		updateAvailableCars();
    	for (Car car : availableCars) {
            if (car.getID() == request.car.getID()) {
            	Order order = new Order(request);
            	Bill bill = setBill(request.getClient(), order);
            	return bill.getID();
            }
        }
        throw new IllegalArgumentException("No available cars for the requested model");
    }
    
    public static List<Car> getAvailableCars(){
    	return availableCars;
    }
    
    private static Bill setBill(Client client, Order order) throws ConnectionPoolException, DAOException {
		return new Bill(order, false, client);
    }
}
