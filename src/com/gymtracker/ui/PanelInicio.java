package com.gymtracker.ui;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelInicio extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//ATRIBUTOS 
	private static final String CREAR_CUENTA = "CREAR_CUENTA";
	private static final String INICIAR_SESION  = "INICIAR_SESION";
	
	private final VentanaLogin ventana;
	
	
	//CONSTRUCTOR
	/**
	 * @param ventana
	 */
	public PanelInicio(VentanaLogin ventana) {
		
		this.ventana  = ventana;
		
		setBackground(Tema.FONDO);
		setLayout(new GridBagLayout());
		
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(40,  50, 40, 50));
		
		
		JLabel titulo = new JLabel("GYM TRACKER");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 34));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel lema = new JLabel("<html><div style='text-align:center'>Tu progreso,<br>en tus manos</div></html>",
                SwingConstants.CENTER);
		lema.setFont(new Font("SansSerif", Font.PLAIN, 26));
		lema.setForeground(Tema.TEXTO);
		lema.setAlignmentX(CENTER_ALIGNMENT);
		
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,30)));
		tarjeta.add(lema);
		tarjeta.add(Box.createRigidArea(new  Dimension(0,35)));
		tarjeta.add(crearBoton("Crear cuenta", CREAR_CUENTA, Tema.BOTON_PRIMARIO));
		tarjeta.add(Box.createRigidArea(new Dimension(0,12)));
		tarjeta.add(crearBoton("Iniciar Sesión",INICIAR_SESION, Tema.BOTON_SECUNDARIO));
		
		
		add(tarjeta);
	}
	
	
	
	private Component crearBoton(String texto, String comando,java.awt.Color fondo ) {
		// TODO Auto-generated method stub
		JButton boton = new JButton(texto);
		boton.setActionCommand(comando);
		boton.addActionListener(this);
		boton.setBackground(fondo);
		boton.setForeground(Tema.TEXTO);
		boton.setOpaque(true);
		boton.setFocusPainted(false);
		boton.setFont(new Font("SansSerif", Font.BOLD, 15));
		Dimension tamano = new Dimension(380, 46);
		boton.setPreferredSize(tamano);
		boton.setMaximumSize(tamano);
		boton.setAlignmentX(CENTER_ALIGNMENT);
		return boton;
		
		
		
	}
	
	


	@Override
	public void actionPerformed(ActionEvent e) {

		if(CREAR_CUENTA.equals(e.getActionCommand())) {
			ventana.mostarRegistro();
		} else if(INICIAR_SESION.equals(e.getActionCommand())){
			ventana.mostrarIngreso();
			
			
		}
		
	}

	

}
