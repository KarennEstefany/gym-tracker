package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.PesoInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;
import com.gymtracker.exceptions.RpeInvalidoException;

class SetPesoTest {
	
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
		ejercicio = new Ejercicio(1, "SENT-01", "Sentadilla", "Piernas", "Cuádriceps", TipoEjercicio.PESO);
		
		//Aquí se van a crear las sesiones de entrenamiento en curso
		
		
	}

	
	@Test
	void calcularVolumen_devuelvePesoPorRepeticiones() {
		
		//Arrange
		SetPeso set = new SetPeso(1, sesion, ejercicio, 1,8,2,80.0,10);
		//Act 
		double volumen = set.calcularVolumen();
		
		//Assert 
		assertEquals(800.0, volumen);
		
	
	}
	
	@Test 
	void constructor_lanzaExcepcion_siPesoEsNegativo() {
		
		assertThrows(PesoInvalidoException.class, () -> {
			new SetPeso(1, sesion, ejercicio, 1, 8, 2, -10.0,10);
			
		});
	}
    
	
	@Test
	void constructor_lanzaExcepcion_siRepeticionesSonCero() {
		
		assertThrows(RepeticionesInvalidasException.class, () -> {
			new SetPeso(1, sesion, ejercicio, 1, 8,2, 80.0, 0);
		});
	}
	  
	@Test 
	void constructor_lanzaExcepcionsiRpeFueraDeRango() {
		 assertThrows(RpeInvalidoException.class, () -> {
	            new SetPeso(1, sesion, ejercicio, 1, 11, 2, 80.0, 10);
	        });
	}		

}
