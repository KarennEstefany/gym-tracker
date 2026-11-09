package com.gymtracker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.EstadoSesion;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.Servicios;


public class VentanaPrincipal extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// ATRIBUTOS 
	private static final String  CERRAR_SESION = "CERRAR_SESION";
	
	private Usuario usuarioActual;
	private Servicios servicios;
	
	private final JTabbedPane pestanas;
	private final PanelUsuario panelUsuario;
	private final PanelRutinas panelRutinas;
	private final PanelEntrenar panelEntrenar;
	private final PanelProgreso panelProgreso;
	private final PanelInBody panelInBody;
	
	
	public VentanaPrincipal(Usuario usuario,  Servicios servicios){
		
		super("Gym Tracker - " + usuario.getNombre());
		
		
		this.usuarioActual= usuario;
		this.servicios = servicios;
		
		
		
		panelUsuario = new PanelUsuario(usuario, servicios.getUsuarios(), servicios.getRutinas(), servicios.getInBody());
		panelRutinas = new PanelRutinas(usuario,servicios.getRutinas(), servicios.getEjercicios());
		panelEntrenar = new PanelEntrenar(usuario, servicios.getSesiones(), servicios.getRutinas(), servicios.getRecords());
		panelProgreso = new PanelProgreso(usuario, servicios.getRecords(), servicios.getEjercicios());
		panelInBody = new PanelInBody(usuario);
		
		pestanas = new JTabbedPane();
		
		pestanas.addTab("Usuario", panelUsuario);
		pestanas.addTab("Rutinas", panelRutinas);
		pestanas.addTab("Entrenar", panelEntrenar);
		pestanas.addTab("Progreso", panelProgreso);
		pestanas.addTab("InBody", panelInBody);
		
		//Al cambiar de pestaña, el panel nuevo vuelve a leer los servicios 
		
		pestanas.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				PanelPestana actual  = (PanelPestana) pestanas.getSelectedComponent();
				
				if(actual!=null) {
					actual.actualizar();
				}
			}
			
		});
		
		
		setJMenuBar(crearMenu());
		add(pestanas);
		setSize(900,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		
	}
	
	private JMenuBar crearMenu() {
		
		JMenuItem itemCerrar = new JMenuItem("Cerrar sesión");
		itemCerrar.setActionCommand(CERRAR_SESION);

		itemCerrar.addActionListener(this);
		
		JMenu menuCuenta = new JMenu("Cuenta");
		menuCuenta.add(itemCerrar);
		
		JMenuBar barra = new JMenuBar();
		barra.add(menuCuenta);
		return barra;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(CERRAR_SESION.equals(e.getActionCommand())) {
			cerrarSesion();
		}
		
	}
	
	private void cerrarSesion() {
		
		SesionEntrenamiento activa = servicios.getSesiones().getSesionActiva();
		boolean entrenando = activa != null && activa.getEstado() == EstadoSesion.EN_CURSO && activa.getUsuario().equals(usuarioActual);
		
		if(entrenando) {
			int respuesta = JOptionPane.showConfirmDialog(this, "Tienes un entrenamiento en cuerso. ¿Quieres finalizarlo y cerrar sesión?", "Cerrar sesión", JOptionPane.YES_OPTION );
		
			if(respuesta != JOptionPane.YES_NO_OPTION) {
				return;
			}
			
			try {
				servicios.getSesiones().finalizarSesion();
				
			} catch(GymTrackerException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "No se puedo finalizar", JOptionPane.ERROR_MESSAGE);
				return;
			}
		
		
		}
		
		new VentanaLogin(servicios).setVisible(true);
		
		dispose();
		
	}
	

	
	
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	

}
