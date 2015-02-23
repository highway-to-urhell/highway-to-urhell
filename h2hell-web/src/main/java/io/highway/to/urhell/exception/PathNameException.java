package io.highway.to.urhell.exception;


public class PathNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PathNameException() {
		super("PathName null or incorrect");
	}
}