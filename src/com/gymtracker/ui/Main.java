package com.gymtracker.ui;


import javax.swing.SwingUtilities;

import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.service.EjercicioService;
import com.gymtracker.service.RutinaService;
import com.gymtracker.service.SesionService;
import com.gymtracker.service.UsuarioService;

public class Main {
	
	public static void main(String[] args) {
		
		final UsuarioService usuarioService = new UsuarioService();
		final EjercicioService ejercicioService = new EjercicioService();
		final RutinaService rutinaService = new RutinaService();
		final SesionService sesionService = new SesionService(rutinaService);
		
		try {
			ejercicioService.cargarCatalogo("datos/ejercicios_predefinidos.txt");
		} catch (GymTrackerException e) {
			
			System.out.println("No se cargó el catálogo:" + e.getMessage());
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override 
			public void run() {
		
				new VentanaLogin(usuarioService, ejercicioService, rutinaService, sesionService)
						.setVisible(true);
			}
		});
	}

}
