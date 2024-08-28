package model;

import jakarta.persistence.*;

import connection.ConnectionPoolException;
import modelDAO.DAOException;
import modelDAO.OrderDAO;

@Entity
@Table(name = "client_order")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@OneToOne
    @JoinColumn(name = "id_request")
	public Request request;

    public Order(Request request) throws ConnectionPoolException, DAOException {
        this.request = request;
        OrderDAO.insertOrder(this);
    }
    
    public Order(int id, Request request) {
    	this.id = id;
        this.request = request;
    }

	public Order() {

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
