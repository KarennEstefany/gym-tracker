package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.DuracionInvalidaException;

class SetTiempoTest {
	
	

	private Usuario usuario;
	private Rutina rutina;
	private SesionEntrenamiento sesion;
	private Ejercicio ejercicio;
	
	

	

	@BeforeEach
	void setUp(){
		
		//Arrange común a todas las pruebas: construir los objetos que 
		//SetPeso necesita 
		
		usuario  = new Usuario (1, "123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
		rutina = new Rutina (1, "Día de pierna", usuario, DiaSemana.LUNES);
		sesion = new SesionEntrenamiento(1, java.time.LocalDate.now(), usuario, rutina);
		ejercicio = new Ejercicio(1, "PL-01", "Plancha", "Abdomen", "Core", TipoEjercicio.TIEMPO);
		
		//Aquí se van a crear las sesiones de entrenamiento en curso
		
		
	}
	
	@Test
	void calcularVolumen_devuelveDuracionEnSegundos() {
		
		//Arrange
		SetTiempo set = new SetTiempo(1,sesion, ejercicio, 2, 8, 3,30);
		//Act 
		double volumen = set.calcularVolumen();
		
		//Assert 
		assertEquals(30, volumen);
		
	
	}

	@Test
	void lanzaExcepcion_DuracionInvalidaException() {
		
		assertThrows(DuracionInvalidaException.class, () -> {
			new SetTiempo(1,sesion, ejercicio, 3, 7, 3,-1);
			
		});
		
		
		
	}
	
	@Test
	void lanzaExcepcion_siDuracionEsCero() {
	    assertThrows(DuracionInvalidaException.class, () -> {
	        new SetTiempo(1, sesion, ejercicio, 3, 7, 3, 0);
	    });
	}

}
