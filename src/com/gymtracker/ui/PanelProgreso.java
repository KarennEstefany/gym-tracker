package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.gymtracker.algorithms.BusquedaLineal;
import com.gymtracker.algorithms.OrdenarPorInsercion;
import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.RecordService;
import com.gymtracker.structures.TablaHash;

/**
 * Pestaña "Progreso". Tiene dos columnas:
 * <ul>
 * <li>Récords personales (R11 y R12): el usuario elige un ejercicio de peso y ve su mayor peso levantado;
 * además puede escribir un peso y consultar el máximo de repeticiones que logró con él.</li>
 * <li>Volumen semanal (R13): cuántos sets hizo en cada grupo muscular durante una semana, con una barra
 * por grupo. Se puede navegar a la semana anterior y a la siguiente.</li>
 * </ul>
 * El panel no calcula nada: todo lo pide a RecordService y solo se encarga de mostrarlo.
 */
public class PanelProgreso extends PanelPestana implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EJERCICIO_CAMBIADO = "EJERCICIO_CAMBIADO";//? porque _?
	private static final String CONSULTAR_REPS = "CONSULTAR_REPS";
	private static final String SEMANA_ANTERIOR ="SEMANA_ANTERIOR";
	private static final String SEMANA_ACTUAL ="SEMANA_ACTUAL";
	private static final String SEMANA_SIGUIENTE ="SEMANA_SIGUIENTE";
	
	private static final int ANCHO_CAMPO =280;
	private static final int LARGO_MAX_BARRA =16; //Barra mas larga (para mostrar volumen semanal)
	private static final String SIN_REGISTROS ="Sin registros";
	//Formatea la fecha en texto Ej 26 oct 2026.
	private static final DateTimeFormatter FORMATO_FECHA =DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es-CO"));//Se piden los meses en espaniol de colombia
	
	private final Usuario usuario;
	private final RecordService recordService;
	private final EjercicioService ejercicioService;
	
	private LocalDate semana = LocalDate.now(); //?Porque now
	
	//Elementos de la columna izquiera (de records)
	private final JComboBox<Ejercicio> comboEjercicio;
	private final JLabel valorRecordPeso;
	private final JTextField campoPesoConsultado;
	private final JLabel valorRecordReps;
	
	//Elementos de la columna derecha (el volumen semanal)
	private final JLabel valorSemana;
	private final JPanel listaVolumen;
	private final JLabel valorTotalSets;
	

	public PanelProgreso(Usuario usuario, RecordService recordService, EjercicioService ejercicioService) {
		
		super();
		this.usuario = usuario;
		this.recordService= recordService;
		this.ejercicioService = ejercicioService;
		
		
		//===========Inicializacion de los elementos de la columna izquierda, los record personales ===========/////
		comboEjercicio = new JComboBox<Ejercicio>();
		Tema.estilizarCombo(comboEjercicio, ANCHO_CAMPO);
		
		//Anade un comando propio al ActionEvent que genera JComboBox para distiguirlo de los botones de actionPerformed
		comboEjercicio.setActionCommand(EJERCICIO_CAMBIADO);
		comboEjercicio.addActionListener(this);
		
		
		valorRecordPeso =crearValor(SIN_REGISTROS);
		campoPesoConsultado = new JTextField();
		Tema.estilizarCampo(campoPesoConsultado, ANCHO_CAMPO);
		valorRecordReps = crearValor("-");
		
		//Creamos el boton primario y le cambiamos el tamanio porque la columna es mas angosta
		JButton botonConsultar = Tema.crearBotonPrimario("Consultar repeticiones", CONSULTAR_REPS, this);
		botonConsultar.setPreferredSize(new Dimension(ANCHO_CAMPO, 42));;
		botonConsultar.setMaximumSize(new Dimension(ANCHO_CAMPO, 42));
		
		JPanel columnaRecords = crearColumna("Récords Personales");
		agregarCampo(columnaRecords, "Ejercicio", comboEjercicio);
		agregarCampo(columnaRecords, "Mayor peso levantado", valorRecordPeso);
		agregarCampo(columnaRecords, "Peso a consultar (kg)", campoPesoConsultado);
		columnaRecords.add(botonConsultar);
		columnaRecords.add(Box.createRigidArea(new Dimension(0,14)));
		agregarCampo(columnaRecords, "Mayor cantidad de repeticiones con ese peso", valorRecordReps);
		
		ImageIcon motivacion = new ImageIcon("data/imagenMotivacion.jpg");
		Image imagenEscalada = motivacion.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		ImageIcon motivacionFinal = new ImageIcon(imagenEscalada);
		JLabel labelImagen = new JLabel(motivacionFinal);
		JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelImagen.setOpaque(false);
		panelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelImagen.add(labelImagen);
		columnaRecords.add(panelImagen);
		
		
		//===================Inicializacion de los elementos de la columna derecha, el volumen semanal del usuario==========/////
		
		valorSemana = crearValor("-");
		valorTotalSets= crearTextoSuave("-");
		listaVolumen= new JPanel();
		listaVolumen.setLayout(new BoxLayout(listaVolumen, BoxLayout.Y_AXIS));
		listaVolumen.setOpaque(false);
		listaVolumen.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel filaSemanas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		filaSemanas.setOpaque(false);
		filaSemanas.setAlignmentX(Component.LEFT_ALIGNMENT);
		filaSemanas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		filaSemanas.add(crearBotonSecundario("← S.Anterior", SEMANA_ANTERIOR, 108));//
		filaSemanas.add(crearBotonSecundario("S.Actual", SEMANA_ACTUAL, 108));
		filaSemanas.add(crearBotonSecundario("S.Siguiente →", SEMANA_SIGUIENTE, 110));
		
		JPanel columnaVolumen = crearColumna("Volumen semanal x grupo muscular");
		columnaVolumen.add(Tema.crearEtiqueta("Semana de lunes a domingo"));
		columnaVolumen.add(Box.createRigidArea(new Dimension(0,4)));
		columnaVolumen.add(valorSemana);
		columnaVolumen.add(Box.createRigidArea(new Dimension(0,8)));
		columnaVolumen.add(filaSemanas);
		columnaVolumen.add(Box.createRigidArea(new Dimension(0,14)));
		columnaVolumen.add(Tema.crearEtiqueta("Sets x grupo muscular"));
		columnaVolumen.add(Box.createRigidArea(new Dimension(0,6)));
		columnaVolumen.add(listaVolumen);
		columnaVolumen.add(Box.createRigidArea(new Dimension(0,8)));
		columnaVolumen.add(valorTotalSets);
		
		
		
		/////////===============Poner las dos columnas dentro de una tarjeta========////////////
		//?que seria una tarjeta
		JPanel columnas = new JPanel(new GridLayout(1,2,16,0));
		columnas.setOpaque(false);
		columnas.setAlignmentX(Component.LEFT_ALIGNMENT);
		columnas.add(columnaRecords);
		columnas.add(columnaVolumen);
		
		/**Creamos el titulo del panel*/
		JLabel titulo = new JLabel("Tu progreso");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,4)));
		tarjeta.add(Tema.crearEtiqueta("Aquí encuentras tu volumen semanal y récords personales"));
		tarjeta.add(Box.createRigidArea(new Dimension(0,16)));
		tarjeta.add(columnas);
		
		JPanel contenido = new JPanel(new GridBagLayout());
		contenido.setBackground(Tema.FONDO);
		contenido.add(tarjeta);
		
		JScrollPane scroll = new JScrollPane(contenido);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(Tema.FONDO);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		
		setBackground(Tema.FONDO);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		
		actualizar();
	}
	
	/////================= Carga de los datos que se muestran ============////////////////
	
	/**Actualiza el volumen de actuerdo a los sets registrados en el panel Entrenar y se llama al crear el panel y cada vez
	 * que el usuario entre a la pestana para que vea la informacion actual*/
	@Override
	public void actualizar() {
		cargarEjercicios();
		mostrarRecordPeso();
		mostrarVolumen();
	}
	/**Llena el comboEjercicio con los ejercicios de peso, mantiene el ejercicio seleccionado*/
	private void cargarEjercicios() {
		//Se castea de Object a Ejercicio, se indica que es de tipo ejercicio
		Ejercicio seleccionado = (Ejercicio) comboEjercicio.getSelectedItem();
		
		//Lista de ejercicios solo de peso
		//e -> e.getTipo() == TipoEjercicio.PESO se evalua para cada ejercicio
		List<Ejercicio> ejerciciosDePeso  = BusquedaLineal.filtrar(ejercicioService.listarEjercicios(), e -> e.getTipo() == TipoEjercicio.PESO);
		
		comboEjercicio.removeAllItems();//?Why
		for(Ejercicio e : ejerciciosDePeso) {
			comboEjercicio.addItem(e); //Se añade al combo y muestra el toString de Ejercicio
			
		}
		if(seleccionado != null) {
			comboEjercicio.setSelectedItem(seleccionado);
		}
	}
	
	private void mostrarRecordPeso() {
		Ejercicio ejercicio =(Ejercicio) comboEjercicio.getSelectedItem();
		if(ejercicio == null) { //Si no hay ejercicios de peso en el catalogo
			valorRecordPeso.setText("-");
			return;
		}
		
		Optional<SetPeso> mejorPeso = recordService.obtenerRecordPeso(usuario, ejercicio);
		//Muestra la cantidad de repeticiones del record de peso, solo si en optional hay un set de peso, si no, muestra "Sin registros"
		valorRecordPeso.setText(mejorPeso.map(s -> formatearPeso(s.getPeso()) + " kg x " + s.getRepeticiones() + " reps" + FORMATO_FECHA.format(s.getSesion().getFecha())).orElse(SIN_REGISTROS));
		
	}
	/**Muestra la cantidad maxima de repeticiones obtenidas en un peso exacto*/
	private void consultarRecordRepeticiones() {
		Ejercicio ejercicio = (Ejercicio ) comboEjercicio.getSelectedItem();
		if(ejercicio == null) {
			JOptionPane.showMessageDialog(this, "No hay ejercicios de peso para consultar", "Sin ejercicios", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			double peso = Double.parseDouble(campoPesoConsultado.getText().trim().replace(",", "."));
			Optional<SetPeso> mejorReps = recordService.obtenerRecordRepeticiones(usuario, ejercicio, peso);
			valorRecordReps.setText(mejorReps.map(s -> s.getRepeticiones() + " reps" + FORMATO_FECHA.format(s.getSesion().getFecha())).orElse(SIN_REGISTROS + " con " + formatearPeso(peso) + " kg"));
		}
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "El peso debe ser un número entero o decimal (por ejempo 50 o 52.5).", "Dato inválido", JOptionPane.WARNING_MESSAGE);
		}
		catch(GymTrackerException ex) { // peso negativo o dato invalido segun las reglas del negocio
			JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo realizar la consulta", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	/**Muestra con una barra por cada grupo muscular el volumen de la semana seleccioada*/
	private void mostrarVolumen() {
		
		LocalDate inicio = recordService.inicioSemana(semana);
		valorSemana.setText(FORMATO_FECHA.format(inicio) + "-" + FORMATO_FECHA.format(inicio.plusDays(6)));
		
		//Clave String: Grupo muscular, valor Integer: Cantidad de sets
		TablaHash<String,Integer> volumen = recordService.calcularVolumenSemanalTotal(usuario, semana);
		List<String > gruposM = volumen.listarClaves();
		
		//Se ordenan los grupos musculares ya que la en la tabla hash no tienen orden
		// Compara de mayor a menor la cantidad de sets, si son iguales (hay empate) compara alfabeticamente ignorando mayusculas
		OrdenarPorInsercion.ordenar(gruposM, Comparator.comparing((String s)->volumen.buscarValor(s)).reversed().thenComparing(String.CASE_INSENSITIVE_ORDER));
		
		int totalSets=0;
		int maximoSets = 0;
		for(String grupo : gruposM) {
			int sets = volumen.buscarValor(grupo);//?que es unboxing
			totalSets+= sets;
			
			if(sets> maximoSets) {
				maximoSets=sets;
			}
			
		}
		listaVolumen.removeAll();
		if(gruposM.isEmpty()) {
			listaVolumen.add(crearTextoSuave("No has registrado sets esta semana"));
			valorTotalSets.setText("");
		}
		else {
			for(String grupo :gruposM) {
				listaVolumen.add(crearFilaVolumen(grupo, volumen.buscarValor(grupo), maximoSets));
				listaVolumen.add(Box.createRigidArea(new Dimension(0,4)));
			}
			valorTotalSets.setText("Total: " + textoSets(totalSets));
		}
		listaVolumen.revalidate();
		listaVolumen.repaint();
	}
	/**Cambia entre semanas mostradas: -1 semana anterior, 1 semana siguiente*/
	private void cambiarSemana(int cantidadSemanas) {
		semana = semana.plusWeeks(cantidadSemanas);//devuelve una fecha nueva
		mostrarVolumen();
	}
	
	////////===========Eventos================//////////////
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		switch (event.getActionCommand()) {
		case EJERCICIO_CAMBIADO -> { //? que significa ejercicio cambiado
			mostrarRecordPeso();
			valorRecordReps.setText("-");
		}
		case CONSULTAR_REPS -> consultarRecordRepeticiones();
		case SEMANA_ANTERIOR -> cambiarSemana(-1);
		case SEMANA_SIGUIENTE -> cambiarSemana(1);
		case SEMANA_ACTUAL-> {
			semana = LocalDate.now();
			mostrarVolumen();
		}
		default -> {
			//No hace nada
		}
		}
	}
	///////////==============Formatos de texto necesitados ================/////////////////////
	
	/**100.0 se muestra como 100, los demas decimales si se muestran completos*/
	private String formatearPeso(double peso) {
		//Math.floor redondea hacia abajo, detecta enteros (si 90.0 = 90, no tiene decimales)
		return peso == Math.floor(peso) ? String.valueOf((long) peso) : String.valueOf(peso);
	}
	private String textoSets(int cantidad) {
		return cantidad == 1 ? "1 set" : cantidad + " sets";
	}
	///////////===============Metodos auxiliares para la estetica y presentacion ===================//////////////
	
	
	/**Crea una fila del grafico con: Nombre del grupo muscular, la cantidad de sets y una barra proporcional*/
			private JPanel crearFilaVolumen(String grupo, int sets, int maximo) { //?Que es int maximo
				JPanel fila = new JPanel(new BorderLayout(8,0)); //8 pixeles entre zonas
				fila.setOpaque(false);
				fila.setAlignmentX(Component.LEFT_ALIGNMENT);
				fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
				JLabel nombre = crearTextoNormal(grupo);
				nombre.setPreferredSize(new Dimension(120, 20)); //ancho fijo para que comiencen alineadas las barras
				
				
				//El grupo con mas sets tiene 16 bloques (LARGO_MAX_BARRA) y los demas grupos una barra proporcional
				//(double) convierte a decimal antes de dividir 
				int largo =Math.max(1, (int) Math.round((double) sets * LARGO_MAX_BARRA/maximo));
				
				//Repite "█" el numero de veces del largo calculado
				JLabel barra = new JLabel("█".repeat(largo));
				barra.setForeground(Tema.BOTON_PRIMARIO); //?pone el color?
				
				fila.add(nombre, BorderLayout.WEST);
				fila.add(barra, BorderLayout.CENTER);
				fila.add(crearTextoSuave(textoSets(sets)), BorderLayout.EAST);
				return fila;
			}
			
			
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
			/**Agrega la etiqueta, el componente y el campo apilados en la columna*/
			private void agregarCampo(JPanel columna, String texto, Component campo) {
				columna.add(Tema.crearEtiqueta(texto));
				columna.add(Box.createRigidArea(new Dimension(0,6)));
				columna.add(campo);
				columna.add(Box.createRigidArea((new Dimension(0,14))));
			}
			
			/**Crear un boton con un color secundario teniendo en cuenta el tema y el ancho indicado*/
			private JButton crearBotonSecundario(String texto, String comando, int ancho) { 
				JButton boton = new JButton(texto);
				boton.setActionCommand(comando);
				boton.addActionListener(this);
				boton.setBackground(Tema.BOTON_SECUNDARIO);
				boton.setForeground(Tema.TEXTO);
				boton.setOpaque(true);//Porque true?
				boton.setFocusPainted(false);
				boton.setFont(new Font ("SansSerif", Font.BOLD, 12));
				boton.setPreferredSize(new Dimension(ancho, 34));
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
