package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Client;
import model.Client_;

import static logger.JPALogger.logException;


public class ClientDAO {
    public static List<Client> getAllClients() throws ConnectionPoolException, DAOException {
		List<Client> clients = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);
            Root<Client> rootEntry = cq.from(Client.class);
            CriteriaQuery<Client> all = cq.select(rootEntry);
            TypedQuery<Client> allQuery = em.createQuery(all);
            clients = allQuery.getResultList();
            if (clients.isEmpty())
        		throw new SQLException("Receiving clients failed, no clients found");
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
        return clients;
    }
    
    public static Client getClientById(String clientId) throws ConnectionPoolException, DAOException {
    	Client client = null;
    	EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Client> cq = cb.createQuery(Client.class);
            Root<Client> rootEntry = cq.from(Client.class);
            Predicate clientIdPredicate = cb.equal(rootEntry.get(Client_.passport_data), clientId);
            CriteriaQuery<Client> clientQuery = cq.select(rootEntry).where(clientIdPredicate);
            TypedQuery<Client> query = em.createQuery(clientQuery);
            client = query.getSingleResult();
            if (client == null)
        		throw new SQLException("Receiving client failed, no client found");
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
        return client;
    }

    public static void insertClient(Client client) throws ConnectionPoolException, DAOException {
    	EntityManager em = null;
    	EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
        	transaction = em.getTransaction();
			transaction.begin();
        	em.persist(client);
        	transaction.commit();
        } catch (Exception e) {
        	if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				throw new DAOException("Creating client failed, no rows affected");
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

	@SuppressWarnings("unchecked")
	public static List<String> payBill(int billID) throws ConnectionPoolException, DAOException {
		EntityManager em = null;
        List<String> results = new ArrayList<>();
        try {
            em = ConnectionPool.getConnection();
            StoredProcedureQuery query = em.createNamedStoredProcedureQuery("PayBill");
            query.setParameter("bill_id", billID);
            results = (List<String>) query.getResultList();
            if (results.isEmpty())
        		throw new SQLException("Paying bill failed");
        }catch (Exception e) {
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
        return results;
	}
	
}
