package net.persei.dionysus.exceptions;


public class WrongVersionException extends LoaderException {
	public final int version;
	
	public WrongVersionException(int version) {
		this.version = version;
	}

	private static final long serialVersionUID = -7423226308772215937L;

}
