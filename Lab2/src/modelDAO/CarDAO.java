package modelDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Car;

import org.apache.logging.log4j.*;

public class CarDAO{
	
	private static Logger theLogger = LogManager.getLogger(CarDAO.class);
	
	private static final String SELECT_ALL_CARS = "SELECT * FROM car";
    private static final String INSERT_CAR = "INSERT INTO car (model, manufacturer, state, available) "
    		+ "VALUES (?, ?, ?, ?)";
    private static final String SELECT_CAR_BY_ID = "SELECT * FROM car WHERE id = ?";
    private static final String SELECT_ALL_AVAILABLE_CARS = "SELECT * FROM car WHERE available = 1";
    private static final String RELEASE_CAR_BY_ID = "UPDATE car SET available = 1 WHERE id = ?";
    private static final String SELECT_CARS_FOR_CLIENT = "{call GetCarsForClient(?)}";
    
    public static List<Car> getAllCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CARS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving cars failed, no cars found");
        	}

            do {
            	int id = rs.getInt("id");
                String model = rs.getString("model");
                String manufacturer = rs.getString("manufacturer");
                String state = rs.getString("state");
                Boolean available = rs.getBoolean("available");
                cars.add(new Car(id, model, manufacturer, state, available));
            }while (rs.next());
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
            if (connection != null) {
                try {
                    ConnectionPool.releaseConnection(connection);
                } catch(ConnectionPoolException e) {
                	theLogger.error(e, e.getCause());
                }
            }
        }
        return cars;
    }
    
    public static List<Car> getAllAvailableCars() throws ConnectionPoolException, DAOException {
        List<Car> cars = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_AVAILABLE_CARS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving cars failed, no cars found");
        	}

           	do {
            	int id = rs.getInt("id");
                String model = rs.getString("model");
                String manufacturer = rs.getString("manufacturer");
                String state = rs.getString("state");
                Boolean available = rs.getBoolean("available");
                cars.add(new Car(id, model, manufacturer, state, available));
            }while (rs.next());
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
            if (connection != null) {
                try {
                    ConnectionPool.releaseConnection(connection);
                } catch(ConnectionPoolException e) {
                	theLogger.error(e, e.getCause());
                }
            }
        }
        return cars;
    }
    
    public static Car getCarById(int carId) throws ConnectionPoolException, DAOException {
    	Car car = null;
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CAR_BY_ID);
        	preparedStatement.setInt(1, carId);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (!resultSet.next()) {
        		throw new SQLException("Receiving car failed, no car found");
        	}

            do {
                int id = resultSet.getInt("id");
                String model = resultSet.getString("model");
                String manufacturer = resultSet.getString("manufacturer");
                String state = resultSet.getString("state");
                Boolean available = resultSet.getBoolean("available");

                car = new Car(id, model, manufacturer, state, available);
            } while (resultSet.next());
           } catch (SQLException e) {
        	   throw new DAOException(e);
           } finally {
               if (connection != null) {
                   try {
                       ConnectionPool.releaseConnection(connection);
                   } catch(ConnectionPoolException e) {
                	   theLogger.error(e, e.getCause());
                   }
               }
           }
        return car;
    }

    public static int insertCar(Car car) throws ConnectionPoolException, DAOException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CAR, 
        			Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, car.getModel());
            preparedStatement.setString(2, car.getManufacturer());
            preparedStatement.setString(3, car.getState());
            preparedStatement.setBoolean(4, car.checkAvailable());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Insert car failed, no rows affected");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating car failed, no ID obtained");
            }
        } catch (SQLException e) {
        	throw new DAOException(e);
        } finally {
            if (connection != null) {
                try {
                    ConnectionPool.releaseConnection(connection);
                } catch (ConnectionPoolException e) {
                	theLogger.error(e, e.getCause());
                }
            }
        }
    }
    
    public static List<Car> getCarsForClient(String clientID) throws ConnectionPoolException, 
    DAOException {
		Connection connection = null;
		List<Car> cars = new ArrayList<>();
		try {
			connection = ConnectionPool.getConnection();
			CallableStatement callableStatement  = connection.prepareCall(SELECT_CARS_FOR_CLIENT);
			callableStatement.setString(1, clientID);
			ResultSet rs = callableStatement.executeQuery();
			if (!rs.next()) {
        		throw new SQLException("Receiving cars failed, no cars found");
        	}
			do {
            	int id = rs.getInt("id");
                String model = rs.getString("model");
                String manufacturer = rs.getString("manufacturer");
                String state = rs.getString("state");
                Boolean available = rs.getBoolean("available");
                cars.add(new Car(id, model, manufacturer, state, available));
            } while (rs.next());
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
            if (connection != null) {
                try {
                    ConnectionPool.releaseConnection(connection);
                } catch(ConnectionPoolException e) {
                	theLogger.error(e, e.getCause());
                }
            }
        }
		return cars;
	}
    
    public static void releaseCarById(int carId) throws DAOException, ConnectionPoolException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_CAR_BY_ID);
        	preparedStatement.setInt(1, carId);

        	preparedStatement.executeUpdate();
           } catch (SQLException e) {
        	   throw new DAOException(e);
           } finally {
               if (connection != null) {
                   try {
                       ConnectionPool.releaseConnection(connection);
                   } catch(ConnectionPoolException e) {
                	   theLogger.error(e, e.getCause());
                   }
               }
           }
    }
}
