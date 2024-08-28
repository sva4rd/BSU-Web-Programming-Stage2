package controller;

public class ClientControllerException extends Exception {

	private static final long serialVersionUID = -6233673671688977757L;

	public ClientControllerException(String s) {
        super(s);
    }
	
	public ClientControllerException(Exception e) {
        super(e);
    }
}
