package net.persei.dionysus.exceptions;

public class UnexpectedDataTypeException extends Exception {

	private String dataKey = null;
	
	public String getDataKey() {
		return dataKey;
	}

	public UnexpectedDataTypeException(String string) {
		dataKey = string;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3634084166179938086L;

}
