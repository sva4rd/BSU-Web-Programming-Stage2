package model;

import connection.ConnectionPoolException;
import modelDAO.DAOException;
import modelDAO.OrderDAO;

public class Order {
	int id;
	public Request request;

    public Order(Request request) throws ConnectionPoolException, DAOException {
        this.request = request;
        this.id = OrderDAO.insertOrder(this);
    }
    
    public Order(int id, Request request) {
    	this.id = id;
        this.request = request;
    }
    
    public int getID() {
    	return this.id;
    }
    
    public int getRequestID() {
    	return this.request.getID();
    }

    @Override
	public String toString() {
	    return "ID: " + getID() + "\n" +
	           "Request ID: " + getRequestID();
	}
}
