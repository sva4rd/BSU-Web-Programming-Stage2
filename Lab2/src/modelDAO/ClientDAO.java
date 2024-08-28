package modelDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import model.Client;

import org.apache.logging.log4j.*;

public class ClientDAO {
	
	private static Logger theLogger = LogManager.getLogger(ClientDAO.class);

	private static final String SELECT_ALL_CLIENTS = "SELECT * FROM client";
	private static final String INSERT_CLIENT = "INSERT INTO client (passport_data) VALUES (?)";
	private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM client WHERE passport_data = ?";
	private static final String PAY_BILL_BY_ID = "{call PayBill(?)}";
	
	public static List<Client> getAllClients() throws ConnectionPoolException, DAOException {
        List<Client> clients = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (!rs.next()) {
        		throw new SQLException("Receiving clients failed, no clients found");
        	}

            do {
                String passportData = rs.getString("passport_data");
                clients.add(new Client(passportData, false));
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
        return clients;
    }
    
    public static Client getClientById(String clientId) throws ConnectionPoolException, DAOException {
    	Client client = null;
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT_BY_ID);
        	preparedStatement.setString(1, clientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (!resultSet.next()) {
        		throw new SQLException("Receiving client failed, no client found");
        	}

            do {
            	String passportData = resultSet.getString("passport_data");
            	client = new Client(passportData, false);
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
        return client;
    }

    public static void insertClient(Client client) throws ConnectionPoolException, DAOException {
    	Connection connection = null;
        try {
        	connection = ConnectionPool.getConnection();
        	PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT);
        	if (getClientById(client.getPassportData()) != null){
        		return;
        	}
            preparedStatement.setString(1, client.getPassportData());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Insert client failed, no rows affected");
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
	
	public static void payBill(int id) throws ConnectionPoolException, DAOException {
		Connection connection = null;
		try {
			connection = ConnectionPool.getConnection();
			CallableStatement callableStatement  = connection.prepareCall(PAY_BILL_BY_ID);
			callableStatement.setInt(1, id);
			ResultSet rs = callableStatement.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1));
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
