package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gymtracker.model.Usuario;
import com.gymtracker.service.Servicios;

public class VentanaLogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//ATRIBUTOS
	

	private static final String INGRESO = "INGRESO";
	private static final String REGISTRO = "REGISTRO";
	
	
	private final Servicios servicios;
	
	
	private final CardLayout cartas;
	private final JPanel contenedor;
	private final PanelIngreso panelIngreso;
	private final PanelRegistro panelRegistro;

	//CONSTRUCTOR
	

	/**
	 * @param usuarioService
	 * @param ejercicioService
	 * @param rutinaService
	 * @param sesionService
	 * @param pestanas
	 *
	 */
	public VentanaLogin(Servicios servicios) {
		super("Gym Tracker");
		this.servicios = servicios;
	

		panelIngreso =  new PanelIngreso(servicios.getUsuarios(), this);
		
		panelRegistro = new PanelRegistro(servicios.getUsuarios(), this);
		
		
		cartas = new CardLayout();
		contenedor = new JPanel(cartas);
		contenedor.setBackground(Tema.FONDO);
		contenedor.add(panelIngreso, INGRESO);
		contenedor.add(panelRegistro, REGISTRO);
		
		getContentPane().setBackground(Tema.FONDO);
		setLayout(new BorderLayout());
		add(crearEncabezado(), BorderLayout.NORTH);
		add(contenedor, BorderLayout.CENTER);
	
	
		setSize(900,780);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
				
		mostrarIngreso();
		/**
		 * Lo llaman PanelIngreso y PanelRegistro cuando el servicio acepta los datos
		 */
		
		
		
	}
		
		private Component crearEncabezado() {
		JPanel encabezado = new JPanel(new BorderLayout());
		encabezado.setBackground(Tema.FONDO);
		encabezado.setBorder(BorderFactory.createEmptyBorder(16,32,16,32));
		
		JLabel logo = new JLabel("GYM TRACKER");
		logo.setFont(new Font("SansSerif", Font.BOLD, 22));
		logo.setForeground(Tema.TEXTO);
		
		JLabel etiqueta = new JLabel("GYM TRACKER");
		etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12));
		etiqueta.setForeground(Tema.TEXTO_SUAVE);
		
		encabezado.add(logo, BorderLayout.WEST);
		encabezado.add(etiqueta, BorderLayout.EAST);
		return encabezado;
	}

		

		public void mostrarRegistro() {
			cartas.show(contenedor, REGISTRO);
			panelRegistro.activaEnter();
			
		}

		public void mostrarIngreso() {
			cartas.show(contenedor, INGRESO);
			panelIngreso.activaEnter();
			
		}

		
		/**
		 * @param usuario
		 */
		public void abrirPrincipal(Usuario usuario) {
			VentanaPrincipal principal = new VentanaPrincipal(usuario, servicios);
			principal.setVisible(true);
			dispose();
		
		}
	
	
	
	
	
	//METODOS 

}
