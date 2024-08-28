package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.Order;
import model.Order_;

import static logger.JPALogger.logException;


public class OrderDAO {
    public static List<Order> getAllOrders() throws ConnectionPoolException, DAOException {
    	List<Order> orders = new ArrayList<>();
        EntityManager em = null;
        try {
			em = ConnectionPool.getConnection();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Order> cq = cb.createQuery(Order.class);
			Root<Order> rootEntry = cq.from(Order.class);
			CriteriaQuery<Order> all = cq.select(rootEntry);
			TypedQuery<Order> allQuery = em.createQuery(all);
			orders = allQuery.getResultList();
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
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Order> cq = cb.createQuery(Order.class);
			Root<Order> rootEntry = cq.from(Order.class);
			Predicate orderIdPredicate = cb.equal(rootEntry.get(Order_.id), orderId);
			CriteriaQuery<Order> orderQuery = cq.select(rootEntry).where(orderIdPredicate);
			TypedQuery<Order> query = em.createQuery(orderQuery);
			order = query.getSingleResult();
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
