package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.gymtracker.algorithms.OrdenarPorInsercion;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.DialogoNuevaRutina;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.Servicios;

public class PanelRutinas extends PanelPestana implements ActionListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	
	
	
	public static final String NUEVA_RUTINA = "NUEVA_RUTINA";
	
	private final Usuario usuario;
	private final RutinaService rutinaService;
	private final EjercicioService ejercicioService;
	
	
	private final ModeloRutinas modelo;
	private final JTextArea areaDetalle;
	private final JTable tabla;
	
	public PanelRutinas(Usuario usuario, RutinaService rutinaService, EjercicioService ejercicioService) {
		super();
		this.usuario = usuario;
		this.rutinaService = rutinaService;
		this.ejercicioService = ejercicioService;
		
		
		setLayout(new BorderLayout(0,12));
		setBackground(Tema.FONDO);
		setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
		
		
		//Título y botón de arriba
		JLabel titulo = new JLabel("Mis rutinas");
		titulo.setFont(new Font("SansSerrig", Font.BOLD, 22));
		titulo.setForeground(Tema.TEXTO);
		
		JButton botonNueva = new JButton("+ Nueva rutina");
		botonNueva.setActionCommand(NUEVA_RUTINA);
		botonNueva.addActionListener(this);
		
		
		JPanel encabezado = new JPanel(new BorderLayout());
		encabezado.setOpaque(false);
		encabezado.add(titulo, BorderLayout.WEST);
		encabezado.add(botonNueva, BorderLayout.EAST);
		
		
		//Centro tabla 
		modelo = new ModeloRutinas();
		tabla = new JTable(modelo);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.setRowHeight(28);
		tabla.setBackground(Tema.CAMPO);
		tabla.setForeground(Tema.TEXTO);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getSelectionModel().addListSelectionListener(this);
		
		JScrollPane scrollTabla = new JScrollPane(tabla);
		scrollTabla.getViewport().setBackground(Tema.CAMPO);
		
		//Detalle de la rutina seleccionada
		
		areaDetalle = new JTextArea(6,20);
		areaDetalle.setEditable(false);
		areaDetalle.setBackground(Tema.TARJETA);
		areaDetalle.setForeground(Tema.TEXTO);
		areaDetalle.setFont(new Font("SansSerif", Font.PLAIN, 148));
		JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
		scrollDetalle.setBorder(BorderFactory.createTitledBorder("Detalle de la rutina seleccionada"));
		scrollDetalle.setOpaque(false);
		
		add(encabezado, BorderLayout.NORTH);
		add(scrollTabla, BorderLayout.CENTER);
		add(scrollDetalle, BorderLayout.SOUTH);
		
		actualizar();
	
	}
	
	
	@Override 
	public void actualizar() {
		
		List<Rutina> rutinas = rutinaService.listarRutinas(usuario);
		OrdenarPorInsercion.ordenar(rutinas, new Comparator<Rutina>() {
			
			@Override
			public int compare(Rutina a, Rutina b) {
				return a.getDiaSemana().ordinal() - b.getDiaSemana().ordinal();
		}
		
	});
		
		modelo.setRutinas(rutinas);
		areaDetalle.setText(" ");
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) {
			
			return;
		}
		
		int fila = tabla.getSelectedRow();
		if(fila<0){
			
			areaDetalle.setText("");
			return;
		}
		
		Rutina rutina = modelo.getRutina(fila);
		StringBuilder texto = new StringBuilder();
		for(RutinaEjercicio re: rutinaService.obtenerEjercicios(rutina)) {
			
			texto.append(re.getOrdenEnRutina()).append(". ")
			.append(re.getEjercicio().getNombre()).append(" -")
			.append(re.getSetsObjetivo()).append(" sets x")
			.append(re.getObjetivo()).append(" ")
			.append(re.getEjercicio().getUnidadObjetivo()).append("\n");
			
			
		}
		areaDetalle.setText(texto.toString());
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(NUEVA_RUTINA.equals(e.getActionCommand())) {
			DialogoNuevaRutina dialogo = new DialogoNuevaRutina(SwingUtilities.getWindowAncestor(this), usuario, rutinaService, ejercicioService);
			actualizar();
		}
		
	}
	
	private class ModeloRutinas extends AbstractTableModel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private final String[] columnas = {"Nombre", "Día", "Ejercicios"};
		private List<Rutina> rutinas = new ArrayList<Rutina>();
		
		void setRutinas(List<Rutina> rutinas) {
			
			this.rutinas = rutinas;
			fireTableDataChanged();
		}
		
		Rutina getRutina(int fila) {
			
			return rutinas.get(fila);
		}
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return rutinas.size();
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnas.length;
		}
		
		@Override 
		public String getColumnName(int columna) {
			return columnas[columna];
		}

		@Override
		public Object getValueAt(int fila, int columna) {
			Rutina r = rutinas.get(fila);
			switch(columna) {
			
			case 0:
				return r.getNombre();
			case 1:
				return r.getDiaSemana();
			default: return rutinaService.obtenerEjercicios(r).size();
				
				}
					
		}
		
		
	}

	
	
	
}
