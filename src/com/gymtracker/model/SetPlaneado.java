package com.gymtracker.model;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.NumeroSetInvalidoException;


public class SetPlaneado {
	private final int numeroSet;
	private final Ejercicio ejercicio;
	private final int objetivo;
	/**
	 * @param numeroSet
	 * @param ejercicio
	 * @param objetivo
	 */
	public SetPlaneado(int numeroSet, Ejercicio ejercicio, int objetivo) {
		this.numeroSet = verificarSet(numeroSet);
		this.ejercicio = verificarEjercicioNoNulo(ejercicio );
		this.objetivo = ejercicio.verificarObjetivo(objetivo);
	}
	public int getNumeroSet() {
		return numeroSet;
	}
	
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
		
	public int getObjetivo() {
		return objetivo;
	}
	
	private static Ejercicio verificarEjercicioNoNulo(Ejercicio e) {
		if (e == null) {
			throw new EjercicioInvalidoException("El ejercicio no puede ser nulo");
		}
		return e;
	}
	
	public int verificarSet(int setsObjetivo) {
		//verificarRango(setsObjetivo, "sets objetivo");
		if (setsObjetivo <=0 || setsObjetivo>30) {
			throw new NumeroSetInvalidoException("Sets objetivo no validos");
		
		}
		return setsObjetivo;
	}
	
	@Override
	public String toString() {
		return "SetPlaneado [numeroSet=" + numeroSet + ", ejercicio=" + ejercicio + ", objetivo=" + objetivo
				+ "]";
	}
	
}
