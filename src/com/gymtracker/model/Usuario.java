package com.gymtracker.model;

public class Usuario {
	private int id;
	private String documento;
	private String nombre;
	private int edad;
	private double estatura;
	private Objetivo objetivo;
	/**
	 * @param id
	 * @param documento
	 * @param nombre
	 * @param edad
	 * @param estatura
	 * @param objetivo
	 */
	public Usuario(int id, String documento, String nombre, int edad, double estatura, Objetivo objetivo) {
		super();
		this.id = id;
		this.documento = documento;
		setNombre(nombre);
		this.edad = edad;
		this.estatura = estatura;
		setObjetivo (objetivo);
	}
	public int getId() {
		return id;
	}
	public String getDocumento() {
		return documento;
	}
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del usuario no puede estar vacio");
		}
		
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	
	public double getEstatura() {
		return estatura;
	}
	public Objetivo getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(Objetivo objetivo) {
		if (objetivo== null) {
			throw new IllegalArgumentException("El objetivo del usuario no puede estar vacio");
		}
		this.objetivo = objetivo;
	}
	

}
