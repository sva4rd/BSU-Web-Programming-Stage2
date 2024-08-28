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
import model.Request;
import model.Request_;

import static logger.JPALogger.logException;


public class RequestDAO {
	public static List<Request> getAllRequests() throws ConnectionPoolException, DAOException {
    	List<Request> requests = new ArrayList<>();
        EntityManager em = null;
        try {
			em = ConnectionPool.getConnection();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Request> cq = cb.createQuery(Request.class);
			Root<Request> rootEntry = cq.from(Request.class);
			CriteriaQuery<Request> all = cq.select(rootEntry);
			TypedQuery<Request> allQuery = em.createQuery(all);
			requests = allQuery.getResultList();
            if (requests.isEmpty())
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
        return requests;
    }
    
    public static Request getRequestById(int requestId) throws ConnectionPoolException, DAOException {
    	Request request = null;
    	EntityManager em = null;
        try {
			em = ConnectionPool.getConnection();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Request> cq = cb.createQuery(Request.class);
			Root<Request> rootEntry = cq.from(Request.class);
			Predicate requestIdPredicate = cb.equal(rootEntry.get(Request_.id), requestId);
			CriteriaQuery<Request> requestQuery = cq.select(rootEntry).where(requestIdPredicate);
			TypedQuery<Request> query = em.createQuery(requestQuery);
			request = query.getSingleResult();
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
