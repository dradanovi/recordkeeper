package hr.java.web.radanovic.recordkeeper.exception;

public class FileException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 659804106095451985L;

	public FileException(String message, Exception e) {
		super(message, e);
	}
	
	public FileException(String message) {
		super(message);
	}
}
