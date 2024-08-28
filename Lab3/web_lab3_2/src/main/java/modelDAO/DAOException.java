package modelDAO;

public class DAOException extends Exception {

	private static final long serialVersionUID = 3432116957146249062L;

	public DAOException(String s) {
        super(s);
    }

	public DAOException(Exception e) {
		super(e);
	}

}
