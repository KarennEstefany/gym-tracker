package com.gymtracker.model;
import java.time.LocalDate;

public class SesionEntrenamiento {
	private final int id;
	private LocalDate fecha; 
	private final int usuarioId; 
	private final int rutinaId;
	
	
	//Falta el enum de estado
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 
	 */
	public SesionEntrenamiento(int id,  LocalDate fecha, int usuarioId, int rutinaId) {
		
		 if (fecha == null) {
	            throw new IllegalArgumentException("La fecha de la sesión no puede ser nula");
	        }
		this.id = id;
		this.usuarioId = usuarioId;
		this.fecha= fecha;
		this.rutinaId = rutinaId;
	}
	public int getId() {
		return id;
	}
	
	public int getUsuarioId() {
		return usuarioId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public int getRutinaId() {
		return rutinaId;
	}
	


}
