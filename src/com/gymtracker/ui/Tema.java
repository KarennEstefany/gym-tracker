package com.gymtracker.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class Tema {
	
	/**
	 * Colores predefinidos para el tema de la aplicación 
	 */
	public static final Color FONDO = new Color(10, 12, 16);
	public static final Color TARJETA = new Color(22,25,31);
	public static final Color BOTON_PRIMARIO = new Color(41,121,255);
	public static final Color BOTON_SECUNDARIO = new Color(30,34,44);
	public static final Color TEXTO = Color.WHITE;
	public static final Color TEXTO_SUAVE = new Color(150, 156, 168);
	
	public static final Color CAMPO  =  new Color(34,38, 48);
	public static final Color  BORDE_CAMPO = new Color(50,55,68);
	public static final Color ENLACE = new Color(80,150,255);
	
	public static final Color SUBTARJETA = new Color(28,31,38);
	
	
	private Tema() {
		
	}
	
	public static void estilizarCampo(JTextField campo) {
		campo.setBackground(CAMPO);
		campo.setForeground(TEXTO);
		campo.setCaretColor(TEXTO);
		campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDE_CAMPO),
				BorderFactory.createEmptyBorder(10,12,10,12)));
		Dimension tamano = new Dimension(380,40);
		campo.setPreferredSize(tamano);
		campo.setMaximumSize(tamano);
		campo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
	}
	
	// Sobrecarga el método para utilizarlo en panelIngreso 
	
	public static void estilizarCampo(JTextField campo, int ancho) {
		
		estilizarCampo(campo);
		/*Dimension tamano = new Dimension(ancho, 30);
		campo.setPreferredSize(tamano);
		campo.setMaximumSize(tamano);
		*/
		Dimension preferido = new Dimension(ancho, 40);
		Dimension maximo = new Dimension(Integer.MAX_VALUE, 40);
		
		campo.setPreferredSize(preferido);
		campo.setMaximumSize(maximo);
	}
	
	public static JLabel crearEtiqueta(String texto) {
		
		JLabel etiqueta = new JLabel(texto);
		etiqueta.setFont(new Font("SanSerif", Font.PLAIN, 13));
		etiqueta.setForeground(TEXTO_SUAVE);
		etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
		return etiqueta;
		
	}
	
	public static JButton crearBotonPrimario(String texto, String comando, ActionListener oyente) {
		
		JButton boton  = new JButton(texto);
		
		
		boton.setActionCommand(comando);
		boton.addActionListener(oyente);
		boton.setBackground(BOTON_PRIMARIO);
		boton.setForeground(TEXTO);
		boton.setOpaque(true);
		boton.setFocusPainted(false);
		boton.setFont(new Font("SansSerif", Font.BOLD, 15 ));
		Dimension tamano = new Dimension(380, 46);
		boton.setPreferredSize(tamano);
		boton.setMaximumSize(tamano);
		boton.setAlignmentX(Component.LEFT_ALIGNMENT);
		return boton;
	}
	
	public static JButton crearEnlace(String texto, String comando, ActionListener oyente, Color color) {
		
		
		JButton enlace  = new JButton(texto);
		
		
		enlace.setActionCommand(comando);
		enlace.addActionListener(oyente);
		enlace.setForeground(color);
		enlace.setFont(new Font("SanSerif", Font.BOLD, 13));
		enlace.setContentAreaFilled(false);
		enlace.setBorderPainted(false);
		enlace.setFocusPainted(false);
		enlace.setOpaque(false);      
		enlace.setMargin(new Insets(0,0,0,0));
		enlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
		enlace.setAlignmentX(Component.LEFT_ALIGNMENT);
			
		return enlace;
	}
	
	
	// Estiliza combo para las opciones de objetivo en panelIngreso
	
	public static void estilizarCombo(JComboBox<?> combo, int ancho) {
		
		combo.setBackground(CAMPO);
		combo.setForeground(TEXTO);
		combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		combo.setPreferredSize(new Dimension(ancho, 40));
		combo.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
		combo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
	}
	
	
	
	
	
}
