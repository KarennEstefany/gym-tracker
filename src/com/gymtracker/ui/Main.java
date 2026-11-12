package com.gymtracker.ui;


import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.persistencia.DatosApp;
import com.gymtracker.persistencia.Persistencia;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.InBodyService;
import com.gymtracker.service.RecordService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.Servicios;
import com.gymtracker.service.SesionService;
import com.gymtracker.service.UsuarioService;

public class Main {
	
	private static final String RUTA_DATOS = "data/gymtracker.dat";
	private static final String RUTA_CATALOGO = "data/ejercicios.txt";
	public static void main(String[] args) {
	
		//Se carga lo que está guardado o datos vacíos si es la primera vez 
		
		
		
		final DatosApp datos;
		try {
			datos = Persistencia.cargar(RUTA_DATOS);
			
		} catch (GymTrackerException e) {
			
			JOptionPane.showMessageDialog(null, e.getMessage() + "\\n Revise o borre el archivo" + RUTA_DATOS, "No se pudieron cargar los datos", JOptionPane.ERROR_MESSAGE );
			return;
			//No se abre la app, para no sobreescribir el archivo con datos vacíos
		}
		
		//Servicios sobre las listas de DatosApp
		
		 UsuarioService usuarioService = new UsuarioService(datos.getUsuarios());
		 EjercicioService ejercicioService = new EjercicioService(datos.getEjercicio());
		 RutinaService rutinaService = new RutinaService(datos.getRutinas(), datos.getEjerciciosDeRutinas());
		 SesionService sesionService = new SesionService(rutinaService, datos.getSesiones(), datos.getHistorial());
		 RecordService recordService = new RecordService(sesionService);
		 InBodyService inBodyService = new InBodyService(datos.getMediciones());
		
		 
		 final Servicios servicios = new Servicios(usuarioService, ejercicioService, rutinaService, sesionService, recordService, inBodyService );
		
		 
		 //El catálogo solo se lee la primera vez, después ya viene en el archivo
		 
		 if(ejercicioService.listarEjercicios().isEmpty()) {
			 
			 try {
				 
				 ejercicioService.cargarCatalogo(RUTA_CATALOGO);
				 
			 } catch(GymTrackerException e) {
				 
				 System.out.println("No se cargó el catálogo: " + e.getMessage());
			 }
		 }
		
		 //Guardar cuando el programa termine
		 
		 Runtime.getRuntime().addShutdownHook(new Thread() {
			 
			 @Override
			 public void run() {
				 
				 try {
					 Persistencia.guardar(datos, RUTA_DATOS);
					 
				 } catch(GymTrackerException e) {
					 System.out.println("No se pudo guardar: " + e.getMessage());
				 }
			 }
		 });
		 
		 
		SwingUtilities.invokeLater(new Runnable() {
			@Override 
			public void run() {
		
				new VentanaLogin(servicios)
						.setVisible(true);
			}
		});
	}

}
