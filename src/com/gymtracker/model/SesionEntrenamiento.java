package com.gymtracker.model;




import java.io.Serializable;
import java.time.LocalDate;

import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;

public class SesionEntrenamiento implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int id;
	private final LocalDate fecha; 
	private final Usuario usuario; 
	private final Rutina rutina;
	private EstadoSesion estado;
	
    
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 * @param rutinaId
	 * @param estado
	 
	 */
	public SesionEntrenamiento(int id,  LocalDate fecha, Usuario usuario, Rutina rutina) {
		
		
		if(usuario == null) {
			throw new UsuarioInvalidoException("La sesión debe estar asociada a un usuario");
		}
		if(rutina == null) {
			throw new RutinaInvalidaException("La sesión debe estar asociada a una rutina.");
		}
		
		
		
		
		//Aquí se verifica si la rutina pertenece al mismo usuario,
		// Se verifica mediante el documento del usuario  
	    if (!rutina.getUsuario().equals(usuario)) {
	        throw new UsuarioInvalidoException("La rutina no pertenece al usuario indicado");
	    } 
		
		 if (fecha == null) { 
	            throw new FechaInvalidaException("La fecha de la sesión no puede ser nula");
	        }
		this.id = id;
		this.usuario = usuario;
		this.fecha= fecha;
		this.rutina = rutina;
		this.estado  = EstadoSesion.EN_CURSO;
	}
	public int getId() {
		return this.id;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}

	public LocalDate getFecha() {
		return this.fecha;
	}

	public Rutina getRutina() {
		return this.rutina;
	}
	
	
	
	public EstadoSesion getEstado() {
		
		return this.estado;
	}
	// Este método permite controlar el estado de la sesión
	public void finalizarSesion() {
		if (this.estado == EstadoSesion.FINALIZADA) 
		{
			throw new SesionYaFinalizadaException("La sesión ya se encuentra finalizada");
		}
		this.estado = EstadoSesion.FINALIZADA;
	}
	

}
