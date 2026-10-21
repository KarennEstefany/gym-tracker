package com.gymtracker.service;

import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.SetRegistro;

public class AccionSet {

	
	private final SetRegistro setRegistrado;
	private final SetPlaneado setPlaneado;
	
	
	public AccionSet(SetRegistro setRegistrado, SetPlaneado setPlaneado) {
		super();
		this.setRegistrado = setRegistrado;
		this.setPlaneado = setPlaneado;
	}


	/**
	 * @return the setRegistrado
	 */
	public SetRegistro getSetRegistrado() {
		return setRegistrado;
	}


	/**
	 * @return the setPlaneado
	 */
	public SetPlaneado getSetPlaneado() {
		return setPlaneado;
	} 
	
	
	
	
}
