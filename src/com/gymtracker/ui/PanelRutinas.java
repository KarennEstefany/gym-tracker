package com.gymtracker.ui;

import javax.swing.JLabel;

import com.gymtracker.model.Usuario;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.RutinaService;

public class PanelRutinas extends PanelPestana {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Usuario usuario;
	private final RutinaService rutinaService;
	private final EjercicioService ejercicioService;
	public PanelRutinas(Usuario usuario, RutinaService rutinaService, EjercicioService ejercicioService) {
		super();
		this.usuario = usuario;
		this.rutinaService = rutinaService;
		this.ejercicioService = ejercicioService;
		add(new JLabel("Rutina de " + usuario.getNombre() + "pendiente"));
	}
	
	
	
}
