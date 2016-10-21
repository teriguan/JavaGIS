/**
 * Project: A00656333Gis
 * File: ApplicationException.java
 * Date: Jun 20, 2016
 * Time: 1:34:30 AM
 */

package a00656333.data;

/**
 * Exceptions for the application.
 * 
 * @author Teresa Guan, A00656333
 *
 */

@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	/**
	 * Default constructor. Overrides superclass constructor.
	 */
	public ApplicationException() {
		super();
	}

	/**
	 * Overrides superclass constructor.
	 * 
	 * @param message
	 *            The message about invalid user input.
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * Overrides superclass constructor.
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ApplicationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Overrides superclass constructor.
	 * 
	 * @param message
	 * @param cause
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Overrides superclass constructor.
	 * 
	 * @param cause
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}

}
