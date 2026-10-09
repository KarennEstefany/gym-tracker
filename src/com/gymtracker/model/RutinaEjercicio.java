package com.gymtracker.model;

import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;
import com.gymtracker.exceptions.OrdenInvalidoException;
public class RutinaEjercicio {
	private int id;
	private int rutinaId;
	private int ejercicioId;
	private int ordenEnRutina;
	private int setsObjetivo;
	private int repsObjetivo;
	/**
	 * @param id
	 * @param rutinaId
	 * @param ejercicioId
	 * @param ordenEnRutina
	 * @param setsObjetivo
	 * @param repsObjetivo
	 */
	public RutinaEjercicio(int id, int rutinaId, int ejercicioId, int ordenEnRutina, int setsObjetivo,
			int repsObjetivo) {
		this.id = id;
		this.rutinaId = rutinaId;
		this.ejercicioId = ejercicioId;
		setOrdenEnRutina(ordenEnRutina);
		setSetsObjetivo(setsObjetivo);
		setRepsObjetivo(repsObjetivo);
	}
	public int getId() {
		return id;
	}
	public int getRutinaId() {
		return rutinaId;
	}
	public int getEjercicioId() {
		return ejercicioId;
	}
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
	public int getRepsObjetivo() {
		return repsObjetivo;
	}
	public void setRepsObjetivo(int repsObjetivo) {
		//verificarRango(repsObjetivo, "Repeticiones objetivo ");
		if (repsObjetivo <=0 || repsObjetivo>30) {
			throw new RepeticionesInvalidasException ("Reps objetivo no validas ");
		
		}
		this.repsObjetivo = repsObjetivo;
	}
	
	@Override
	public String toString() {
		return "RutinaEjercicio [id=" + id + ", rutinaId=" + rutinaId + ", ejercicioId=" + ejercicioId
				+ ", ordenEnRutina=" + ordenEnRutina + ", setsObjetivo=" + setsObjetivo + ", repsObjetivo="
				+ repsObjetivo + "]";
	}
	

}
