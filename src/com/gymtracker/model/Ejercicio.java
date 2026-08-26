package com.gymtracker.model;

public class Ejercicio {
	private int id;
	private String codigo;
	private String nombre;
	private String grupoMuscular;
	private String categoria;
	private TipoEjercicio tipo;
	/**
	 * @param id
	 * @param codigo
	 * @param nombre
	 * @param grupoMuscular
	 * @param categoria
	 * @param tipo
	 */
	public Ejercicio(int id, String codigo, String nombre, String grupoMuscular, String categoria, TipoEjercicio tipo) {
		this.id = id;
		this.codigo = codigo;
		setNombre(nombre);
		setGrupoMuscular(grupoMuscular);
		setCategoria(categoria);
		this.tipo = tipo;
	}
	public int getId() {
		return id;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
	if (nombre ==null || nombre.trim().isEmpty()) {
		throw new IllegalArgumentException ("El nombre del Ejercicio no puede estar vacio");
	}
	this.nombre = nombre;
	}
	public String getGrupoMuscular() {
		return grupoMuscular;
	}
	public void setGrupoMuscular(String grupoMuscular) {
		if (grupoMuscular ==null || grupoMuscular.trim().isEmpty()) {
			throw new IllegalArgumentException ("Ingrese el grupo muscular que trabaja el ejercicio (ej. Cuadriceps");
		
		}
		this.grupoMuscular = grupoMuscular;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		if (categoria==null || categoria.trim().isEmpty()) {
			throw new IllegalArgumentException ("Ingrese la categoria a la que pertenece el ejercicio (ej. Piernas/Tren Inferior");
		}
		this.categoria = categoria;
	}
	public TipoEjercicio getTipo() {
		return tipo;
	}
	
	

}
