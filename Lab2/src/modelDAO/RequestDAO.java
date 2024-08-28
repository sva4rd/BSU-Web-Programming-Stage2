package modelDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Request;

import org.apache.logging.log4j.*;

public class RequestDAO {
	
	private static Logger theLogger = LogManager.getLogger(RequestDAO.class);
	
	private static final String SELECT_ALL_REQUESTS = "SELECT * FROM request";
    private static final String INSERT_REQUEST = "INSERT INTO request (id_car, id_client, rent_days)"
    		+ " VALUES (?, ?, ?)";
    private static final String SELECT_REQUEST_BY_ID = "SELECT * FROM request WHERE id = ?";
    
    public static List<Request> getAllRequests() throws ConnectionPoolException, DAOException {
        List<Request> requets = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_REQUESTS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving requests failed, no requests found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_car = rs.getInt("id_car");
                String id_client = rs.getString("id_client");
                int rent_days = rs.getInt("rent_days");
                requets.add(new Request(id, ClientDAO.getClientById(id_client), 
                		CarDAO.getCarById(id_car), rent_days));
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
        return requets;
    }
    
    public static Request getRequestById(int requestId) throws ConnectionPoolException, DAOException {
    	Request request = null;
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REQUEST_BY_ID);
        	preparedStatement.setInt(1, requestId);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (!resultSet.next()) {
        		throw new SQLException("Receiving request failed, no request found");
        	}

            do {
            	int id = resultSet.getInt("id");
            	int id_car = resultSet.getInt("id_car");
                String id_client = resultSet.getString("id_client");
                int rent_days = resultSet.getInt("rent_days");

                request = new Request(id, ClientDAO.getClientById(id_client), 
                		CarDAO.getCarById(id_car), rent_days);
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
        return request;
    }

    public static int insertRequest(Request request) throws ConnectionPoolException, DAOException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REQUEST,
        			Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, request.getCarID());
            preparedStatement.setString(2, request.getClientID());
            preparedStatement.setInt(3, request.getRentDays());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Insert request failed, no rows affected");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating request failed, no ID obtained.");
            }
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
