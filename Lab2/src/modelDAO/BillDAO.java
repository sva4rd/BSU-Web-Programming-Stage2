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
import model.Bill;
import org.apache.logging.log4j.*;

public class BillDAO {
	
	private static Logger theLogger = LogManager.getLogger(BillDAO.class);
	
	private static final String SELECT_ALL_BILLS = "SELECT * FROM bill";
    private static final String INSERT_BILL = "INSERT INTO bill (id_order, is_paid, id_client)"
    		+ " VALUES (?, ?, ?)";
    private static final String SELECT_BILL_BY_ID = "SELECT * FROM bill WHERE id = ?";
    private static final String SELECT_BILLS_BY_CLIENT_ID = 
    		"SELECT * FROM bill WHERE id_client = ? AND is_paid = 0";
    
    public static List<Bill> getAllBills() throws DAOException, ConnectionPoolException {
        List<Bill> bills = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BILLS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving bills failed, no bills found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_order = rs.getInt("id_order");
            	boolean isPaid = rs.getBoolean("is_paid");
            	String id_client = rs.getString("id_client");
            	bills.add(new Bill(id, OrderDAO.getOrderById(id_order), isPaid, 
            			ClientDAO.getClientById(id_client)));
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
        return bills;
    }
    
    public static Bill getBillById(int billId) throws ConnectionPoolException, DAOException {
    	Bill bill = null;
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BILL_BY_ID);
        	preparedStatement.setInt(1, billId);

            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving bill failed, no bill found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_order = rs.getInt("id_order");
            	boolean isPaid = rs.getBoolean("is_paid");
            	String id_client = rs.getString("id_client");
            	bill = new Bill(id, OrderDAO.getOrderById(id_order), isPaid, 
            			ClientDAO.getClientById(id_client));
            }while (rs.next());
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
        return bill;
    }

    public static int insertBill(Bill bill) throws ConnectionPoolException, DAOException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BILL,
        			Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, bill.getOrderID());
            preparedStatement.setBoolean(2, bill.checkPay());
            preparedStatement.setString(3, bill.getClientID());

            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Insert bill failed, no rows affected");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Insert bill failed, no ID obtained");
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
    
    public static List<Bill> getBillsByClientId(String clientID) throws ConnectionPoolException,
    DAOException {
        List<Bill> bills = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BILLS_BY_CLIENT_ID);
        	preparedStatement.setString(1, clientID);
        	ResultSet rs = preparedStatement.executeQuery();
        	
        	if (!rs.next()) {
        		throw new SQLException("Receiving bills failed, no bills found");
        	}

            do {
            	int id = rs.getInt("id");
            	int id_order = rs.getInt("id_order");
            	boolean isPaid = rs.getBoolean("is_paid");
            	String id_client = rs.getString("id_client");
            	bills.add(new Bill(id, OrderDAO.getOrderById(id_order), isPaid, 
            			ClientDAO.getClientById(id_client)));
            }while (rs.next());
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
        return bills;
    }
}
