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
import model.Order;

import org.apache.logging.log4j.*;

public class OrderDAO {
	
	private static Logger theLogger = LogManager.getLogger(OrderDAO.class);
	
	private static final String SELECT_ALL_ORDERS = "SELECT * FROM `order`";
    private static final String INSERT_ORDER = "INSERT INTO `order` (id_request) VALUES (?)";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM `order` WHERE id = ?";
    
    public static List<Order> getAllOrders() throws ConnectionPoolException, DAOException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ORDERS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving orders failed, no orders found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_request = rs.getInt("id_request");
                orders.add(new Order(id, RequestDAO.getRequestById(id_request)));
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
        return orders;
    }
    
    public static Order getOrderById(int carId) throws ConnectionPoolException, DAOException {
    	Order order = null;
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ORDER_BY_ID);
        	preparedStatement.setInt(1, carId);

            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving order failed, no order found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_request = rs.getInt("id_request");

                order = new Order(id, RequestDAO.getRequestById(id_request));
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
        return order;
    }

    public static int insertOrder(Order order) throws ConnectionPoolException, DAOException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER,
        			Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getRequestID());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Insert order failed, no rows affected");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating order failed, no ID obtained.");
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
