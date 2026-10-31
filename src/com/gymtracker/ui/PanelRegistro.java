package com.gymtracker.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.UsuarioService;

public class PanelRegistro extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String REGISTRAR = "REGISTRAR";
	private static final String IR_INGRESO = "IR_INGRESO";
	private static final int ANCHO_CAMPO = 300;
	
	
	private final UsuarioService usuarioService;
	private final VentanaLogin ventana;
	
	private final JTextField campoNombre;
	private final JTextField campoDocumento;
	private final JPasswordField campoContrasena;
	private final JPasswordField campoConfirmacion;
	private final JTextField campoEdad;
	private final JTextField campoEstatura;
	private final JComboBox<Objetivo> comboObjetivo;
	private final JButton botonRegistrar;


	


	public PanelRegistro(UsuarioService usuarioService, VentanaLogin ventana) {
		
		this.usuarioService = usuarioService;
		this.ventana = ventana;
		
		setBackground(Tema.FONDO);
		setLayout(new GridBagLayout());
		
		campoNombre = new JTextField();
		campoDocumento = new JTextField();
		campoContrasena = new JPasswordField();
		campoConfirmacion = new JPasswordField();
		campoEdad = new JTextField();
		campoEstatura = new JTextField();
		comboObjetivo = new JComboBox<Objetivo>(Objetivo.values());
		

		Tema.estilizarCampo(campoNombre, ANCHO_CAMPO);
		Tema.estilizarCampo(campoDocumento, ANCHO_CAMPO);
		Tema.estilizarCampo(campoContrasena, ANCHO_CAMPO);
		Tema.estilizarCampo(campoConfirmacion, ANCHO_CAMPO);
		Tema.estilizarCampo(campoEdad, ANCHO_CAMPO);
		Tema.estilizarCampo(campoEstatura, ANCHO_CAMPO);
		Tema.estilizarCombo(comboObjetivo, ANCHO_CAMPO);
		
		botonRegistrar = Tema.crearBotonPrimario("Crear cuenta →", REGISTRAR,this);
		
		
		// Columna izquierda 
		JPanel columnaPersonal = crearColumna("Datos personales");
		agregarCampo(columnaPersonal, "Nombre completo", campoNombre, null);
		agregarCampo(columnaPersonal, "Documento de identidad", campoDocumento, null);
		agregarCampo(columnaPersonal, "Contraseña", campoContrasena, "Mínimo 6 caracteres");
		agregarCampo(columnaPersonal, "Confirma tu contraseña", campoConfirmacion, null);
		
		//Columna derecha 
		
		JPanel columnaFisica = crearColumna("Datos físicos");
		agregarCampo(columnaFisica, "Edad (años)", campoEdad, null);
		agregarCampo(columnaFisica, "Estatura (metros)", campoEstatura, null);
		agregarCampo(columnaFisica, "Objetivo", comboObjetivo, null);
		
		// Las dos columnas lado  a lado 
		JPanel columnas = new JPanel(new GridLayout(1,2,16,0));
		columnas.setOpaque(false);
		columnas.setAlignmentX(LEFT_ALIGNMENT);
		columnas.add(columnaPersonal);
		columnas.add(columnaFisica);
		
		//Botón centrado dentro de una fila 
		JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		filaBoton.setOpaque(false);
		filaBoton.setAlignmentX(LEFT_ALIGNMENT);
		filaBoton.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
		filaBoton.add(botonRegistrar);
		
		
		//Tarjeta principal 
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(28,36,28,36));
		
		
		JLabel titulo = new JLabel("Crear cuenta");
		titulo.setFont(new Font("SansSerif", Font.BOLD,28));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(LEFT_ALIGNMENT);
		
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,4)));
		tarjeta.add(Tema.crearEtiqueta("Completa tus datos para empezar"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,18)));
		tarjeta.add(columnas);
		tarjeta.add(Box.createRigidArea(new Dimension(0,20)));
		tarjeta.add(filaBoton);
		tarjeta.add(Box.createRigidArea(new Dimension(0,12)));
		tarjeta.add(crearFilaIngreso());
		
		add(tarjeta);
	} 
	
	
	
	// Subtarjeta con título (Datos personales y Datos físicos) 
	private JPanel crearColumna(String tituloColumna) {
		
		JPanel columna = new JPanel();
		columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
		columna.setBackground(Tema.SUBTARJETA);
		columna.setBorder(BorderFactory.createEmptyBorder(18,20,18,20));
		JLabel encabezado = new JLabel(tituloColumna);
		encabezado.setFont(new Font("SansSerif", Font.BOLD, 15));
		encabezado.setForeground(Tema.TEXTO);
		encabezado.setAlignmentX(LEFT_ALIGNMENT);
		
		columna.add(encabezado);
		columna.add(Box.createRigidArea(new Dimension(0,14)));
		
		return columna; 
	}
	
	private void agregarCampo(JPanel columna, String texto, Component campo, String ayuda) {
		
		columna.add(Tema.crearEtiqueta(texto));
		columna.add(Box.createRigidArea(new Dimension(0,6)));
		columna.add(campo);
		
		if(ayuda !=null) {
			columna.add(Box.createRigidArea(new Dimension(0,4)));
			JLabel textoAyuda = Tema.crearEtiqueta(ayuda);
			textoAyuda.setFont(new Font("SansSerif", Font.PLAIN, 11));
			columna.add(textoAyuda);
		
		}
		columna.add(Box.createRigidArea(new Dimension(0,14)));
	}
	
	private JPanel crearFilaIngreso() {
		
		JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 4,0));
		fila.setOpaque(false);
		fila.setAlignmentX(LEFT_ALIGNMENT);
		fila.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
		
		JLabel pregunta = new JLabel("¿Ya tienes una cuenta? ");
		pregunta.setForeground(Tema.TEXTO_SUAVE);
		fila.add(pregunta);
		fila.add(Tema.crearEnlace("Iniciar Sesión", IR_INGRESO, this, Tema.ENLACE));
		return fila;
	}
	
	
	//TODO 
	private void registrar() {
		
		try {
			
			String documento = campoDocumento.getText().trim();
			String nombre = campoNombre.getText().trim();
			int edad = Integer.parseInt(campoEdad.getText().trim());
			double estatura = Double.parseDouble(campoEstatura.getText().trim().replace(',', '.'));
			Objetivo objetivo = (Objetivo) comboObjetivo.getSelectedItem();
			String contrasena = new String(campoContrasena.getPassword());
			String confirmacion = new String(campoConfirmacion.getPassword());
			
			if(!contrasena.equals(confirmacion)) {
				limpiarContrasenas();
				JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Registro", JOptionPane.WARNING_MESSAGE);
				
			}
			
			Usuario usuario = usuarioService.registrarUsuario(documento, nombre, edad, estatura, objetivo, contrasena);
			limpiarCampos();
			ventana.abrirPrincipal(usuario);
			
	} catch(NumberFormatException ex) {
		
		JOptionPane.showMessageDialog(this, "La edad y la estatura deben ser un número entero ","Datos inválidos", JOptionPane.WARNING_MESSAGE);
		
	} catch (GymTrackerException ex) {
		
		limpiarContrasenas();
		JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
		
		}
		
	}
	
	
	private void limpiarContrasenas() {
		campoContrasena.setText("");
		campoConfirmacion.setText("");
	}

	private void limpiarCampos() {
		
		campoNombre.setText("");
		campoDocumento.setText("");
		campoEdad.setText("");
		campoEstatura.setText("");
		comboObjetivo.setSelectedIndex(0);
		limpiarContrasenas();
	}

	public void activaEnter() {
		ventana.getRootPane().setDefaultButton(botonRegistrar);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		
		if(REGISTRAR.equals(comando)) {
			registrar();
		} else if(IR_INGRESO.equals(comando)) {
			limpiarCampos();
			ventana.mostrarIngreso();
		}
	}
	
	
	
}
