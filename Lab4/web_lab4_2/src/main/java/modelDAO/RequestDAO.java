package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Request;

import static logger.JPALogger.logException;


public class RequestDAO {    
    @SuppressWarnings("unchecked")
	public static List<Request> getAllRequests() throws ConnectionPoolException, DAOException {
    	List<Request> request = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllRequests");
        	request = query.getResultList();
            if (request.isEmpty())
        		throw new SQLException("Receiving requests failed, no requests found");
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
        return request;
    }
    
    public static Request getRequestById(int requestId) throws ConnectionPoolException, DAOException {
    	Request request = null;
    	EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getRequestById");
        	query.setParameter("requestId", requestId);
        	request = (Request) query.getSingleResult();           
            if (request == null)
        		throw new SQLException("Receiving request failed, no request found");
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
        return request;
    }

    public static void insertRequest(Request request) throws ConnectionPoolException, DAOException {
    	EntityManager em = null;
    	EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
        	transaction = em.getTransaction();
			transaction.begin();
        	em.persist(request);
        	transaction.commit();
        } catch (Exception e) {
        	if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				throw new DAOException("Creating request failed, no rows affected");
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
