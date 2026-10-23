package com.gymtracker.model;

import com.gymtracker.exceptions.EdadEstaturaInvalidaException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;
import java.util.Objects;

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
			throw new NombreNullException("El nombre del usuario no puede estar vacio");
		}
		
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int edad) {
		if (edad <=0 || edad>120 ) {
			throw new EdadEstaturaInvalidaException("La edad debe estar entre 1 y 120");
		}
		this.edad = edad;
	}
	public void setEstatura(double estatura) {
		if (estatura<=0.49 || estatura >2.30) {
			throw new EdadEstaturaInvalidaException("La estatura debe estar entre 50 y 2.30m");
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
			throw new ObjetivoInvalidoException("El objetivo del usuario no puede estar vacio");
		}
		this.objetivo = objetivo;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	        return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	        return false;
	    }
	    Usuario otro = (Usuario) o;
	    return Objects.equals(documento, otro.documento);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(documento);
	}
	
	
	

}
