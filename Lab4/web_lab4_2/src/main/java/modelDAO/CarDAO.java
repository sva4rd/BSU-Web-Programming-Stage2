package modelDAO;

import java.sql.SQLException;
import java.util.*;

import jakarta.persistence.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Car;

import static logger.JPALogger.logException;


public class CarDAO{	
    @SuppressWarnings("unchecked")
	public static List<Car> getAllCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllCars");
            cars = query.getResultList();
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
    
    @SuppressWarnings("unchecked")
	public static List<Car> getAllAvailableCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        EntityManager em = null;
        try {
        	em = ConnectionPool.getConnection();
        	Query query = em.createNamedQuery("getAllAvailableCars");
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
        	Query query = em.createNamedQuery("getCarById");
        	query.setParameter("carId", carId);
            car = (Car) query.getSingleResult();           
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
