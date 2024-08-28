package connection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jakarta.persistence.*;
import org.eclipse.persistence.jpa.PersistenceProvider;

import static logger.JPALogger.logException;


public class ConnectionPool {
    private static BlockingQueue<EntityManager> pool;
    private static final String PERSISTENCE_UNIT_NAME = "simpleFactory";
    private static EntityManagerFactory factory;
    private static final int POOL_SIZE = 10;

    static {
        try {
            pool = new LinkedBlockingQueue<>(POOL_SIZE);
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            
            for (int i = 0; i < POOL_SIZE; i++) 
                pool.put(factory.createEntityManager());
        } catch (InterruptedException e) {
        	RuntimeException ex = new RuntimeException(e);
            logException(ex);
            throw ex;
        }
    }

    public static EntityManager getConnection() throws ConnectionPoolException {
    	try {
            return pool.take();
        } catch (InterruptedException | ExceptionInInitializerError e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Error: no available connections");
        }
    }

    public static void releaseConnection(EntityManager manager) throws ConnectionPoolException {
        try {
        	manager.clear();
        	pool.put(manager);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConnectionPoolException("Error: releasing connection failed");
        }
    }

    public static void closeFactory() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
