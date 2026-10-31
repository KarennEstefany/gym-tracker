package com.gymtracker.ui;

import javax.swing.JLabel;

import com.gymtracker.model.Usuario;

public class PanelInBody extends PanelPestana{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Usuario usuario;

	public PanelInBody(Usuario usuario) {
		super();
		this.usuario = usuario;
		add(new JLabel("Seguimiento de " + usuario.getNombre() + "pendiente"));
	}
	

}
