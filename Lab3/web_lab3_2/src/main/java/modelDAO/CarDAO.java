package modelDAO;

import java.sql.SQLException;
import java.util.*;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import model.Car;
import model.Car_;

import static logger.JPALogger.logException;


public class CarDAO{	

	public static List<Car> getAllCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> rootEntry = cq.from(Car.class);
            CriteriaQuery<Car> all = cq.select(rootEntry);
            TypedQuery<Car> allQuery = em.createQuery(all);
            cars = allQuery.getResultList();
            if (cars.isEmpty())
        		throw new SQLException("Receiving cars failed, no cars found");
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
        return cars;
    }
    
    public static List<Car> getAllAvailableCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> rootEntry = cq.from(Car.class);
            Predicate availableCarIdPredicate = cb.equal(rootEntry.get(Car_.available), true);
            CriteriaQuery<Car> carQuery = cq.select(rootEntry).where(availableCarIdPredicate);
            TypedQuery<Car> query = em.createQuery(carQuery);
            cars = query.getResultList();
            if (cars.isEmpty())
        		throw new SQLException("Receiving cars failed, no cars found");
        } catch (SQLException e) {
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
        return cars;
    }
    
    public static Car getCarById(int carId) throws ConnectionPoolException, DAOException {
    	Car car = null;
    	EntityManager em = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> rootEntry = cq.from(Car.class);
            Predicate carIdPredicate = cb.equal(rootEntry.get(Car_.id), carId);
            CriteriaQuery<Car> carQuery = cq.select(rootEntry).where(carIdPredicate);
            TypedQuery<Car> query = em.createQuery(carQuery);
            car = query.getSingleResult();
            if (car == null)
        		throw new SQLException("Receiving car failed, no car found");
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
        return car;
    }

    public static void insertCar(Car car) throws ConnectionPoolException, DAOException {
    	EntityManager em = null;
    	EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
        	transaction = em.getTransaction();
			transaction.begin();
        	em.persist(car);
        	transaction.commit();
        } catch (Exception e) {
        	if (transaction != null && transaction.isActive()) {
				transaction.rollback();
				throw new DAOException("Creating car failed, no rows affected");
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
	public static List<Car> getCarsForClient(String clientID) throws ConnectionPoolException, 
    DAOException {
    	EntityManager em = null;
        List<Car> cars = new ArrayList<>();
        try {
            em = ConnectionPool.getConnection();
            StoredProcedureQuery query = em.createNamedStoredProcedureQuery("GetCarsForClient");
            query.setParameter("clientPassportData", clientID);
            cars = query.getResultList();
            if (cars.isEmpty())
        		throw new SQLException("Receiving cars failed, no cars found");
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
        return cars;
    }
    
    public static void releaseCarById(int carId) throws DAOException, ConnectionPoolException {
    	EntityManager em = null;
        EntityTransaction transaction = null;
        try {
        	em = ConnectionPool.getConnection();
            transaction = em.getTransaction();
            transaction.begin();
        	Query query = em.createNamedQuery("releaseCarById");
        	query.setParameter("carId", carId);
        	query.executeUpdate();
            transaction.commit();
           } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
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
