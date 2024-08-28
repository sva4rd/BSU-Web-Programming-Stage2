package model;

import jakarta.persistence.*;

import connection.ConnectionPoolException;
import modelDAO.BillDAO;
import modelDAO.DAOException;

@Entity
@Table(name = "bill")
public class Bill {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@OneToOne
    @JoinColumn(name = "id_order")
	Order order;
	
	@ManyToOne
    @JoinColumn(name = "id_client")
	Client client;

	@Column(name = "is_paid", nullable = false)
	boolean is_paid;
	
	public Bill (int id, Order order, boolean isPaid, Client client) {
		this.id = id;
		this.order = order;
		this.is_paid = isPaid;
		this.client = client;
	}
	
	public Bill (Order order, boolean isPaid, Client client) throws ConnectionPoolException, DAOException {
		this.order = order;
		this.is_paid = isPaid;
		this.client = client;
		BillDAO.insertBill(this);
	}

	public Bill() {

	}
	
	public int getID() {
		return this.id;
	}
	
	public int getOrderID() {
		return this.order.getID();
	}
	
	public boolean checkPay() {
		return this.is_paid;
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
