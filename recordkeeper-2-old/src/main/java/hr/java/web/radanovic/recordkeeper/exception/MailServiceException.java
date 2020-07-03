package hr.java.web.radanovic.recordkeeper.exception;

public class MailServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2798629301502242197L;

	public MailServiceException(String message, Exception e) {
		super(message, e);
	}
	
	public MailServiceException(String message) {
		super(message);
	}
}
