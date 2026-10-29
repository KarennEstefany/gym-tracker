package com.gymtracker.ui;

import javax.swing.JLabel;

import com.gymtracker.model.Usuario;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.UsuarioService;

public class PanelUsuario extends PanelPestana {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Usuario usuario;
	private final UsuarioService usuarioService;
	private final RutinaService rutinaService;
	public PanelUsuario(Usuario usuario, UsuarioService usuarioService, RutinaService rutinaService) {
		super();
		this.usuario = usuario;
		this.usuarioService = usuarioService;
		this.rutinaService = rutinaService;
		
		add(new JLabel("Perfil de " + usuario.getNombre() + "pendiente"));
	}
	
	
	

}
