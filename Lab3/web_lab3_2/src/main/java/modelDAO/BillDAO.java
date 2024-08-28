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
import model.Bill;
import model.Bill_;
import model.Client_;

import static logger.JPALogger.logException;


public class BillDAO {	    
    public static List<Bill> getAllBills() throws DAOException, ConnectionPoolException {
        List<Bill> bills = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
            Root<Bill> rootEntry = cq.from(Bill.class);
            CriteriaQuery<Bill> all = cq.select(rootEntry);
            TypedQuery<Bill> allQuery = em.createQuery(all);
            bills = allQuery.getResultList();
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
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
            Root<Bill> rootEntry = cq.from(Bill.class);
            Predicate billIdPredicate = cb.equal(rootEntry.get(Bill_.id), billId);
            CriteriaQuery<Bill> billQuery = cq.select(rootEntry).where(billIdPredicate);
            TypedQuery<Bill> query = em.createQuery(billQuery);
            bill = query.getSingleResult();
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
    
    public static List<Bill> getBillsByClientId(String clientID) throws ConnectionPoolException,
    DAOException {
        List<Bill> bills = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Bill> cq = cb.createQuery(Bill.class);
            Root<Bill> rootEntry = cq.from(Bill.class);
            Predicate clientIdPredicate = cb.equal(rootEntry.get(Bill_.client).get(Client_.passport_data), clientID);
            Predicate isPaidPredicate = cb.equal(rootEntry.get(Bill_.is_paid), false);
            CriteriaQuery<Bill> billQuery = cq.select(rootEntry).where(cb.and(clientIdPredicate, isPaidPredicate));
            TypedQuery<Bill> query = em.createQuery(billQuery);
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
