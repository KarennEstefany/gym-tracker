package com.gymtracker.model;

public class Rutina {
	private int id;
	private String nombre; //Ejemplo : Día de pierna
	private int usuarioId; 
	//Falta el enum de Días de semana
	/**
	 * @param id
	 * @param nombre
	 * @param usuarioId
	 
	 */
	public Ejercicio(int id, String nombre, int usuarioId) {
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
