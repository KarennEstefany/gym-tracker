package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.service.EjercicioService;

public class DialogoNuevoEjercicio extends JDialog implements ActionListener{

	/**
	 * 
	 */
	
	
	
	
	private static final long serialVersionUID = 1L;

	
	private static final String GUARDAR  = "GUARDAR";
	private static final String  CANCELAR = "CANCELAR";
	
	private final EjercicioService ejercicioService;
	
	private final JTextField campoCodigo;
	private final JTextField campoNombre;
	private final JComboBox<String> comboGrupo;
	private final JComboBox<String> comboCategoria;
	private final JComboBox<TipoEjercicio> comboTipo;
	
	
	private Ejercicio ejercicioCreado; //null si el usuario canceló 
	
	
	public DialogoNuevoEjercicio(Window propietario, EjercicioService ejercicioService, String nombreInicial) {
		super(propietario, "Nuevo Ejercicio", ModalityType.APPLICATION_MODAL);
		this.ejercicioService = ejercicioService;
		this.campoCodigo = new JTextField(15);
		this.campoNombre = new JTextField(nombreInicial == null ? "": nombreInicial, 15);
		this.comboGrupo = crearComboEditable(ejercicioService.listarGruposMusculares());
		this.comboCategoria =crearComboEditable(ejercicioService.listarCategorias());
		this.comboTipo = new JComboBox<TipoEjercicio>(TipoEjercicio.values());
		JPanel formulario = new JPanel(new GridLayout(5, 2,8,10));
		
		formulario.setOpaque(false);
		formulario.add(new JLabel("Código (ej. SENT-01"));
		formulario.add(campoCodigo);
		formulario.add(new JLabel("Nombre: "));
		formulario.add(campoNombre);
		formulario.add(new JLabel("Grupo muscular: "));
		formulario.add(comboGrupo);
		formulario.add(new JLabel("Categoría: "));
		formulario.add(comboCategoria);
		formulario.add(new JLabel("Se mide por: "));
		formulario.add(comboTipo);
		
		
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		botones.setOpaque(false);
		botones.add(crearBoton("Cancelar", CANCELAR));
		botones.add(crearBoton("Guardar", GUARDAR));
		
		JPanel contenido = new JPanel(new BorderLayout(0,14));
		contenido.setOpaque(false);
		contenido.setBorder(BorderFactory.createEmptyBorder(18,20,14,20));
		contenido.add(formulario, BorderLayout.CENTER);
		contenido.add(botones, BorderLayout.SOUTH);
		
		
		setContentPane(contenido);
		setSize(460,330);
		setLocationRelativeTo(propietario);
		
	}
	
	
	private JComboBox<String> crearComboEditable(List<String> valores){
		
		JComboBox<String> combo = new JComboBox<String>(valores.toArray(new String[0]));
	
		combo.setEditable(true);
		
		return combo;
	}
	
	private JButton crearBoton(String texto, String comando) {
		
		JButton boton = new JButton(texto);
		boton.setActionCommand(comando);
		boton.addActionListener(this);
		return boton;
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(GUARDAR.equals(e.getActionCommand())) {
			
			guardar();
		} else if(CANCELAR.equals(e.getActionCommand())){
			
			dispose();
			
			
		}
		
		
		
	}
	
	
	private void guardar() {
		try {
			
			String grupo = String.valueOf(comboGrupo.getEditor().getItem()).trim();
			String categoria = String.valueOf(comboCategoria.getEditor().getItem()).trim();
			
			ejercicioCreado = ejercicioService.registrarEjercicio(campoCodigo.getText(), campoNombre.getText().trim(), grupo, categoria, (TipoEjercicio)  comboTipo.getSelectedItem());
			
			dispose();
			
			
		} catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo guardar el mensaje", JOptionPane.ERROR_MESSAGE );
			
		}
		
	}
	
	public Ejercicio getEjercicioCreado() {
		
		return ejercicioCreado;
	}
	

}
