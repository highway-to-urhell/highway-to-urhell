package io.highway.to.urhell.exception;

public class DateIncomingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DateIncomingException() {
		super("Date incoming from agent null or incorrect");
	}
}