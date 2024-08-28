package connection;

//import java.sql.SQLException;

public class ConnectionPoolException extends Exception {
	private static final long serialVersionUID = -7793461284823925521L;

	public ConnectionPoolException(String s) {
        super(s);
    }
}