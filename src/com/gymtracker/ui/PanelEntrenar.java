package com.gymtracker.ui;

import javax.swing.JLabel;

import com.gymtracker.model.Usuario;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.SesionService;

public class PanelEntrenar extends PanelPestana{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Usuario usuario;
	private final SesionService sesionService;
	private final RutinaService rutinaService;
	public PanelEntrenar(Usuario usuario, SesionService sesionService, RutinaService rutinaService) {
		super();
		this.usuario = usuario;
		this.sesionService = sesionService;
		this.rutinaService = rutinaService;
		
		add(new JLabel("Panel de etrenamiento de " + usuario.getNombre() + "pendiente"));
		
	}
	
	
	

}
