package com.gymtracker.model;


import java.time.LocalDate;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;

public class SesionEntrenamiento {
	private final int id;
	private LocalDate fecha; 
	private final int usuarioId; 
	private final int rutinaId;
	private EstadoSesion estado;
	
	
	//Falta el enum de estado
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 
	 */
	public SesionEntrenamiento(int id,  LocalDate fecha, int usuarioId, int rutinaId, EstadoSesion estado) {
		
		 if (fecha == null) {
	            throw new FechaInvalidaException("La fecha de la sesión no puede ser nula");
	        }
		this.id = id;
		this.usuarioId = usuarioId;
		this.fecha= fecha;
		this.rutinaId = rutinaId;
		this.estado  = EstadoSesion.EN_CURSO;
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
	
	public EstadoSesion getEstado() {
		
		return estado;
	}
	public void finalizarSesion() {
		if (this.estado == EstadoSesion.FINALIZADA) {
			throw new SesionYaFinalizadaException("La sesión ya se encuentra finalizada");
		}
		this.estado = EstadoSesion.FINALIZADA;
	}
	


}
