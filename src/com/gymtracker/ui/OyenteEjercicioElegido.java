package com.gymtracker.ui;

import com.gymtracker.service.ItemRutina;

public interface OyenteEjercicioElegido {
	
	/**
	 * 
	 * Se llama cuando el usuario pulsa "Agregar". Puede lanzar GymTrackerException 
	 */
	
	void ejercicioElegido(ItemRutina item);
	
}
