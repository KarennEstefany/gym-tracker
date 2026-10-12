package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.ColaVaciaException;

class ColaSetsTest {
	
	ColaSets<Integer> cola;

	@BeforeEach
	void setUp() throws Exception {
		
		//Se instancia un objeto para realizar la prueba 
		
		cola = new ColaSets<>();
		
	}
	

	@Test
	
	void EncolarYDesencolarNoFuncionaOrdenFIFO() {
		
		
		// Encolar utilizando el método de la clase ColaSets
		cola.encolar(10);
		cola.encolar(20);
		cola.encolar(30);
		
		//Comparar los resultados 
		assertEquals(10, cola.desencolar(), "El primero en salir debe ser 10");
		assertEquals(20, cola.desencolar(), "El segundo en salir debe ser 20");
		assertEquals(30, cola.desencolar(), "El tercero en salir debe ser 30");
		
	}
	
	@Test
	
	void LanzaColaVaciaException() {
	
		assertThrows(ColaVaciaException.class, () -> {
			cola.desencolar();
			
		}, "Debería lanzar ColaVaciaException al intentar desencolar una cola vacía");
	}
	
	
	@Test
	
	void verTopeNoEliminaElemento() {
		
		cola.encolar(10);
		cola.encolar(20); 
		
		cola.verTope();
		assertEquals(10, cola.verTope(), "Debe retornar el primer valor sin eliminarlo");
		
		assertEquals(2, cola.darTamanio(), "El tamaño del arreglo debe ser el mismo.");
	}
	
	@Test
	
	void tamanioCambiaAlEncolarYDesencolar() {
		
		cola.encolar(10);
		cola.encolar(20);// Encolar dos elementos 
		
		assertEquals(2, cola.darTamanio(), "El tamaño incial"); 
		
		cola.encolar(30); 
		//Se comprueba si el tamaño aumenta
		assertEquals(3, cola.darTamanio(), "El tamaño aumenta");
		
		cola.desencolar();
		//Se comprueba si el tamaño disminuye 
		assertEquals(2, cola.darTamanio(), "El tamaño disminuye");
		
	}
	
	@Test
	
	void vaciarColaYVolverAEncolar() {
		
		cola.encolar(10);
		cola.encolar(20); //Encolar dos elementos 
		
		cola.desencolar();
		cola.desencolar();//Ahora se desencolan, por lo tanto, debería quedar vacía
		
		
		assertEquals(true, cola.estaVacia(), "Debe quedar vacío" );
		
		cola.encolar(30); //prueba para volver a encolar 
		
		assertEquals(1, cola.darTamanio(), "Debe agregar el elemento correctamente");
		assertEquals(30, cola.verTope(), "El nuevo elemento ahora es el tope");
	}

}
