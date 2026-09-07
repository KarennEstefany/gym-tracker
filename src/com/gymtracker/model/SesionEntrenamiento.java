package com.gymtracker.model;




import java.time.LocalDate;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;

public class SesionEntrenamiento {
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
	public SesionEntrenamiento(int id,  LocalDate fecha, Usuario usuario, Rutina rutina, EstadoSesion estado) {
		
		
		if(usuario == null) {
			throw new UsuarioInvalidoException("La sesión debe estar asociada a un usuario");
		}
		if(rutina == null) {
			throw new RutinaInvalidaException("La sesión debe estar asociada a una rutina.");
		}
		
		
		//TODO
		
		//Aquí se verifica si la rutina pertenece al mismo usuario,
		//sin embargo, para poder implementarlo, es necesario sobrescribir equals() 
		//y por lo tanto, también hashCode(). Hay que determinar cuál será el identificador del objeto ( documento o Id ) 
	    /*if (!rutina.getUsuario().equals(usuario)) 
	        throw new UsuarioInvalidoException("La rutina no pertenece al usuario indicado");
	    }*/ 
		
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
		return id;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public Rutina getRutina() {
		return rutina;
	}
	
	
	
	public EstadoSesion getEstado() {
		
		return estado;
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
