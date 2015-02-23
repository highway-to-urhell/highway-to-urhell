package io.highway.to.urhell.exception;

public class TokenException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TokenException() {
		super("Token null or incorrect");
	}
}
