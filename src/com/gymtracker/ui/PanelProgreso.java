package com.gymtracker.ui;

import javax.swing.JLabel;

import com.gymtracker.model.Usuario;

public class PanelProgreso extends PanelPestana{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Usuario usuario;

	public PanelProgreso(Usuario usuario) {
		super();
		this.usuario = usuario;
		add(new JLabel("Progreso de " + usuario.getNombre() + "pendiente"));
	}
	

}
