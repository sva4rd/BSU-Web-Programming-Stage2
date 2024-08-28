package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.*;


public class ConnectionPool {
    private static BlockingQueue<Connection> pool;
    private static final int POOL_SIZE = 10;
    
    private static Logger theLogger = LogManager.getLogger(ConnectionPool.class);

    static {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        String url = resource.getString("url");
        String driver = resource.getString("driver");
        String user = resource.getString("user");
        String pass = resource.getString("password");

        try {
            Class.forName(driver);
            pool = new LinkedBlockingQueue<>(POOL_SIZE);

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(url, user, pass);
                pool.put(connection);
            }
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
        	RuntimeException ex =  new RuntimeException(e);
            theLogger.error(ex, ex.getCause());
            throw ex;
        }
    }

    public static Connection getConnection() throws ConnectionPoolException {
    	try {
            return pool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Error: no available connections");
        }
    }

    public static void releaseConnection(Connection connection) throws ConnectionPoolException {
        try {
        	pool.put(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Error: releasing connection failed");
        }
    }
}

