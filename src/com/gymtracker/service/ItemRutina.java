package com.gymtracker.service;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.model.Ejercicio;



/**
 * Clase auxiliar, sin lógica de negocio, valida lo mínimos para no guardar nulos y las reglas de rango las aplica RutinaEjercicio al construirse 
 */
public class ItemRutina {
	
	private final Ejercicio ejercicio;
	private final int sets;
	private final int objetivo;
	/**
	 * 
	 * @param ejercicio
	 * @param sets
	 * @param objetivo
	 */
	public ItemRutina(Ejercicio ejercicio, int sets, int objetivo) {
		super();
		if(ejercicio == null) {
			
			throw new EjercicioInvalidoException("Debe ingresar un ejercicio. ");
		}
		this.ejercicio = ejercicio;
		this.sets = sets;
		this.objetivo = objetivo;
	}
	/**
	 * @return the ejercicio
	 */
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	/**
	 * @return the sets
	 */
	public int getSets() {
		return sets;
	}
	/**
	 * @return the objetivo
	 */
	public int getObjetivo() {
		return objetivo;
	}
	
	
	
	
	

}
