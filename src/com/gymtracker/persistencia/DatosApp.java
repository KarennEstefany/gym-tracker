package com.gymtracker.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetRegistro;
import com.gymtracker.model.Usuario;

public class DatosApp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final List<Usuario> usuarios = new ArrayList<Usuario>();
	private final List<Ejercicio> ejercicios = new ArrayList<Ejercicio>();
	private final List<Rutina> rutinas = new ArrayList<Rutina>();
	private final List<RutinaEjercicio> ejerciciosDeRutinas = new ArrayList<RutinaEjercicio>();
	private final List<SesionEntrenamiento> sesiones = new ArrayList<SesionEntrenamiento>();
	private final List<SetRegistro> historial = new ArrayList<SetRegistro>();
	
	
	
	/**
	 * @return the usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	/**
	 * @return the ejercicio
	 */
	public List<Ejercicio> getEjercicio() {
		return ejercicios;
	}
	/**
	 * @return the rutinas
	 */
	public List<Rutina> getRutinas() {
		return rutinas;
	}
	/**
	 * @return the ejerciciosDeRutinas
	 */
	public List<RutinaEjercicio> getEjerciciosDeRutinas() {
		return ejerciciosDeRutinas;
	}
	/**
	 * @return the sesiones
	 */
	public List<SesionEntrenamiento> getSesiones() {
		return sesiones;
	}
	/**
	 * @return the historial
	 */
	public List<SetRegistro> getHistorial() {
		return historial;
	}
	
	
	
	

}
