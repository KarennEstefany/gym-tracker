package com.gymtracker.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.gymtracker.algorithms.OrdenarPorInsercion;
import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.MedicionInBody;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.Usuario;
import com.gymtracker.service.InBodyService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.UsuarioService;


public class PanelUsuario extends PanelPestana implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Identifica al boton dentro de actionperformer
	private static final String GUARDAR = "GUARDAR";
	private static final int ANCHO_DEL_CAMPO = 280;
	
	private final Usuario usuario;
	private final UsuarioService usuarioService;
	private final RutinaService rutinaService;
	private final InBodyService inBodyService;
	
	//Columna izquierda, donde van los datos del perfil
	private final JLabel valorDocumento;
	private final JTextField campoNombre;
	private final JTextField campoEdad;
	private final JTextField campoEstatura;
	private final JComboBox<Objetivo> comboObjetivo;
	
	//Columna derecha, donde va el resumen de entreno del usuario
	private final JLabel valorCantidadRutinas;
	private final JPanel listaRutinas;
	private final JLabel valorImc;
	
	public PanelUsuario(Usuario usuario, UsuarioService usuarioService, RutinaService rutinaService, InBodyService inBodyService) {
		super();
		this.usuario = usuario;
		this.usuarioService = usuarioService;
		this.rutinaService = rutinaService;
		this.inBodyService = inBodyService;
		
		//Se crean los componentes para modificar o leer
		valorDocumento = crearValor(""); //Se crea vacio porque el metodo exige un String, actualizar asigna el dato real
		campoNombre= new JTextField();
		campoEdad= new JTextField();
		campoEstatura= new JTextField();
		//ComboBox=Lista desplegable de valores tipo Objetivo
		comboObjetivo= new JComboBox<Objetivo>(Objetivo.values()); //.values devuelve un arreglo con todos los valores
		
		valorCantidadRutinas= crearValor("0"); //Valor inicial provisional (aun no se han contado rutinas)
		valorCantidadRutinas.setFont(new Font("SansSerif", Font.BOLD, 26));
		valorImc = crearValor("--");
		listaRutinas= new JPanel();
		listaRutinas.setLayout(new BoxLayout(listaRutinas, BoxLayout.Y_AXIS)); //Apila los componentes del panel (rutinas) en vertical
		listaRutinas.setOpaque(false);//El fondo es transparente y deja ver lo que hay detras
		listaRutinas.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Implementar el estilo de la aplicacion con los metodos del tema
		Tema.estilizarCampo(campoNombre, ANCHO_DEL_CAMPO);
		Tema.estilizarCampo(campoEdad, ANCHO_DEL_CAMPO);
		Tema.estilizarCampo(campoEstatura, ANCHO_DEL_CAMPO);
		Tema.estilizarCombo(comboObjetivo, ANCHO_DEL_CAMPO); 
		
		//Crear la columna izquierda de los datos personales
		JPanel columnaDatos= crearColumna("Datos Personales");
		agregarCampo(columnaDatos, "Documento de identidad", valorDocumento, "El documento no se puede modificar");
		agregarCampo(columnaDatos, "Nombre", campoNombre, null);
		agregarCampo(columnaDatos, "Edad", campoEdad, null);
		agregarCampo(columnaDatos, "Estatura(m)", campoEstatura, null);
		agregarCampo(columnaDatos, "Objetivo", comboObjetivo, null);
		
		
		//Crear la columna derecha con el resumen de la informacion de rutinas del usuario
		
		JPanel columnaResumen= crearColumna("Resumen");
		agregarCampo(columnaResumen, "Rutinas activas", valorCantidadRutinas, null);
		columnaResumen.add(listaRutinas);
		columnaResumen.add(Box.createRigidArea(new Dimension(0,14)));
		agregarCampo(columnaDatos, "IMC (Indice de masa corporal)", valorImc, "Se calcula con el ultimo peso registrado en la seccion Inbody");
		
		
		//La dos columnas se ponen lado a lado en el panel (1 fila, 2 columnas)
		JPanel columnas = new JPanel(new GridLayout(1,2,16,0)); // agrupa las dos columnas
		columnas.setOpaque(false); //El fondo es transparente y deja ver lo que hay detras  (la lista de rutinas)
		columnas.setAlignmentX(Component.LEFT_ALIGNMENT);
		columnas.add(columnaDatos);
		columnas.add(columnaResumen);
		
		//Crear el boton centrado dentro de una fila para actualizar el perfil
		
		//this es el PanelUsuario
		JButton botonGuardar = Tema.crearBotonPrimario("Guardar cambios", GUARDAR, this); // cuando hagan clic en el boton, se avisa al panel usuario para que guarge y actualice los datos
		JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));//Se acomodan los elementos en fila y se centran
		filaBoton.setOpaque(false);
		filaBoton.setAlignmentX(Component.LEFT_ALIGNMENT);
		filaBoton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46)); //La fila puede ocupar todo el ancho pero solo 46 px de alto
		filaBoton.add(botonGuardar);
		
		//Creacion de la tarjeta principal
		JLabel titulo = new JLabel("Mi perfil");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
		titulo.setForeground(Tema.TEXTO);
		titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Tema.TARJETA);
		tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 36,24, 36));
		tarjeta.add(titulo);
		tarjeta.add(Box.createRigidArea(new Dimension(0,4)));
		tarjeta.add(Tema.crearEtiqueta("Consulta y actualiza tus datos"));
		tarjeta.add(Box.createRigidArea(new Dimension(0, 16)));
		tarjeta.add(columnas);
		tarjeta.add(Box.createRigidArea(new Dimension(0,16)));
		tarjeta.add(filaBoton);
		
		//Crear barra de desplazamiento con JScrollPanel
		
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
		
		actualizar();// se llenan los componentes con los datos actuales del usuario
		
		
	}
	
	////================ Metodos para que el panel muestre datos============/////////////
	
	@Override
	public void actualizar() { //Sobreescribe el metodo actualizar de panel pestana
		valorDocumento.setText(usuario.getDocumento());
		campoNombre.setText(usuario.getNombre());
		campoEdad.setText(String.valueOf(usuario.getEdad()));
		campoEstatura.setText(String.valueOf(usuario.getEstatura()));
		comboObjetivo.setSelectedItem(usuario.getObjetivo());
		mostrarRutinas();
		
		//aquí se muestra el IMC de la última medición.
		//el panel solo muestra lo que le devuelve el service.
		Optional<MedicionInBody> ultimaMedicion = inBodyService.obtenerUltimaMedicion(usuario);
		
		valorImc.setText(ultimaMedicion.map(m-> String.format("%.1f", m.getImc())).orElse("-"));
		
	}
	
	private void mostrarRutinas() {
		List<Rutina> rutinas = rutinaService.listarRutinas(usuario); //Entrega una copia, no la lista interna
		//Ordena la lista de rutinas luego de compararlas por el dia de la semana
		OrdenarPorInsercion.ordenar(rutinas, Comparator.comparing(Rutina::getDiaSemana)); //Rutina::getDiaSemana abreviatura de lamba
		valorCantidadRutinas.setText(String.valueOf(rutinas.size()));
		
		listaRutinas.removeAll();//Borra todos los componentes para que no se dupliquen al actualizar
		
		if(rutinas.isEmpty()) {
			listaRutinas.add(crearTextoSuave("Aun no has creado rutinas"));
			
		} else {
			for( Rutina rutina : rutinas) {
				listaRutinas.add(crearTextoNormal(formatearDia(rutina.getDiaSemana()) + " .. "  + rutina.getNombre()));
				listaRutinas.add(Box.createRigidArea(new Dimension(0,4)));
			}
		}
		
		listaRutinas.revalidate();// recalcula posiciones y tamanios
		listaRutinas.repaint(); //Vuelve a dibujar (estilizar)
	}
	
	
	/////////==========Eventos ===============/////////////
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (GUARDAR.equals(e.getActionCommand())) {
	        guardar();
	    }
	}
	
	private void guardar() {
		try {
			String nombre= campoNombre.getText().trim();
			//Los numeros entran como String, hay que convertirlos a numero
			int edad = Integer.parseInt(campoEdad.getText().trim());
			double estatura = Double.parseDouble(campoEstatura.getText().trim());
			Objetivo objetivo = (Objetivo) comboObjetivo.getSelectedItem(); //Realiza el casting del objeto generico que devuelve getSelectedItem(), le dice que es un Objetivo
			
			//La validacion de los datos la realiza el service antes de actualizar
			usuarioService.actualizarPerfil(usuario, nombre, edad, estatura, objetivo);
			
			actualizar();
			
			JOptionPane.showMessageDialog(this, "Datos guardados correctamente", "Perfil", JOptionPane.INFORMATION_MESSAGE); //?Que es
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "La edad debe ser un numero entero, la estatura un decimal en metros (ej. 1.68)", "Datos Invalidos", JOptionPane.WARNING_MESSAGE);
		} catch(GymTrackerException e) {
			JOptionPane.showMessageDialog(this,e.getMessage(), "No se pudieron guardar los datos", JOptionPane.ERROR_MESSAGE);//? diferencia entre los 3 
		}
		
	}
	
	
	/////////////////Metodos auxiliares para la estetica y presentacion///////////////
	
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
	
	private void agregarCampo (JPanel columna, String texto, Component campo, String textoAyuda) {
		columna.add(Tema.crearEtiqueta(texto));
		columna.add(Box.createRigidArea(new Dimension(0,6)));
		columna.add(campo);
		
		if(textoAyuda != null) {
			columna.add(Box.createRigidArea(new Dimension(0,4)));
			JLabel textoAnadirAyuda = Tema.crearEtiqueta(textoAyuda);
			textoAnadirAyuda.setFont(new Font("SansSerif", Font.PLAIN, 12));
			columna.add(textoAnadirAyuda);
		}
		columna.add(Box.createRigidArea(new Dimension(0,14)));
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
	
	
	/**  Permite que LUNES se muestre como Lunes*/
	private String formatearDia(DiaSemana dia) {
		String nombreDia = dia.name();
		return nombreDia.charAt(0) + nombreDia.substring(1).toLowerCase();// Muestra la primera letra como mayuscula y el resto como minuscula
	}

}
