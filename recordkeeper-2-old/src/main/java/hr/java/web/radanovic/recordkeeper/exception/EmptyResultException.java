package hr.java.web.radanovic.recordkeeper.exception;

public class EmptyResultException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -601116690558045265L;
	
	public EmptyResultException(String message, Exception e) {
		super(message, e);
	}
	
	public EmptyResultException(String message) {
		super(message);
	}

}
