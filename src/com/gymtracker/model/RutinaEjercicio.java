package com.gymtracker.model;

import java.io.Serializable;

import com.gymtracker.exceptions.NumeroSetInvalidoException;

import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.exceptions.OrdenInvalidoException;

public class RutinaEjercicio implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private final Rutina rutina;
	private final Ejercicio ejercicio;
	private int ordenEnRutina;
	private int setsObjetivo;
	private int objetivo;
	/**
	 * @param id
	 * @param rutinaId
	 * @param ejercicioId
	 * @param ordenEnRutina
	 * @param setsObjetivo
	 * @param objetivo
	 */
	public RutinaEjercicio(int id, Rutina rutina, Ejercicio ejercicio, int ordenEnRutina, int setsObjetivo,
			int objetivo) {
		this.id = id;
		this.rutina = verificarNoNulo(rutina,"la rutina" );
		this.ejercicio = verificarNoNulo(ejercicio,"el ejercicio" );
		setOrdenEnRutina(ordenEnRutina);
		setSetsObjetivo(setsObjetivo);
		setObjetivo(objetivo);
	}
	public int getId() {
		return id;
	}
	public Rutina getRutina() { 
		return rutina; }
	public Ejercicio getEjercicio() {
		return ejercicio; }

	public int getOrdenEnRutina() {
		return ordenEnRutina;
	}

	public void setOrdenEnRutina(int ordenEnRutina) {
		//verificarOrden(ordenEnRutina);
		if(ordenEnRutina <= 0 || ordenEnRutina >10) {
			throw new OrdenInvalidoException("El orden en la rutina no es valido");
		}
		this.ordenEnRutina = ordenEnRutina;
	}
	public int getSetsObjetivo() {
		return setsObjetivo;
	}
	public void setSetsObjetivo(int setsObjetivo) {
		//verificarRango(setsObjetivo, "sets objetivo");
		if (setsObjetivo <=0 || setsObjetivo>30) {
			throw new NumeroSetInvalidoException("Sets objetivo no validos");
		
		}
		this.setsObjetivo = setsObjetivo;
	}
	public int getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(int objetivo) {
		//verificarRango(repsObjetivo, "Repeticiones objetivo ");
		
		this.objetivo = ejercicio.verificarObjetivo(objetivo);
	}
	
	
	private static <T> T verificarNoNulo(T objeto, String campo) {
		if (objeto == null) {
			throw new ObjetoNuloException("El campo " + campo + " no puede ser nulo");
		}
		return objeto;
	}
	@Override
	public String toString() {
		return "RutinaEjercicio [id=" + id + ", rutina=" + rutina.getNombre()+ ", ejercicio=" + ejercicio.getNombre()
				+ ", ordenEnRutina=" + ordenEnRutina + ", setsObjetivo=" + setsObjetivo + ", objetivo="
				+ objetivo + "]";
	}
	
	

}
