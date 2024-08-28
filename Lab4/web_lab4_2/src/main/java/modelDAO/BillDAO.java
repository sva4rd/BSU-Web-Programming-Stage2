package modelDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Bill;

import static logger.JPALogger.logException;


public class BillDAO {	    
    @SuppressWarnings("unchecked")
	public static List<Bill> getAllBills() throws DAOException, ConnectionPoolException {
        List<Bill> bills = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllBills");
        	bills = query.getResultList();
            if (bills.isEmpty())
        		throw new SQLException("Receiving bills failed, no bills found");
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
        return bills;
    }
    
    public static Bill getBillById(int billId) throws ConnectionPoolException, DAOException {
    	Bill bill = null;
    	EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getBillById");
        	query.setParameter("id_bill", billId);
        	bill = (Bill) query.getSingleResult();           
            if (bill == null)
        		throw new SQLException("Receiving bill failed, no bill found");
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
        return bill;
    }

    public static void insertBill(Bill bill) throws ConnectionPoolException, DAOException {
    	EntityManager em = null;
    	EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
        	transaction = em.getTransaction();
			transaction.begin();
        	em.persist(bill);
        	transaction.commit();
        } catch (Exception e) {
        	if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				throw new DAOException("Creating bill failed, no rows affected");
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
	public static List<Bill> getBillsByClientId(String clientID) throws ConnectionPoolException,
    DAOException {
        List<Bill> bills = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            Query query = em.createNamedQuery("getBillsByClientId");
            query.setParameter("id_client", clientID);
            bills = query.getResultList();
            if (bills.isEmpty())
        		throw new SQLException("Receiving bills failed, no bills found");
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
        return bills;
    }
}
