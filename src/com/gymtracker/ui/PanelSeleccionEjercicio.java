package com.gymtracker.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.ItemRutina;

public class PanelSeleccionEjercicio extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static final String AGREGAR  = "AGREGAR";
	private static final String NUEVO_EJERCICIO = "NUEVO_EJERCICIO";
	private static final String CAMBIO_EJERCICIO = "CAMBIO_EJERCICIO";
	
	private final EjercicioService ejercicioService;
	private final OyenteEjercicioElegido oyente;
	
	private final JComboBox<Ejercicio> comboEjercicio;
	private final JTextField campoSets;
	private final JTextField campoObjetivo;
	private final JLabel etiquetaObjetivo;
	
	
	
	public PanelSeleccionEjercicio(EjercicioService ejercicioService, OyenteEjercicioElegido oyente) {
		
		this.ejercicioService = ejercicioService;
		this.oyente = oyente;
		
		comboEjercicio = new JComboBox<Ejercicio>();
		comboEjercicio.setActionCommand(CAMBIO_EJERCICIO);
		campoSets = new JTextField(5);
		campoObjetivo = new JTextField(5);
		etiquetaObjetivo = new JLabel("Objetivo");
		
		JPanel filaEjercicio = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,4));
		filaEjercicio.setOpaque(false);
		filaEjercicio.add(comboEjercicio);
		filaEjercicio.add(crearBoton("+ Nuevo ejercicio", NUEVO_EJERCICIO));
		
		JPanel filaDatos = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4 ));
		filaDatos.setOpaque(false);
		filaDatos.add(new JLabel("Sets:"));
		filaDatos.add(campoSets);
		filaDatos.add(etiquetaObjetivo);
		filaDatos.add(campoObjetivo);
		filaDatos.add(crearBoton("Agregar", AGREGAR));
		
		setOpaque(false);
		setLayout(new GridLayout(2,1,0,4));
		add(filaEjercicio);
		add(filaDatos);
		
		recargar(null);
		
	}

	private JButton crearBoton(String texto, String comando) {
		
		JButton boton = new JButton(texto);
		boton.setActionCommand(comando);
		boton.addActionListener(this);
		return boton; 
	}
	
	public void recargar(Ejercicio seleccionar) {
		
		comboEjercicio.removeActionListener(this);
		comboEjercicio.setModel(new DefaultComboBoxModel<Ejercicio>(
				ejercicioService.listarEjercicios().toArray(new Ejercicio[0])));
		if(seleccionar != null) {
			
			comboEjercicio.setSelectedItem(seleccionar);
		}
		
		comboEjercicio.addActionListener(this);
		actualizarEtiquetaObjetivo();
				
	}
	
	private void actualizarEtiquetaObjetivo() {
		
		Ejercicio e = (Ejercicio) comboEjercicio.getSelectedItem();
		if(e ==null) {
			
			etiquetaObjetivo.setText("Objetivo: ");
			
		} else {
			
			etiquetaObjetivo.setText("Objetivo (" + e.getUnidadObjetivo());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(CAMBIO_EJERCICIO.equals(comando)) {
			
			actualizarEtiquetaObjetivo();
			
		}else if(NUEVO_EJERCICIO.equals(comando)){
			abrirDialogoNuevoEjercicio();
			
		} else if( AGREGAR.equals(comando)) {
			
			agregar();
		}
		
	}
	
	private void abrirDialogoNuevoEjercicio() {
		
		DialogoNuevoEjercicio dialogo  = new DialogoNuevoEjercicio(
				
				SwingUtilities.getWindowAncestor(this), ejercicioService, null);
		
		dialogo.setVisible(true);
		
		Ejercicio creado = dialogo.getEjercicioCreado();
		if(creado != null) {
			
			recargar(creado);
		}
				
	}
	
	private void agregar() {
		
		Ejercicio ejercicio =  (Ejercicio) comboEjercicio.getSelectedItem();
		if(ejercicio == null) {
			
			JOptionPane.showMessageDialog(this, "No hay ejercicios en el catálogo");
			return;
		}
		
		try {
			
			int sets = Integer.parseInt(campoSets.getText().trim());
			int objetivo  =  Integer.parseInt(campoObjetivo.getText().trim());
			
			oyente.ejercicioElegido(new ItemRutina(ejercicio, sets, objetivo));
			
			campoSets.setText("");  // solo se limpia si el oyente aceptó el ejercicio
			campoObjetivo.setText("");
			
			
		} catch (NumberFormatException ex) {
			
			JOptionPane.showMessageDialog(this, "Sets y objetivo deben ser números enteros", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
			
		} catch (GymTrackerException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
			
		}
	}
	
	

}
