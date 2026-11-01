package com.gymtracker.model;

import com.gymtracker.exceptions.PesoInvalidoException;

import com.gymtracker.exceptions.RepeticionesInvalidasException;

public class SetPeso extends SetRegistro{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double peso;
	private int repeticiones;
	

	
	public SetPeso(int id, SesionEntrenamiento sesion, Ejercicio ejercicio, int numeroSet, int rpe, int rir, double peso, int repeticiones ) {
		super(id, sesion, ejercicio, numeroSet, rpe, rir);
		
		
		// TODO Auto-generated constructor stub
		
		setPeso(peso);
		setRepeticiones(repeticiones);
	}

	
	

	/**
	 * @return the peso
	 */
	public double getPeso() {
		
		return peso;
	}




	/**
	 * @param peso the peso to set
	 */
	public void setPeso(double peso) {
		
		if(peso < 0) {
			throw new PesoInvalidoException("El peso no puede ser negativo");
		}
		this.peso = peso;
	}




	/**
	 * @return the repeticiones
	 */
	public int getRepeticiones() {
		return repeticiones;
	}




	/**
	 * @param repeticiones the repeticiones to set
	 */
	public void setRepeticiones(int repeticiones) {
		
		if(repeticiones <= 0) {
			throw new RepeticionesInvalidasException("Las repeticiones deben ser mayores a 0");
		}
	
		this.repeticiones = repeticiones;
	}




	@Override
	public double calcularVolumen() {
		// TODO Auto-generated method stub
		
		return peso * repeticiones;
		
		
	}
	
	

}
