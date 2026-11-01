package com.gymtracker.service;

public class Servicios {
	
	private final UsuarioService usuarios;
	private final EjercicioService ejercicios;
	private final RutinaService rutinas;
	private final SesionService sesiones;
	private final RecordService records;
	private final InBodyService inBody;
	public Servicios(UsuarioService usuarios, EjercicioService ejercicios, RutinaService rutinas,
			SesionService sesiones, RecordService records, InBodyService inBody) {
		super();
		this.usuarios = usuarios;
		this.ejercicios = ejercicios;
		this.rutinas = rutinas;
		this.sesiones = sesiones;
		this.records = records;
		this.inBody = inBody;
	}
	/**
	 * @return the usuarios
	 */
	public UsuarioService getUsuarios() {
		return usuarios;
	}
	/**
	 * @return the ejercicios
	 */
	public EjercicioService getEjercicios() {
		return ejercicios;
	}
	/**
	 * @return the rutinas
	 */
	public RutinaService getRutinas() {
		return rutinas;
	}
	/**
	 * @return the sesiones
	 */
	public SesionService getSesiones() {
		return sesiones;
	}
	/**
	 * @return the records
	 */
	public RecordService getRecords() {
		return records;
	}
	/**
	 * @return the inBody
	 */
	public InBodyService getInBody() {
		return inBody;
	}
	
	
	

}
