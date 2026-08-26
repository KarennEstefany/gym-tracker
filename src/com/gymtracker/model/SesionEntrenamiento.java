package com.gymtracker.model;
import java.time.LocalDate;

public class Rutina {
	private int id;
	private LocalDate fecha; 
	private int usuarioId; 
	private int rutinaId; 
	
	//Falta el enum de estado
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 
	 */
	public Ejercicio(int id, int usuarioId, LocalDate fecha) {
		this.id = id;
		this.nombre = nombre;
		setNombre(nombre);
		this.usuarioId = usuairoId;
	}
	public int getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
	if (nombre ==null || nombre.trim().isEmpty()) {
		throw new IllegalArgumentException ("El nombre no puede estar vacío");
	}
	this.nombre = nombre;
	}
	
	
	

}
