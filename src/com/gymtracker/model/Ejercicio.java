package com.gymtracker.model;

import com.gymtracker.exceptions.CategoriaInvalidaException;
import com.gymtracker.exceptions.GrupoMuscularInvalidoException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;

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
		throw new NombreNullException("El nombre del Ejercicio no puede estar vacio");
	}
	this.nombre = nombre;
	}
	public String getGrupoMuscular() {
		return grupoMuscular;
	}
	public void setGrupoMuscular(String grupoMuscular) {
		if (grupoMuscular ==null || grupoMuscular.trim().isEmpty()) {
			throw new GrupoMuscularInvalidoException("Ingrese el grupo muscular que trabaja el ejercicio (ej. Cuadriceps");
		
		}
		this.grupoMuscular = grupoMuscular;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		if (categoria==null || categoria.trim().isEmpty()) {
			throw new CategoriaInvalidaException("Ingrese la categoria a la que pertenece el ejercicio (ej. Piernas/Tren Inferior");
		}
		this.categoria = categoria;
	}
	public TipoEjercicio getTipo() {
		return tipo;
	}
	
	/**
	 *Valida el objetivo de acuerdo con el tipo de ejercicio (TIEMPO o PESO) 
	 *Si es de tipo PESO: no puede exceder 30 repeticiones 
	 *Si es de tipo TIEMPO: no puede exceder los 600 segundos 
	 *Lanza una excepcion de ObjetivoInvalidoException 
	 * @param objetivo recibe el objetivo que ingresa el usuario 
	 * @return objetivo retorna el mismo objetivo una vez se ha validado 
	 */
	public int verificarObjetivo(int objetivo) {
		
		int maximo = (tipo ==TipoEjercicio.PESO) ? 30 : 600;
		
		if(objetivo <=0 || objetivo>maximo) {
			throw new ObjetivoInvalidoException("Objetivo no válido para este ejercicio");
		}
		return objetivo;
	}
	@Override
	public String toString() {
		return "Ejercicio [id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", grupoMuscular=" + grupoMuscular
				+ ", categoria=" + categoria + ", tipo=" + tipo + "]";
	}
	
	
	

}
