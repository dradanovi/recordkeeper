package hr.java.web.radanovic.recordkeeper.exception;

public class UserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1836447632453750341L;
	
	public UserException(String message, Exception e) {
		super(message, e);
	}
	
	public UserException(String message) {
		super(message);
	}

}
