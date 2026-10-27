package com.gymtracker.ui;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.gymtracker.model.Usuario;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.SesionService;
import com.gymtracker.service.UsuarioService;


public class VentanaPrincipal extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// ATRIBUTOS 
	private Usuario usuario;
	private UsuarioService usuarioService;
	private RutinaService rutinaService;
	private EjercicioService ejercicioService;
	private SesionService sesionService;
	public VentanaPrincipal(Usuario usuario,  UsuarioService usuarioService, RutinaService rutinaService,
			EjercicioService ejercicioService, SesionService sesionService) throws HeadlessException {
		
		super();
		
		/**
		this.usuario = usuario;
		this.usuarioService = usuarioService;
		this.rutinaService = rutinaService;
		this.ejercicioService = ejercicioService;
		this.sesionService = sesionService;
		*/
		
		add(new JLabel( "Bienvenido" + usuario.getNombre(), SwingConstants.CENTER));
		setSize(800,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	//CONSTRUCTOR 
	
	
	
	//METODOS
	

}
