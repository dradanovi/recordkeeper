package hr.java.web.radanovic.recordkeeper.exception;

public class JwtException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7034654599960382038L;

	public JwtException(String message, Exception e) {
		super(message, e);
	}

	public JwtException(String message) {
		super(message);
	}

}
