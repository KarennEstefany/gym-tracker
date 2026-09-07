package com.gymtracker.exceptions;


//Crea la excepción para validar que el nombre no sea nulo 
public class NombreNullException extends GymTrackerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NombreNullException(String message) {
		super(message);
	}
	


}
