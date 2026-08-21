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
		setEdad(edad);
		setEstatura(estatura);
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
	
	public void setEdad(int edad) {
		if (edad <=0 || edad>120 ) {
			throw new IllegalArgumentException("La edad debe estar entre 0 y 120");
		}
		this.edad = edad;
	}
	public void setEstatura(double estatura) {
		if (estatura<=0 || estatura >230) {
			throw new IllegalArgumentException("La estatura debe estar entre 0 y 2.30m");
		}
		this.estatura = estatura;
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
