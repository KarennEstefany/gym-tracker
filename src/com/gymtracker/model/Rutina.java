package com.gymtracker.model;

import com.gymtracker.exceptions.DiaSemanaInvalidaException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.UsuarioInvalidoException;


public class Rutina {
	private final int id;
	private String nombre; //Ejemplo : Día de pierna
	private final Usuario usuario; 
	private DiaSemana diaSemana; 
	
	/**
	 * @param id
	 * @param nombre
	 * @param usuario
	 * @param diaSemana
	 
	 */
	
	public Rutina(int id, String nombre, Usuario usuario, DiaSemana diaSemana) {
		super();
		this.id = id;
		setNombre(nombre);
		if(usuario ==null) {
			throw new UsuarioInvalidoException("La rutina debe estar asociada a un usuario.");
		}
		this.usuario = usuario;
		setDiaSemana(diaSemana);
	}

	
	public int getId() {
		return id;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	
	public Usuario getUsuario() {
		
		return usuario;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	
	//Este método verifica que se ingrese un día de la semana válido, no nulo
	public void setDiaSemana(DiaSemana diaSemana) {
	       if (diaSemana == null) {
	           throw new DiaSemanaInvalidaException("El día de la semana no puede ser nulo");
	       }
	       this.diaSemana = diaSemana;
	}

	
	// Valida que el nombre no sea nulo
	public void setNombre(String nombre) {
	if (nombre ==null || nombre.trim().isEmpty()) {
		throw new NombreNullException("El nombre no puede estar vacío");
	}
	this.nombre = nombre;
	}
	

}
