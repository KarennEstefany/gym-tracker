package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;

public class SetPlaneadoTest {
	private Ejercicio ejercicio;
	private SetPlaneado set;
	
	@BeforeEach
	void setUp() {
		ejercicio = new Ejercicio (4, "SENT-01", "Sentadilla Libre", "Cuadricep", "Piernas", TipoEjercicio.PESO) ;
		set = new SetPlaneado(2, ejercicio ,12);
	}
	
	////////////////Pruebas de constructor//////////////////
	@Test
	void constructorValido() {
		assertEquals(ejercicio, set.getEjercicio());
		assertEquals(2, set.getNumeroSet());
		assertEquals(12, set.getObjetivo());
	}
	@Test
	void constructorEjercicioNulo() {
		assertThrows(EjercicioInvalidoException.class, ()-> new SetPlaneado(2, null, 12));
	}
	
	@Test 
	void constructorNumeroSetCero() {
		assertThrows(NumeroSetInvalidoException.class, ()-> new SetPlaneado(0, ejercicio, 12));
	}
	
	@Test
	void constructorNumeroSetNegativo() {
		assertThrows(NumeroSetInvalidoException.class, ()-> new SetPlaneado(-2, ejercicio, 12));
	}
	@Test
	void constructorSetEncimaDelMaximo() {
		assertThrows(NumeroSetInvalidoException.class, ()-> new SetPlaneado(35, ejercicio, 3));
	}
	@Test 
	void constructorNumeroSetBordeInferior() {
		SetPlaneado serie1= new SetPlaneado(1, ejercicio, 12);
		assertEquals(1, serie1.getNumeroSet());
	}
	@Test 
	void constructorNumeroSetBordeSuperior() {
		SetPlaneado serie1= new SetPlaneado(30, ejercicio, 12);
		assertEquals(30, serie1.getNumeroSet());
	}
	
	@Test
	void constructorRepsCero() {
		assertThrows(ObjetivoInvalidoException.class, ()-> new SetPlaneado(2, ejercicio, 0));
	}
	
	@Test
	void constructorRepsNegativas() {
		assertThrows(ObjetivoInvalidoException.class, ()-> new SetPlaneado(2, ejercicio, -1));
	}
	@Test
	void constructorRepsEncimaDelMaximo() {
		assertThrows(ObjetivoInvalidoException.class, ()-> new SetPlaneado(2, ejercicio, 34));
	}
	@Test 
	void constructorRepsBordeSuperior() {
		SetPlaneado serie1= new SetPlaneado(3, ejercicio, 30);
		assertEquals(30, serie1.getObjetivo());
	}
	@Test 
	void constructorRepsBordeInferior() {
		SetPlaneado serie1= new SetPlaneado(3, ejercicio, 1);
		assertEquals(1, serie1.getObjetivo());
	}
	
	@Test
	void toStringCompleto() {
		String texto = set.toString();
		assertTrue(texto.contains("Sentadilla Libre"));
		assertTrue(texto.contains("12"));
		assertTrue(texto.contains("2"));
	}

	@Test
	void constructor_ejercicioDeTiempo_aceptaSegundos() {

	    Ejercicio plancha = new Ejercicio(2, "PLAN-01", "Plancha", "Abdomen", "Core", TipoEjercicio.TIEMPO);
	    assertEquals(60, new SetPlaneado(1, plancha, 60).getObjetivo());
	}
	
	@Test
	void constructor_ejercicioDePeso_rechazaUnValorEnSegundos() {
																																									
		Ejercicio sentadilla = new Ejercicio(1, "SENT-01", "Sentadilla", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
	    assertThrows(ObjetivoInvalidoException.class, () -> new SetPlaneado(1, sentadilla, 60));
	}
}
