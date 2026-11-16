package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.ItemRutina;
import com.gymtracker.service.RutinaService;

public class DialogoEditarRutina extends JDialog implements ActionListener, OyenteEjercicioElegido {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static final String CAMBIAR_DIA = "CAMBIAR_DIA";
	private static final String CERRAR = "CERRAR";
	
	private final Rutina rutina;
	private final RutinaService rutinaService;
	
	private final JComboBox<DiaSemana> comboDia;
	private final DefaultListModel<String> modeloLista = new DefaultListModel<String>();
	
	
	
	public DialogoEditarRutina(Window propietario, Rutina rutina, RutinaService rutinaService,EjercicioService ejercicioService) {
		super(propietario, "Editar rutina : " + rutina.getNombre(), ModalityType.APPLICATION_MODAL);
		this.rutina = rutina;
		this.rutinaService = rutinaService;
		
		
		//Combo con la etiqueta legible y el día actual seleccionado 
		comboDia = new JComboBox<DiaSemana>(DiaSemana.values());
		comboDia.setSelectedItem(rutina.getDiaSemana());
		comboDia.setRenderer(new DefaultListCellRenderer() {
			
			
			private static final long serialVersionUID = 1L;
			
			@Override 
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean foco) {
				
				super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
				if(valor instanceof DiaSemana) {
				
				setText(((DiaSemana)valor).getEtiqueta()); }
				
				return this;
			} 
			
			
		
		});
		
		JButton botonDia = new JButton("Cambiar día");
		botonDia.setActionCommand(CAMBIAR_DIA);
		botonDia.addActionListener(this);
		
		JPanel filaDia = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
		filaDia.setOpaque(false);
		filaDia.add(new JLabel("Día:"));
		filaDia.add(comboDia);
		filaDia.add(botonDia);
		
		//Ejercicios que la rutina ya tiene 
		JList<String> listaVisible = new JList<String>(modeloLista);
		
		//Selección de ejercicio nuevo
		
		PanelSeleccionEjercicio seleccion = new PanelSeleccionEjercicio(ejercicioService, this);
		
		JPanel centro = new JPanel(new BorderLayout(0,6));
		centro.setOpaque(false);
		centro.add(seleccion, BorderLayout.NORTH);
		centro.add(new JScrollPane(listaVisible), BorderLayout.CENTER);
		
		
		JButton botonCerrar = new JButton("Cerrar");
		botonCerrar.setActionCommand(CERRAR);
		botonCerrar.addActionListener(this);
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		botones.setOpaque(false);
		botones.add(botonCerrar);
		
		JPanel contenido = new JPanel(new BorderLayout(0,10));
		contenido.setOpaque(false);
		contenido.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
		contenido.add(filaDia, BorderLayout.NORTH);
		contenido.add(centro, BorderLayout.CENTER);
		contenido.add(botones, BorderLayout.SOUTH);
		
		setContentPane(contenido);
		setSize(660, 480);
		setLocationRelativeTo(propietario);
		
		refrescarLista();
	}
	
	private void refrescarLista() {
		
		modeloLista.clear();
		for(RutinaEjercicio re: rutinaService.obtenerEjercicios(rutina)) {
			
			modeloLista.addElement(re.getOrdenEnRutina() + ". " + re.getEjercicio().getNombre() + " - " + re.getSetsObjetivo() + " sets x " + re.getObjetivo() + " " + re.getEjercicio().getUnidadObjetivo());
		}
	}

	@Override
	public void ejercicioElegido(ItemRutina item) {
		
		rutinaService.agregarEjercicio(rutina, item.getEjercicio(), item.getSets(), item.getObjetivo());
		refrescarLista();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		String comando = e.getActionCommand();
		if(CAMBIAR_DIA.equals(comando)) {
			
			cambiarDia();
		} else if(CERRAR.equals(comando)) {
		
			
			dispose();
		}
		
	}
	
	private void cambiarDia() {
		
		DiaSemana nuevoDia = (DiaSemana)comboDia.getSelectedItem();
		
		try {
			rutinaService.editarRutina(rutina, nuevoDia);
			JOptionPane.showMessageDialog(this, "Día actualizado.");
			
		}catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this,  ex.getMessage(), "No se pudo cambiar el día ",
			JOptionPane.ERROR_MESSAGE);
			
			comboDia.setSelectedItem(rutina.getDiaSemana());
		}
	}

}
