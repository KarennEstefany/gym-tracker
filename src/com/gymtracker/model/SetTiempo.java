package com.gymtracker.model;

import com.gymtracker.exceptions.DuracionInvalidaException;

public class SetTiempo extends SetRegistro{
	
	private int duracionSegundos;

	public SetTiempo(int id, SesionEntrenamiento sesion, Ejercicio ejercicio, int numeroSet, int rpe, int rir, int duracionSegundos) {
		super(id, sesion, ejercicio, numeroSet, rpe, rir);
		// TODO Auto-generated constructor stub
		
		setDuracionSegundos(duracionSegundos);
		 
	}
	
	
 
	/**
	 * @return the duracionSegundos
	 */
	public int getDuracionSegundos() {
		return duracionSegundos;
	}



	/**
	 * @param duracionSegundos the duracionSegundos to set
	 */
	public void setDuracionSegundos(int duracionSegundos) {
		
		if(duracionSegundos <= 0 ) {
			
			throw new DuracionInvalidaException("La duración debe ser mayor a 0 segundos");
		} 
		this.duracionSegundos = duracionSegundos;
		

	}
 

	@Override
	public double calcularVolumen() {
		// TODO Auto-generated method stub
		return duracionSegundos;
	}
	
	
	
	
	
	
	
	

	
	
}
