package hr.java.web.radanovic.recordkeeper.exception;

public class HoursException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7768215889936934319L;

	public HoursException(String message, Exception e) {
		super(message, e);
	}

	public HoursException(String message) {
		super(message);
	}

}
