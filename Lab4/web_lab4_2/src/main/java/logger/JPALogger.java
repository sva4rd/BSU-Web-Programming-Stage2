package logger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class JPALogger {
	private static final Logger logger = LogManager.getLogger(JPALogger.class);
	
	public static void logException(Exception exception) {
        logger.error(exception, exception.getCause());
	}
}
