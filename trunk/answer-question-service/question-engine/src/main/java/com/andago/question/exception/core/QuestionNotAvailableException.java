/**
 * 
 */
package com.andago.question.exception.core;

/**
 * @author eduardo.perrino
 *
 */
public class QuestionNotAvailableException extends Exception {
	
	private static final long serialVersionUID = 1091L;
	
	/**
	 * 
	 */
	public QuestionNotAvailableException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public QuestionNotAvailableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public QuestionNotAvailableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public QuestionNotAvailableException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
