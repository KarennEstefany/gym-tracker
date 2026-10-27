package com.gymtracker.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gymtracker.service.UsuarioService;

public class PanelRegistro extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private UsuarioService usuarioService;


	public PanelRegistro(UsuarioService usuarioService, VentanaLogin ventanaLogin) {
		
		this.usuarioService = usuarioService;
		add(new JLabel("Panel de de registro (pendiente)"));
	}


	public void activaEnter() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
