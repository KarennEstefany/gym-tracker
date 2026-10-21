package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EstructuraVaciaException;

public class ColaSetsTest {
	private ColaSets<String> cola;
	
	@BeforeEach
	void setUp() {
		cola= new ColaSets<>();
	}
	
	//////////////Pruebas de cola Vacia/////////////////
	@Test
	void estaVaciaRetornaTrue() {
		assertTrue(cola.estaVacia());
	}
	@Test
	void darTamanioCero() {
		assertEquals(0, cola.darTamanio());
	}
	@Test
	void desencolarLanzaExcepcion() {
		assertThrows(EstructuraVaciaException.class, ()-> cola.desencolar());
	}
	@Test
	void verTopeLanzaExcepcion() {
		assertThrows(EstructuraVaciaException.class, ()-> cola.verTope());
	}
	////////////////Pruebas de las operaciones///////////////////////
	@Test
	void encolarUnElemento() {
		cola.encolar("Primero");
		assertFalse(cola.estaVacia());
		assertEquals(1, cola.darTamanio());
	}
	
	@Test
	void probarEncolarAlFrente() {
	 
	    cola.encolar("Elemento1"); // Queda atrás
	    cola.encolarAlFrente("Elemento2"); 
	    assertEquals(2, cola.darTamanio());

	    assertEquals("Elemento2", cola.desencolar());
	}
	
	@Test
	void ordenFIFO() {
		cola.encolar("Primero");
		cola.encolar("Segundo");
		cola.encolar("Tercero"); 
		
		assertEquals(3, cola.darTamanio());
		assertEquals("Primero", cola.desencolar());
		assertEquals(2, cola.darTamanio());
		assertEquals("Segundo", cola.desencolar());
		assertEquals(1, cola.darTamanio());
		assertEquals("Tercero", cola.desencolar());
		assertTrue(cola.estaVacia());
	}
	@Test
	void verTopeCorrectos() {
		cola.encolar("Elemento");
		cola.verTope();
		assertEquals(1, cola.darTamanio());
	}
}
