package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.PilaVaciaException;

class PilaAccionesTest {
	
	PilaAcciones<String> pila;

	@BeforeEach
	void setUp() throws Exception {
		
		pila = new PilaAcciones<>();
		
	}

	@Test
	void ordenLIFOCorrecto() {
	
		
		pila.push("A");
		pila.push("B");
		pila.push("C");
		
		
		assertEquals("C", pila.pop());
		assertEquals("B", pila.pop());
		assertEquals("A", pila.pop());
			
	}
	
	
	//Verificar que el método pop() lance la excepcion adecuada cuando se intenta eliminar un elemento cuando la pila está vacía 
	@Test 
	void pop_lanzaPilaVaciaException() {
		
		assertThrows(PilaVaciaException.class, () -> {
		
			pila.pop();
		});
	}

	//se verifica que el método verTope() no elimina  y por lo tanto, el tamaño no cambia 
	
	@Test 
	
	void verTopeNoElimina() {
		
		 pila.push("A");
		 
		 assertEquals("A", pila.verTope());
		 assertEquals(1, pila.darTamanio());
		 assertEquals("A", pila.verTope());
		 assertEquals(1, pila.darTamanio());
		
	}
	
	@Test
	void darTamanio_aumentaConCadaPush() {
	    assertEquals(0, pila.darTamanio()); // estado inicial

	    pila.push("A");
	    assertEquals(1, pila.darTamanio());

	    pila.push("B");
	    assertEquals(2, pila.darTamanio());
	}

	@Test
	void darTamanio_disminuyeConCadaPop() {
	    pila.push("A");
	    pila.push("B");

	    pila.pop();
	    assertEquals(1, pila.darTamanio());

	    pila.pop();
	    assertEquals(0, pila.darTamanio());
	}	
	
	@Test
	
	void reutilizacionTrasVaciarLaPila() {
		
		pila.push("A");
		
		pila.pop();
		
		assertTrue(pila.estaVacia());
		
		pila.push("B");
		
		assertEquals("B", pila.verTope());
		
	}
	
	@Test
	void verTope_lanzaExcepcion_siPilaVacia() {
	    assertThrows(PilaVaciaException.class, ()
	    		-> pila.verTope());
	}
	
}
