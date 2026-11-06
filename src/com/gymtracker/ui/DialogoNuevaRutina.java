package com.gymtracker.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JTextField;

import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Usuario;
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
	private final JTextField etiquetaObjetivo;
	private final JList<String> listaVisible;
	
	
	//Dos listas paralelas, la que se ve y la que se manda al servicio 
	
	private final DefaultListModel<String> modeloLista = new DefaultListModel<String>();
	
	private final List<ItemRutina> items = new ArrayList<ItemRutina>();

	
	
	

	public DialogoNuevaRutina(Window propietario, Usuario usuario, RutinaService rutinaService, JTextField campoNombre,
			JComboBox<DiaSemana> comboDia, JComboBox<Ejercicio> comboEjercicio, JTextField campoSets,
			JTextField campoObjetivo, JTextField etiquetaObjetivo, JList<String> listaVisible) {
		super(propietario);
		this.usuario = usuario;
		this.rutinaService = rutinaService;
		this.campoNombre = campoNombre;
		this.comboDia = comboDia;
		this.comboEjercicio = comboEjercicio;
		this.campoSets = campoSets;
		this.campoObjetivo = campoObjetivo;
		this.etiquetaObjetivo = etiquetaObjetivo;
		this.listaVisible = listaVisible;
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
