package com.gymtracker.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.UsuarioService;

public class PanelIngreso extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//ATRIBUTOS 
	private static final String INGRESAR = "INGRESAR";
	private static final String VOLVER = "VOLVER";
	private static final String IR_REGISTRO = "IR_REGISTRO";
	
	private final UsuarioService usuarioService;
	private final VentanaLogin ventana;
	
	private final JTextField campoDocumento;
	private final JPasswordField campoContrasena;
	private final JButton botonIngresar;

	public PanelIngreso(UsuarioService usuarioService, VentanaLogin ventana) {
		this.usuarioService = usuarioService;
		this.ventana = ventana;
		
		setBackground(Tema.FONDO);
		setLayout(new GridBagLayout());
		
		campoDocumento = new JTextField();
		campoContrasena = new JPasswordField();
		Tema.estilizarCampo(campoDocumento);
		Tema.estilizarCampo(campoContrasena);
		botonIngresar = Tema.crearBotonPrimario("Acceder →", INGRESAR,  this);
		
		//Tarjeta con el formulario 
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(32,40,32,40));
		tarjeta.setAlignmentX(LEFT_ALIGNMENT);
		
		
		JLabel titulo = new JLabel("Iniciar Sesión");
		titulo.setFont(new Font("SansSErif", Font.BOLD,28));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(LEFT_ALIGNMENT);
		
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,6)));
		tarjeta.add(Tema.crearEtiqueta("Accede a tu cuenta GYM TRACKER"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,24)));
		tarjeta.add(Tema.crearEtiqueta("Documento de identidad"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,6)));
		tarjeta.add(campoDocumento);
		tarjeta.add(Box.createRigidArea(new Dimension(0,16)));
		tarjeta.add(Tema.crearEtiqueta("Contraseña"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,6)));
		tarjeta.add(campoContrasena);
		tarjeta.add(Box.createRigidArea(new Dimension (0,26)));
		tarjeta.add(botonIngresar);
		tarjeta.add(Box.createRigidArea(new Dimension(0,16)));
		tarjeta.add(crearFilaRegistro());
		
		
		
		JPanel columna = new JPanel();
		columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
		columna.setOpaque(false);
		columna.add(Tema.crearEnlace("← Volver al inicio", VOLVER, this, Tema.TEXTO_SUAVE));
		columna.add(Box.createRigidArea(new Dimension(0,14)));
		columna.add(tarjeta);
		
		add(columna);
	}
	
	
	private JPanel crearFilaRegistro() {
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 4,0));
		fila.setOpaque(false);
		fila.setAlignmentX(LEFT_ALIGNMENT);
		fila.setMaximumSize(new Dimension(380, 30));
		
		JLabel pregunta = new JLabel("¿No tienes una cuenta?");
		pregunta.setForeground(Tema.TEXTO_SUAVE);
		fila.add(pregunta);
		fila.add(Tema.crearEnlace("Crear cuenta", IR_REGISTRO, this, Tema.ENLACE));
		
		return fila;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String comando = e.getActionCommand();
		if(INGRESAR.equals(comando)) {
			ingresar();
		} else if(VOLVER.equals(comando)) {
			limpiar();
			ventana.mostrarInicio();
		} else if(IR_REGISTRO.equals(comando)) {
			limpiar();
			ventana.mostrarRegistro();
		}
	}
	
	
	private void ingresar() {
		try {
			String documento = campoDocumento.getText().trim();
			String contrasena = new String(campoContrasena.getPassword());
			
			Usuario usuario = usuarioService.ingresar(documento, contrasena);
			limpiar();
			ventana.abrirPrincipal(usuario);
		} catch (GymTrackerException ex) {
			campoContrasena.setText("");
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo ingresar", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void limpiar() {
		campoDocumento.setText("");
		campoContrasena.setText("");
	}

	public void activaEnter() {
		// TODO Auto-generated method stub
		ventana.getRootPane().setDefaultButton(botonIngresar);
	}
	
	
	

}
