package com.gymtracker.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gymtracker.service.UsuarioService;

public class PanelIngreso extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//Constructor 
	
	private UsuarioService usuarioService;


	public PanelIngreso(UsuarioService usuarioService, VentanaLogin ventanaLogin) {
		this.usuarioService = usuarioService;
		add(new JLabel("Panel de ingreso pendiente"));
	}


	

	public void activaEnter() {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
