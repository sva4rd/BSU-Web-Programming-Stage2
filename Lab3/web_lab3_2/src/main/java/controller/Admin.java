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
		} catch (ConnectionPoolException ignored) {
			// TODO Auto-generated catch block
		} catch (DAOException ignored) {
			// TODO Auto-generated catch block
		}
	}
	
	public static void updateAvailableCars() throws ConnectionPoolException, DAOException {
		availableCars = CarDAO.getAllAvailableCars();
	}

    public static Order processRequest(Request request) throws ConnectionPoolException, DAOException {
    	for (Car car : availableCars) {
            if (car.getID() == request.car.getID()) {
            	Order order = new Order(request);
            	setBill(request.getClient(), order);
            	return order;
            }
        }
        throw new IllegalArgumentException("No available cars for the requested model");
    }
    
    public static List<Car> getAvailableCars(){
    	return availableCars;
    }
    
    private static void setBill(Client client, Order order) throws ConnectionPoolException, DAOException {
    	@SuppressWarnings("unused")
		Bill bill = new Bill(order, false, client);
    }
}
