package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.exceptions.PilaVaciaException;
import com.gymtracker.model.EstadoSesion;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.RecordService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.SesionService;

public class PanelEntrenar extends PanelPestana implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Comandos de los botones para identificarlos dentro de actionPerformed
	private static final String INICIAR = "INICIAR";
	private static final String REGISTRAR = "REGISTRAR";
	private static final String DESHACER = "DESHACER";
	private static final String FINALIZAR = "FINALIZAR";
	
	//Nombres de las cartas //? Que son las cartas?
	private static final String VISTA_PREVIA = "VISTA_PREVIA"; //El _ es necesario?
	private static final String ENTRENAR = "ENTRENAR";
	private static final String MEDIDAS_PESO = "MEDIDAS_PESO";
	private static final String MEDIDAS_TIEMPO = "MEDIDAS_TIEMPO";
	
	private static final int ANCHO_CAMPO_CORTO = 120;
	
	
	
	private final Usuario usuario;
	private final SesionService sesionService;
	private final RutinaService rutinaService;
	private final RecordService recordService;
	
	//CardLayout permite mostrar un solo componente a la vez, se intercambian por nombres
	private final CardLayout cartasEstado;
	private final JPanel contenedorEstado;
	
	//Elementos del estado 1: Vista previa
	private final JLabel valorRutinaHoy;
	private final JPanel listaEjercicios;
	private final JButton botonIniciar;
	
	//Elementos del estado 2: Entrenando
	private final JLabel valorEjercicio;
	private final JLabel valorDetalleSerie;
	private final CardLayout cartasMedidas;// "peso y repeticiones" o "duracion"
	private final JPanel panelMedidas;
	private final JTextField campoPeso;
	private final JTextField campoRepeticiones;
	private final JTextField campoDuracion;
	private final JTextField campoRpe;
	private final JTextField campoRir;
	private final JButton botonRegistrar; 
	private final JButton botonDeshacer;
	private final JButton botonFinalizar;
	
	
	public PanelEntrenar(Usuario usuario, SesionService sesionService, RutinaService rutinaService, RecordService recordService) {
		super();
		this.usuario = usuario;
		this.sesionService = sesionService;
		this.rutinaService = rutinaService;
		this.recordService = recordService;
		
		//Inicializacion de los elementos del estado 1: Vista previa
		
		valorRutinaHoy= crearValor("");
		listaEjercicios=new JPanel();
		listaEjercicios.setLayout(new BoxLayout(listaEjercicios, BoxLayout.Y_AXIS));
		listaEjercicios.setOpaque(false);
		listaEjercicios.setAlignmentX(Component.LEFT_ALIGNMENT);
		botonIniciar =Tema.crearBotonPrimario("Iniciar sesison de entrenamiento", INICIAR, this);
		
		JPanel columnaVPrevia= crearColumna("Vista previa del entreno de hoy");
		columnaVPrevia.add(Tema.crearEtiqueta("Rutina de hoy"));
		columnaVPrevia.add(Box.createRigidArea(new Dimension(0,6)));
		columnaVPrevia.add(valorRutinaHoy);
		columnaVPrevia.add(Box.createRigidArea(new Dimension(0,14))); //? que es rigid area
		columnaVPrevia.add(Tema.crearEtiqueta("Ejercicios"));
		columnaVPrevia.add(Box.createRigidArea(new Dimension(0,6)));
		columnaVPrevia.add(listaEjercicios);
		columnaVPrevia.add(Box.createRigidArea(new Dimension(0,16)));
		columnaVPrevia.add(crearFilaCentrada(botonIniciar));
		
		// Inicializacion de los elementos del estado 2: Entrenando
		
		valorEjercicio= crearValor("");
		valorEjercicio.setFont(new Font("SansSerif", Font.BOLD, 25));
		valorDetalleSerie = crearTextoSuave("");
		
		campoPeso = new JTextField();
		campoRepeticiones =  new JTextField();
		campoDuracion =  new JTextField();
		campoRpe =  new JTextField();
		campoRir =  new JTextField();
		Tema.estilizarCampo(campoPeso, ANCHO_CAMPO_CORTO);
		Tema.estilizarCampo(campoRepeticiones, ANCHO_CAMPO_CORTO);
		Tema.estilizarCampo(campoDuracion, ANCHO_CAMPO_CORTO);
		Tema.estilizarCampo(campoRpe, ANCHO_CAMPO_CORTO);
		Tema.estilizarCampo(campoRir, ANCHO_CAMPO_CORTO);
		
		//Las medidas de las cartas y el panel dependen del tipo de ejercicio
		cartasMedidas= new CardLayout();
		panelMedidas = new JPanel (cartasMedidas);
		panelMedidas.setOpaque(false);
		panelMedidas.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelMedidas.add(crearFilaDeCampos(crearCampoConEtiqueta("Peso (kg) ", campoPeso), crearCampoConEtiqueta("Repeticiones", campoRepeticiones)), MEDIDAS_PESO);
		panelMedidas.add(crearCampoConEtiqueta("Duracion (seg) ", campoDuracion), MEDIDAS_TIEMPO);
		
		JPanel columnaSerie = crearColumna("Set pendiente");
		columnaSerie.add(valorEjercicio);
		columnaSerie.add(Box.createRigidArea(new Dimension (0,6)));
		columnaSerie.add(valorDetalleSerie);
		
		JPanel columnaRegistro = crearColumna("Registrar");
		columnaRegistro.add(panelMedidas);
		columnaRegistro.add(Box.createRigidArea(new Dimension (0,14)));
		columnaRegistro.add(crearFilaDeCampos(crearCampoConEtiqueta("RPE (1-10)", campoRpe), crearCampoConEtiqueta("RIR", campoRir)));
	
		JPanel columnas  = new JPanel (new GridLayout(1, 2, 16, 0));
		columnas.setOpaque(false);
		columnas.setAlignmentX(Component.LEFT_ALIGNMENT);
		columnas.add(columnaSerie);
		columnas.add(columnaRegistro);
		
		botonRegistrar = Tema.crearBotonPrimario("Registrar set ",REGISTRAR, this);
		botonDeshacer = crearBotonSecundario("Deshacer ultimo set ", DESHACER);
		botonFinalizar = crearBotonSecundario("Finalizar serie", FINALIZAR);
		
		JPanel entrenando=new JPanel();
		entrenando.setLayout(new BoxLayout(entrenando, BoxLayout.Y_AXIS));
		entrenando.setOpaque(false);
		entrenando.add(columnas);
		entrenando.add(Box.createRigidArea(new Dimension(0,16)));
		entrenando.add(crearFilaCentrada(botonRegistrar));
		entrenando.add(Box.createRigidArea(new Dimension(0,10)));
		entrenando.add(crearFilaCentrada(botonDeshacer, botonFinalizar));
		
		//Poner los dos estados en un CardLayout//
		cartasEstado= new CardLayout();
		contenedorEstado=new JPanel(cartasEstado);
		contenedorEstado.setOpaque(false);
		contenedorEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
		contenedorEstado.add(columnaVPrevia, VISTA_PREVIA);
		contenedorEstado.add(entrenando, ENTRENAR);
		
		/**Tarjeta principal del panel */
		JLabel titulo = new JLabel("Entrenar");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,4)));
		tarjeta.add(Tema.crearEtiqueta("Registra cada serie de tu entreno"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,16)));
		tarjeta.add(contenedorEstado);
		
		JPanel contenido = new JPanel(new GridBagLayout());
		contenido.setBackground(Tema.FONDO);
		contenido.add(tarjeta);
		
		JScrollPane scroll = new JScrollPane(contenido);
		contenido.setBorder(null);
		scroll.getViewport().setBackground(Tema.FONDO);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		
		setBackground(Tema.FONDO);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		
		actualizar();
	}
	
	
	///========Elegir que pantalla mostrar==================/////////////
	/// 
	/** Se consulta en el service en que estado se encuentra el usuario y muestra la pantalla correspondiente*/
	@Override
	public void actualizar() {
		if(estaEntrenando()) {
			cartasEstado.show(contenedorEstado, ENTRENAR); //Muestra la carta con el nombre ENTRENAR
			mostrarSetPendiente();
			
		}
		else {
			cartasEstado.show(contenedorEstado, VISTA_PREVIA);
			mostrarVistaPrevia();
		}
	}
	/**El usuario esta entrenando (true) si hay una sesion activa (en curso) y es suya*/
	private boolean estaEntrenando() {
		SesionEntrenamiento activa = sesionService.getSesionActiva();
		//Retorna true si la sesion del usuario esta activa
		return activa!= null && activa.getEstado() == EstadoSesion.EN_CURSO && activa.getUsuario().equals(usuario);
	}
	
	private void mostrarVistaPrevia() {//?
		listaEjercicios.removeAll();
		boolean puedeIniciar =false;
		
		//Optional porque puede no haber rutina para hoy
		Optional<Rutina> rutinaHoy = sesionService.buscarRutinaDelDia(usuario, LocalDate.now());
		if(rutinaHoy.isEmpty()) {
			valorRutinaHoy.setText("Hoy es dia de descanso, no hay rutina");
			listaEjercicios.add(crearTextoSuave("No tienes una rutina para hoy"));
		}
		else {
			Rutina rutina = rutinaHoy.get();
			valorRutinaHoy.setText(rutina.getNombre());
			
			List<RutinaEjercicio> ejercicios = rutinaService.obtenerEjercicios(rutina);
			if(ejercicios.isEmpty()) {
				listaEjercicios.add(crearTextoSuave("La rutina de hoy no tiene ejercicios"));
				
			}
			else {
				puedeIniciar=true;
				for(RutinaEjercicio RutE : ejercicios) {
					listaEjercicios.add(crearTextoNormal(describirEjercicio(RutE)));
					listaEjercicios.add(Box.createRigidArea(new Dimension(0,4)));
				}
			}
		}
		botonIniciar.setEnabled(puedeIniciar); //el boton no se habilita sin rutinas o ejercicios
		listaEjercicios.revalidate();
		listaEjercicios.repaint();
	}
	
	private void mostrarSetPendiente() {
		if(sesionService.haySetsPendientes()){
			SetPlaneado set= sesionService.verSetPendiente(); //Mira el tope(cabeza) de la cola de sets
			TipoEjercicio tipo = set.getEjercicio().getTipo();
			valorEjercicio.setText(set.getEjercicio().getNombre());
			valorDetalleSerie.setText("Set " + set.getNumeroSet() + " , repeticiones objetivo: " + formatearRepsObjetivo(tipo, set.getObjetivo()));
		
			cartasMedidas.show(panelMedidas,  tipo == TipoEjercicio.PESO ? MEDIDAS_PESO : MEDIDAS_TIEMPO);
			botonRegistrar.setEnabled(true);
		}
		else {
			valorEjercicio.setText("Terminaste la rutina!!");
			valorDetalleSerie.setText("No quedan sets pendientes");
			botonRegistrar.setEnabled(false);
		}
	}
	
	
	////////////============Eventos=====================///////////////// //? que son los eventos
	@Override
	public void actionPerformed(ActionEvent e) {//? que hace, que es
		
		switch(e.getActionCommand()) {
		// case X-> accion a ejecutar 
		case INICIAR -> iniciar();
		case REGISTRAR -> registrar();
		case DESHACER -> deshacer();
		case FINALIZAR -> finalizar();
		default -> { //Si no es uno de los anteriores, no hace nada
			
		}
		}
		
	}
	private void iniciar() {
		try {
			sesionService.iniciarSesion(usuario);
			actualizar();
		}
		catch(GymTrackerException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo iniciar el entrenamiento", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void registrar() {
		try {
			SetPlaneado setPendiente = sesionService.verSetPendiente();
			int rpe = Integer.parseInt(campoRpe.getText().trim());
			int rir = Integer.parseInt(campoRir.getText().trim());
			
			
			if(setPendiente.getEjercicio().getTipo() == TipoEjercicio.PESO) {
				double peso = Double.parseDouble(campoPeso.getText().trim().replace(",", "."));
				int repeticiones = Integer.parseInt(campoRepeticiones.getText().trim());
				sesionService.registrarSetPeso(peso, repeticiones, rpe, rir);
			}
			else {
				int duracion = Integer.parseInt(campoDuracion.getText().trim());
				sesionService.registrarSetTiempo(duracion, rpe, rir);
			}
			limpiarCampos();
			actualizar();
			
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this,  "Revisa los datos: el peso puede tener decimales (ej. 52.5); repeticiones, duración, RPE y RIR son números enteros.", "Datos invalidos", JOptionPane.WARNING_MESSAGE);
		}
		catch(GymTrackerException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo registrar bel set", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void deshacer() {
		try {
			sesionService.deshacerUltimoSet();
			actualizar(); // el set deshecho queda como pendiente
		
		}
		catch(PilaVaciaException e) {
			//En caso de que se pulsara deshacer sin haber registrado ningun set
			JOptionPane.showMessageDialog(this, "Todavia no se ha registrado ningun set en este entreno", "Nada que deshacer", JOptionPane.INFORMATION_MESSAGE);
		} 
		catch (GymTrackerException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo deshacer", JOptionPane.ERROR_MESSAGE );
		}
	}
	private void finalizar() {
		// AVANZADO: showConfirmDialog devuelve QUÉ botón se presionó. El tipo de diálogo (YES_NO_OPTION)
				// va como argumento; el resultado se compara con YES_OPTION.
		int respuesta = JOptionPane.showConfirmDialog(this, "Quieres finalizar el entrenamiento?", "Finalizar", JOptionPane.YES_NO_OPTION);
		if(respuesta!= JOptionPane.YES_OPTION) {
			return;
		}
		try {
			//La fecha se lee antes de finalizar el entreno
			LocalDate fechaSesion = sesionService.getSesionActiva().getFecha();
			sesionService.finalizarSesion();
			actualizar();
			mostrarResumenSemanal(fechaSesion);
			
		}catch(GymTrackerException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "No se pudo finalizar el entreno", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	/** Al terminar el entreno, mmustra cuantos sets lleva la semana en cada grupo muscular
	 * Esto se calcula en record Service*/
	private void mostrarResumenSemanal(LocalDate fecha ) {
		
	}
	private void limpiarCampos() {
		campoPeso.setText("");
		campoRepeticiones.setText("");
		campoDuracion.setText("");
		campoRpe.setText("");
		campoRir.setText("");
	}
	//////============Mostrar textos==================////////////////
	
	private String describirEjercicio(RutinaEjercicio rutE) {
		TipoEjercicio tipo =rutE.getEjercicio().getTipo();
		return rutE.getOrdenEnRutina() + ". " + rutE.getEjercicio().getNombre() + " - " + rutE.getSetsObjetivo() + " sets x  " + formatearRepsObjetivo(tipo, rutE.getObjetivo());
		
	}
	
	private String formatearRepsObjetivo (TipoEjercicio tipo, int objetivo) {
		return tipo == TipoEjercicio.PESO ? objetivo + " reps " : objetivo + "seg";
	}
	
	///////////////=== Metodos auxiliares para la presentacion y la estetica =========///////////////
	
	//Crea una columna/Subtarjeta con titulo
		private JPanel crearColumna(String titulo) {
			JPanel columna = new JPanel();
			columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
			columna.setBackground(Tema.SUBTARJETA);
			columna.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
			
			JLabel encabezadoColumna = new JLabel(titulo);
			encabezadoColumna.setFont(new Font("SansSerif", Font.BOLD, 18));
			encabezadoColumna.setForeground(Tema.TEXTO);
			encabezadoColumna.setAlignmentX(Component.LEFT_ALIGNMENT);
			
			columna.add(encabezadoColumna);
			columna.add(Box.createRigidArea(new Dimension(0,14)));
			return columna;
		}
		/** Crea una etiqueta sobre un campo, agrupadas en un panel*/
		private JPanel crearCampoConEtiqueta(String texto, JTextField campo) {
			JPanel bloque = new JPanel();
			bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
			bloque.setOpaque(false);
			bloque.setAlignmentX(Component.LEFT_ALIGNMENT);
			bloque.add(Tema.crearEtiqueta(texto));
			bloque.add(Box.createRigidArea(new Dimension(0,6)));
			bloque.add(campo);
			return bloque;
		}
		/**Crea 2 componentes lado a lado (1 fila, 2 columnas) */
		private JPanel crearFilaDeCampos(Component izq, Component der) {
			JPanel fila = new JPanel(new GridLayout(1, 2, 12, 0));
			fila.setOpaque(false);
			fila.setAlignmentX(Component.LEFT_ALIGNMENT);
			fila.add(izq);
			fila.add(der);
			return fila;
		}
		/** Crear una fila con los componentes centrados, acepta uno o varios argumentos*/
		private JPanel crearFilaCentrada(Component...otros) {
			JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
			fila.setOpaque(false);
			fila.setAlignmentX(Component.LEFT_ALIGNMENT);
			fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
			for (Component componente : otros) {
				fila.add(componente);
			}
			return fila;
		}
		/**Crear un boton con un color secundario teniendo en cuenta el tema*/
		private JButton crearBotonSecundario(String texto, String comando) { //explain all
			JButton boton = new JButton(texto);
			boton.setActionCommand(comando);
			boton.addActionListener(this);
			boton.setBackground(Tema.BOTON_SECUNDARIO);
			boton.setForeground(Tema.TEXTO);
			boton.setOpaque(true);//Porque true?
			boton.setFocusPainted(false);
			boton.setFont(new Font ("SansSerif", Font.BOLD, 14));
			boton.setPreferredSize(new Dimension(230, 42));
			return boton;
		}
		/**Crea una etiqueta para mostrar un dato*/
		private JLabel crearValor(String texto) {
			JLabel valor = new JLabel(texto);
			valor.setFont(new Font("SansSerif", Font.BOLD, 18));
			valor.setForeground(Tema.TEXTO);
			valor.setAlignmentX(Component.LEFT_ALIGNMENT);
			return valor;
		}
		
		private JLabel crearTextoNormal(String texto) { //?why this method
			JLabel etiqueta = new JLabel(texto);
			etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));
			etiqueta.setForeground(Tema.TEXTO);
			etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
			return etiqueta;
		}
		private JLabel crearTextoSuave(String texto) { //?why this method
			JLabel etiqueta = new JLabel(texto);
			etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));
			etiqueta.setForeground(Tema.TEXTO_SUAVE);
			etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
			return etiqueta;
		}
		

}
