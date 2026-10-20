package com.gymtracker.model;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;

public class SetPlaneado {
	private final int numeroSet;
	private final Ejercicio ejercicio;
	private final int repsObjetivo;
	/**
	 * @param numeroSet
	 * @param ejercicio
	 * @param repsObjetivo
	 */
	public SetPlaneado(int numeroSet, Ejercicio ejercicio, int repsObjetivo) {
		this.numeroSet = verificarSet(numeroSet);
		this.ejercicio = verificarEjercicioNoNulo(ejercicio );
		this.repsObjetivo = verificarRepsObjetivo(repsObjetivo);
	}
	public int getNumeroSet() {
		return numeroSet;
	}
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public int getRepsObjetivo() {
		return repsObjetivo;
	}
	private static Ejercicio verificarEjercicioNoNulo(Ejercicio e) {
		if (e == null) {
			throw new EjercicioInvalidoException("El ejercicio no puede ser nulo");
		}
		return e;
	}
	private static int verificarRepsObjetivo(int repsObjetivo) {
		//verificarRango(repsObjetivo, "Repeticiones objetivo ");
		if (repsObjetivo <=0 || repsObjetivo>30) {
			throw new RepeticionesInvalidasException ("Reps objetivo no validas ");
		
		}
		return repsObjetivo;
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
		return "SetPlaneado [numeroSet=" + numeroSet + ", ejercicio=" + ejercicio + ", repsObjetivo=" + repsObjetivo
				+ "]";
	}
	
}
