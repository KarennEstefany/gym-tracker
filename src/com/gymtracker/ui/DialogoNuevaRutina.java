package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JTextField;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.ItemRutina;
import com.gymtracker.service.RutinaService;

public class DialogoNuevaRutina extends JDialog implements ActionListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String AGREGAR ="AGREGAR";
private static final String QUITAR = "QUITAR";
	private static final String GUARDAR = "GUARDAR";
	
	private static final String CANCELAR = "CANCELAR";
	
	private static final String CAMBIO_EJERCICIO = "CAMBIO_EJERCICIO";
	
	private final Usuario usuario;
	private final RutinaService rutinaService;
	
	
	private final JTextField campoNombre;
	private final JComboBox<DiaSemana> comboDia;
	private final JComboBox<Ejercicio> comboEjercicio;
	private final JTextField campoSets;
	private final JTextField campoObjetivo;
	private final JLabel etiquetaObjetivo;
	private final JList<String> listaVisible;
	
	
	//Dos listas paralelas, la que se ve y la que se manda al servicio 
	
	private final DefaultListModel<String> modeloLista = new DefaultListModel<String>();
	
	private final List<ItemRutina> items = new ArrayList<ItemRutina>();

	
	
	

	public DialogoNuevaRutina(Window propietario, Usuario usuario, RutinaService rutinaService, EjercicioService ejercicioService ) {
		super(propietario, "Nueva Rutina", ModalityType.APPLICATION_MODAL);
		this.usuario = usuario;
		this.rutinaService = rutinaService;
		
		
		campoNombre = new JTextField(20);
		
		comboDia = new JComboBox<DiaSemana>(DiaSemana.values());
		comboDia.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID =1L;
			@Override 
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean foco) {
				
				if(valor instanceof DiaSemana) {
				
				setText(((DiaSemana)valor).getEtiqueta()); }
				
				return this;
			}
		
		});
		

	comboEjercicio = new JComboBox<Ejercicio>(
			
			new DefaultComboBoxModel<Ejercicio>(
					
					ejercicioService.listarEjercicios().toArray(new Ejercicio[0])));
	comboEjercicio.setActionCommand(CAMBIO_EJERCICIO);
	comboEjercicio.addActionListener(this);
	
	campoSets = new JTextField(5);
	campoObjetivo = new JTextField(5);
	etiquetaObjetivo = new JLabel("Objetivo:");
	actualizarEtiquetaObjetivo();
	
		
	listaVisible = new JList<String>(modeloLista);
	
	//Datos de la rutina 
	JPanel datos = new JPanel(new GridLayout(2,2,8,8));
	datos.add(new JLabel("Nombre:"));
	datos.add(campoNombre);
	datos.add(new JLabel("Día:"));
	datos.add(comboDia);
	
	//fila para agregar un ejercicio
	JPanel agregar = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
	agregar.add(comboEjercicio);
	agregar.add(new JLabel("Sets:"));
	agregar.add(campoSets);
	agregar.add(etiquetaObjetivo);
	agregar.add(campoObjetivo);
	agregar.add(crearBoton("Agregar", AGREGAR));
	
	JPanel centro = new JPanel(new BorderLayout(0,6));
	centro.add(agregar, BorderLayout.NORTH);
	centro.add(new JScrollPane(listaVisible), BorderLayout.CENTER);
	centro.add(crearBoton("Quitar seleccionado", QUITAR), BorderLayout.SOUTH);
	
	JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	botones.add(crearBoton("Cancelar", CANCELAR));
	botones.add(crearBoton("Guardar rutina", GUARDAR));
	
	JPanel contenido = new JPanel(new BorderLayout(0,10));
	contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(14,14,14,14));
	contenido.add(datos, BorderLayout.NORTH);
	contenido.add(centro, BorderLayout.CENTER);
	contenido.add(botones, BorderLayout.SOUTH);
	
	setContentPane(contenido);
	setSize(640, 480);
	setLocationRelativeTo(propietario);
	
	
	
	}

	
	private JButton crearBoton(String texto, String comando) {
		
		JButton boton = new JButton(texto);
		boton.setActionCommand(comando);
		boton.addActionListener(this);
		return boton;
	}
	
	private void actualizarEtiquetaObjetivo() {
		
		Ejercicio e = (Ejercicio) comboEjercicio.getSelectedItem();
		if(e == null) {
			
			etiquetaObjetivo.setText("Objetivo:");
		} else {
			
			etiquetaObjetivo.setText("Objetivo (" + e.getUnidadObjetivo() + "):");
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(CAMBIO_EJERCICIO.equals(comando)) {
			actualizarEtiquetaObjetivo();
		} else if(AGREGAR.equals(comando)) {
			
			agregarEjercicio();
		} else if(QUITAR.equals(comando)) {
			
			quitarSeleccionado();
		} else if(GUARDAR.equals(comando)) {
			
			guardar();
		} else if(CANCELAR.equals(comando)) {
			
			dispose();
		}
		
	}
	
	private void agregarEjercicio(){
		
		try {
			Ejercicio ejercicio  = (Ejercicio) comboEjercicio.getSelectedItem();
			
			if(ejercicio == null) {
				
				JOptionPane.showMessageDialog(this,  "No hay ejercicios en el catálogo.");
				return;
			}
			
			
			int sets = Integer.parseInt(campoSets.getText().trim());
			int objetivo = Integer.parseInt(campoObjetivo.getText().trim());
			
			items.add(new ItemRutina(ejercicio, sets, objetivo));
			modeloLista.addElement(items.size() + ". " + ejercicio.getNombre() + "-" + sets + "sets x" + objetivo + " " + ejercicio.getUnidadObjetivo() );
			campoSets.setText("");
			campoObjetivo.setText("");
		} catch(NumberFormatException ex) {
			
			JOptionPane.showMessageDialog(this, "Sets y objetivo deben ser números enteros.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
		} catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void quitarSeleccionado() {
		
		int indice = listaVisible.getSelectedIndex();
		if(indice<0) {
			return;
		}
		items.remove(indice);
		modeloLista.clear();
		int numero =1;
		for(ItemRutina item: items) {
			
			modeloLista.addElement(numero + ". " + item.getEjercicio().getNombre()+ "-" + item.getSets() + " sets x" + item.getObjetivo() + " " + item.getEjercicio().getUnidadObjetivo());
			
			numero++;
		}
	}
	
	private void guardar() {
		
		try {
			
			String nombre = campoNombre.getText().trim();
			DiaSemana dia = (DiaSemana) comboDia.getSelectedItem();
			rutinaService.crearRutinaConEjercicios(usuario, nombre, dia, items);
			dispose();
			
		} catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo guardar la rutina", JOptionPane.ERROR_MESSAGE);
		}
	}
}
