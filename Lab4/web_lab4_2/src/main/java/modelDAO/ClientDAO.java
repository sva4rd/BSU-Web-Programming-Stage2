package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Client;

import static logger.JPALogger.logException;


public class ClientDAO {	
	@SuppressWarnings("unchecked")
	public static List<Client> getAllClients() throws ConnectionPoolException, DAOException {
		List<Client> clients = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllClients");
        	clients = query.getResultList();
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
        	Query query = em.createNamedQuery("getClientById");
        	query.setParameter("clientId", clientId);
        	client = (Client) query.getSingleResult();
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
	public static void payBill(int billID) throws ConnectionPoolException, DAOException {
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
    }
	
}
