package com.gymtracker.model;

import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.DiaSemanaInvalidaException;



public class Rutina {
	private final int id;
	private String nombre; //Ejemplo : Día de pierna
	private final int usuarioId; 
	private DiaSemana diaSemana; 
	
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 * @param diaSemana
	 
	 */
	
	public Rutina(int id, String nombre, int usuarioId, DiaSemana diaSemana) {
		super();
		this.id = id;
		setNombre(nombre);
		this.usuarioId = usuarioId;
		setDiaSemana(diaSemana);
	}

	
	public int getId() {
		return id;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	
	public int getUsuarioId() {
		
		return usuarioId;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
	       if (diaSemana == null) {
	           throw new DiaSemanaInvalidaException("El día de la semana no puede ser nulo");
	       }
	       this.diaSemana = diaSemana;
	}
	
	public void setNombre(String nombre) {
	if (nombre ==null || nombre.trim().isEmpty()) {
		throw new NombreNullException("El nombre no puede estar vacío");
	}
	this.nombre = nombre;
	}
	

}
