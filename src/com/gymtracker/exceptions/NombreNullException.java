package com.gymtracker.exceptions;


//Crea la excepción para validar que el nombre no sea nulo 
public class NombreNullException extends GymTrackerException{

	public NombreNullException(String message) {
		super(message);
	}
	


}
