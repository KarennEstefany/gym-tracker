package com.gymtracker.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
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
	public static final Color ENLACE = new Color(80, 150, 255);
	
	
	
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
		Dimension tamano = new Dimension(380,44);
		campo.setPreferredSize(tamano);
		campo.setMaximumSize(tamano);
		campo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
	}
	
	
}
