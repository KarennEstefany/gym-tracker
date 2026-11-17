package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.MedicionInBody;
import com.gymtracker.model.Usuario;
import com.gymtracker.model.VariacionInBody;
import com.gymtracker.service.InBodyService;

public class PanelInBody extends PanelPestana implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String REGISTRAR  = "REGISTRAR";
	private static final String COMPARAR = "COMPARAR";
	
	
	private final Usuario usuario;
	private final InBodyService inBodyService;
	
	
	private final JTextField campoFecha;
	private final JTextField campoPeso;
	private final JTextField campoGrasa;
	private final JTextField campoMasa;
	private final JTextField campoAgua;
	
	private final ModeloMediciones modelo;
	private final JTable tabla;
	private final JTextArea areaComparacion;
	

	public PanelInBody(Usuario usuario, InBodyService inBodyService) {
		
		
		this.usuario = usuario;
		this.inBodyService = inBodyService;
		
		setLayout(new BorderLayout(0,12));
		setBackground(Tema.FONDO);
		setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
		
		
		// Arriba Título y formulario 
		JLabel titulo  = new JLabel("Composición corporal (InBody)");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		titulo.setForeground(Tema.TEXTO);
		
		campoFecha = crearCampo(LocalDate.now().toString());
		campoPeso = crearCampo("");
		campoGrasa = crearCampo("");
		campoMasa = crearCampo("");
		campoAgua = crearCampo("");
		
		
		
		JPanel campos = new JPanel(new GridLayout(2,5,12,4));
		
		campos.setOpaque(false);
		campos.add(Tema.crearEtiqueta("Fecha (AAAA-MM-DD)"));
		campos.add(Tema.crearEtiqueta("Peso (kg)")); 
		campos.add(Tema.crearEtiqueta("% grasa"));
		campos.add(Tema.crearEtiqueta("Masa muscular (kg)"));
		campos.add(Tema.crearEtiqueta("Agua corporal (L)"));
		campos.add(campoFecha);
		campos.add(campoPeso);
		campos.add(campoGrasa);
		campos.add(campoMasa);
		campos.add(campoAgua);
		
		
		JButton botonRegistrar = Tema.crearBotonPrimario("Registrar medición", REGISTRAR, this);
		botonRegistrar.setPreferredSize(new Dimension(200,44));
		botonRegistrar.setMaximumSize(new Dimension(200,44));
		
		JPanel formulario = new JPanel(new BorderLayout(16,0));
		formulario.setBackground(Tema.TARJETA);
		formulario.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
		formulario.add(campos, BorderLayout.CENTER);
		formulario.add(botonRegistrar, BorderLayout.EAST);
		
		JPanel norte = new JPanel(new BorderLayout(0,10));
		norte.setOpaque(false);
		norte.add(titulo, BorderLayout.NORTH);
		norte.add(formulario, BorderLayout.CENTER);
		
		// centro: tabla 
		
		modelo = new ModeloMediciones();
		tabla = new JTable(modelo);
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setRowHeight(28);
		tabla.setBackground(Tema.CAMPO);
		tabla.setForeground(Tema.TEXTO);
		tabla.getTableHeader().setReorderingAllowed(false);
	
		JScrollPane scrollTabla = new JScrollPane(tabla);
		scrollTabla.getViewport().setBackground(Tema.CAMPO);
		
		
		//Abajo: comparación
		
		JButton botonComparar = new JButton("Comparar seleccionadas");
				
		botonComparar.setActionCommand(COMPARAR);
		botonComparar.addActionListener(this);
				
				
		JLabel ayuda = new JLabel("Selecciona dos mediciones (Ctrl + clic) para ver la variación." );
		ayuda.setForeground(Tema.TEXTO_SUAVE);
				
				
		JPanel filaComparar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12,0));
		filaComparar.setOpaque(false);
		filaComparar.add(botonComparar);
		filaComparar.add(ayuda);
				
		areaComparacion = new JTextArea(4,20);
		areaComparacion.setEditable(false);
		areaComparacion.setBackground(Tema.TARJETA);
		areaComparacion.setForeground(Tema.TEXTO);
		areaComparacion.setFont(new Font("SansSerif", Font.PLAIN, 14));
		JScrollPane scrollComparacion = new JScrollPane(areaComparacion);
		scrollComparacion.setBorder(BorderFactory.createTitledBorder("Variación"));
		scrollComparacion.setOpaque(false);
		
		JPanel sur = new JPanel(new BorderLayout(0,8));
		sur.setOpaque(false);
		sur.add(filaComparar, BorderLayout.NORTH);
		sur.add(scrollComparacion, BorderLayout.CENTER);
		
		add(norte, BorderLayout.NORTH);
		add(scrollTabla, BorderLayout.CENTER);
		add(sur, BorderLayout.SOUTH);
		
		actualizar();
	}
	
	private JTextField crearCampo(String textoInicial) {
		
		JTextField campo = new JTextField(textoInicial);
		Tema.estilizarCampo(campo);
		campo.setPreferredSize(new Dimension(120,40));
		campo.setMaximumSize(new Dimension(120,40));
		return campo;
		
	
	}
	
	/**
	 * Vuelve a leer las mediciones del servicio - se llama al cambiar de pestaña
	 */
	
	@Override
	public void actualizar() {
		
		modelo.setMediciones(inBodyService.listarPorUsuario(usuario));
		areaComparacion.setText("");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();
		if(REGISTRAR.equals(comando)) {
			
			registrar();
		} else if(COMPARAR.equals(comando)) {
			
			comparar();
		}
		
	}
	
	private void registrar() {
		
		try {
			
			LocalDate fecha  = LocalDate.parse(campoFecha.getText().trim());
			double peso  = leerNumero(campoPeso);
			double grasa  = leerNumero(campoGrasa);
			double masa  = leerNumero(campoMasa);
			double agua  = leerNumero(campoAgua);
			
			inBodyService.registrarMedicion(usuario, fecha, peso, grasa, masa, agua);
			campoFecha.setText(LocalDate.now().toString());
			campoPeso.setText("");
			campoGrasa.setText("");
			campoMasa.setText("");
			campoAgua.setText("");
			actualizar();
		} catch (DateTimeParseException ex) {
			
			JOptionPane.showMessageDialog(this, "La fecha debe tener el formato AAAA-MM-DD, por ejemplo 2026-11-17.", "Fecha inválida",JOptionPane.WARNING_MESSAGE);
		} catch (NumberFormatException  ex) {
			
		JOptionPane.showMessageDialog(this, "Peso, grasa, masa muscular y agua deben ser números.", "Datos inválidos",JOptionPane.WARNING_MESSAGE);
		}catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	//Acepta coma o punto como separador decimal 
	
	
	private double leerNumero(JTextField campo) {
		
		return Double.parseDouble(campo.getText().trim().replace(',', '.'));
	}
	
	
	
	private void comparar() {
		
		int[] filas = tabla.getSelectedRows(); // vienen en orden ascendente
		if(filas.length!=2) {
			
			JOptionPane.showMessageDialog(this, "Selecciona exactamente dos mediciones");
			return;
		}
		
		MedicionInBody anterior = modelo.getMedicion(filas[0]);
		MedicionInBody posterior = modelo.getMedicion(filas[1]);
		
		try {
			
			VariacionInBody v = inBodyService.comparar(usuario, anterior.getFecha(), posterior.getFecha());
			areaComparacion.setText(
					
					"De " + v.getFechaAnterior() + " a " + v.getFechaPosterior() + ":\n" + "Peso: " + formatearDelta(v.getDeltaPeso()) + " kg\n"
		                    + "Grasa: " + formatearDelta(v.getDeltaGrasa()) + " puntos %\n"
		                    + "Masa muscular: " + formatearDelta(v.getDeltaMasaMuscular()) + " kg");
					
				
		}catch(GymTrackerException ex) {
			
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo comparar", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private String formatearDelta(double delta) {
        return String.format("%+.1f", delta);       // siempre con signo: +0,5 / -2,0
    }
	 
	
	
	
	
	private class ModeloMediciones extends AbstractTableModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String[] columnas = {
				"Fecha", "Peso (kg)","% grasa", "Masa muscular (kg)", "Agua (L)", "IMC"
		};
		private List<MedicionInBody> mediciones = new ArrayList<MedicionInBody>();
		
		void setMediciones(List<MedicionInBody> mediciones) {
			
			this.mediciones = mediciones;
			fireTableDataChanged();
		}
		
		MedicionInBody getMedicion(int fila) {
			
			return mediciones.get(fila);
		
		
		}
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return mediciones.size();
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
			// TODO Auto-generated method stub
			MedicionInBody m = mediciones.get(fila);
			switch(columna) {
			
			case 0:
				return m.getFecha().toString();
				
			case 1:
				return String.format("%.1f", m.getPeso());
			case 2:
				return String.format("%.1f", m.getPorcentajeGrasa());
			case 3:
				return String.format("%.1f", m.getMasaMuscular());
			case 4:
				return String.format("%.1f", m.getAguaCorporal());
			default:
				return String.format("%.1f", m.getImc());
				
			}
		}
		
		
	}
	
	
	

}
