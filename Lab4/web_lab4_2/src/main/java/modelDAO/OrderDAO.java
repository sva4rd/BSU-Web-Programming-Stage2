package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Order;

import static logger.JPALogger.logException;


public class OrderDAO {
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrders() throws ConnectionPoolException, DAOException {
    	List<Order> orders = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllOrders");
        	orders = query.getResultList();
            if (orders.isEmpty())
        		throw new SQLException("Receiving orders failed, no orders found");
        } catch (Exception e) {
        	throw new DAOException(e);
        } finally {
        	if (em != null) {
                try {
                	ConnectionPool.releaseConnection(em);
                } catch(ConnectionPoolException e) {
                	logException(e);
                }
            }
        }
        return orders;
    }
    
    public static Order getOrderById(int orderId) throws ConnectionPoolException, DAOException {
    	Order order = null;
    	EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getOrderById");
        	query.setParameter("orderId", orderId);
        	order = (Order) query.getSingleResult();
            if (order == null)
        		throw new SQLException("Receiving order failed, no order found");
           } catch (Exception e) {
        	   throw new DAOException(e);
           } finally {
        	   if (em != null) {
                   try {
                   	ConnectionPool.releaseConnection(em);
                   } catch(ConnectionPoolException e) {
                   	logException(e);
                   }
               }
           }
        return order;
    }

    public static void insertOrder(Order order) throws ConnectionPoolException, DAOException {
    	EntityManager em = null;
    	EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
        	transaction = em.getTransaction();
			transaction.begin();
        	em.persist(order);
        	transaction.commit();
        } catch (Exception e) {
        	if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				throw new DAOException("Creating order failed, no rows affected");
			}
        	throw new DAOException(e);
        } finally {
        	if (em != null) {
                try {
                	ConnectionPool.releaseConnection(em);
                } catch(ConnectionPoolException e) {
                	logException(e);
                }
            }
        }
    }
}
