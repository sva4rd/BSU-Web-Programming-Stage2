package model;

import connection.ConnectionPoolException;
import modelDAO.BillDAO;
import modelDAO.DAOException;

public class Bill {
	int id;
	Order order;
	boolean isPaid;
	Client client;
	
	public Bill (int id, Order order, boolean isPaid, Client client) {
		this.id = id;
		this.order = order;
		this.isPaid = isPaid;
		this.client = client;
	}
	
	public Bill (Order order, boolean isPaid, Client client) throws ConnectionPoolException, DAOException {
		this.order = order;
		this.isPaid = isPaid;
		this.client = client;
		this.id = BillDAO.insertBill(this);
	}
	
	public void pay() {
        this.isPaid = true;
    }
	
	public int getID() {
		return this.id;
	}
	
	public int getOrderID() {
		return this.order.getID();
	}
	
	public boolean checkPay() {
		return this.isPaid;
	}
	
	public String getClientID() {
		return client.getPassportData();
	}
	
	@Override
	public String toString() {
	    return "ID: " + getID() + "\n" +
	           "Order ID: " + getOrderID() + "\n" +
	           "Client ID: " + getClientID() + "\n" +
	           "Is paid: " + checkPay();
	}
}
