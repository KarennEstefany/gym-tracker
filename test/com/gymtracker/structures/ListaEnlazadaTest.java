package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EstructuraVaciaException;

public class ListaEnlazadaTest {
	private ListaEnlazada<String> lista;
	
	@BeforeEach
	void setUp() {
		lista = new ListaEnlazada<>();
	}
	/**
	 * Agrega al final de la lista cada elemento 
	 * @param strings, recibe cualquier cantidad de elementos de tipo String, por dentro se trata como un arreglo[]
	 */
	
	private void llenarLista(String ...strings) {
		for (String s : strings) {
			lista.agregarAlFinal(s);
		}
	}
	private String vaciarYLeer() {
		StringBuilder sb = new StringBuilder();
		while (!lista.estaVacia()) {
			sb.append(lista.eliminarPrimero());
		}
		return sb.toString();
	}
	//Pruebas en lista Vacia
	@Test
	void nuevaListaVaciaConTamanioCero() {
		assertTrue(lista.estaVacia());
		assertEquals(0,lista.darTamanio());
	}
	@Test
	void eliminarPrimeroEnListaVaciaLanzaExcepcion() {
		assertThrows(EstructuraVaciaException.class, () -> lista.eliminarPrimero());
	}
	@Test
	void obtenerPrimeroEnListaVaciaLanzaExcepcion() {
		assertThrows(EstructuraVaciaException.class, () -> lista.obtenerPrimero());
	}
	@Test
	void buscarEnListaVaciaDevuelveNull() {
		assertNull(lista.buscar("Alas"));
	}
	@Test
	void eliminarEnListaVaciaDevuelveNull() {
		assertNull(lista.eliminar("Alas"));
	}
	
	/// Pruebas de agregar al inicio
	@Test 
	void agregarAlInicioEnListaVacia() {
		lista.agregarAlInicio("Botas");
		assertFalse(lista.estaVacia());
		assertEquals(1,lista.darTamanio());
		assertEquals("Botas", lista.obtenerPrimero());
	}
	@Test 
	void agregarAlInicioEnListaVaciaActualziaLaCola() {
		lista.agregarAlInicio("Botas");
		lista.agregarAlFinal("Cola");
		assertEquals("BotasCola",vaciarYLeer());

	}
	@Test 
	void agregarAlInicioMantieneOrdenInverso() {
		lista.agregarAlInicio("Alas");
		lista.agregarAlInicio("Botas");
		lista.agregarAlInicio("Cola");
		assertFalse(lista.estaVacia());
		assertEquals(3,lista.darTamanio());
		assertEquals("ColaBotasAlas", vaciarYLeer());
	}
	
	/// Pruebas de agregar al final
	@Test 
	void agregarAlFinalEnListaVacia() {
		lista.agregarAlFinal("Cola");
		assertFalse(lista.estaVacia());
		assertEquals(1,lista.darTamanio());
		
	}
	@Test 
	void agregarAlFinalMantieneOrdenDeInsercion() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals(3,lista.darTamanio());
		assertEquals("AlasBotasCola", vaciarYLeer());
	}
	// pruebas de obtener y eliminar
	@Test 
	void obtenerPrimeroNoModificaLaLista() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Alas", lista.obtenerPrimero());
		assertEquals(3,lista.darTamanio());
		
	}
	@Test 
	void eliminarPrimeroModificaYDevuelve() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Alas", lista.eliminarPrimero());
		assertEquals(2,lista.darTamanio());
		assertEquals("Botas", lista.obtenerPrimero());
	}
	@Test
	void eliminarPrimeroConservaLaCola() {
		llenarLista("Alas", "Botas");
		assertEquals("Alas", lista.eliminarPrimero());
		lista.agregarAlFinal("Cola");
		assertEquals("BotasCola", vaciarYLeer());
	}
	
	@Test
	void eliminarLaCabeza() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Alas", lista.eliminar("Alas"));
		assertEquals(2,lista.darTamanio());
		assertEquals("BotasCola", vaciarYLeer());
	}
	@Test
	void eliminarElMedio() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Botas", lista.eliminar("Botas"));
		assertEquals(2,lista.darTamanio());
		assertEquals("AlasCola", vaciarYLeer());
	}
	@Test
	void eliminarLaColaYAgregarAlFinal() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Cola", lista.eliminar("Cola"));
		assertEquals(2,lista.darTamanio());
		lista.agregarAlFinal("Datos");
		assertEquals("AlasBotasDatos", vaciarYLeer());
	}
	@Test
	void eliminarInexistente() {
		llenarLista("Alas", "Botas", "Cola");
		assertNull(lista.eliminar("Datos"));
		assertEquals(3,lista.darTamanio());
		assertEquals("AlasBotasCola", vaciarYLeer());
	}
	@Test
	void eliminarElUnicoElemento() {
		llenarLista("Alas");
		assertEquals("Alas", lista.eliminar("Alas"));
		assertTrue(lista.estaVacia());
		lista.agregarAlFinal("Botas");
		assertEquals("Botas", lista.obtenerPrimero());
	}
	@Test
	void eliminarDuplicado() {
		llenarLista("Alas", "Botas", "Alas");
		assertEquals("Alas", lista.eliminar("Alas"));
		assertEquals(2,lista.darTamanio());
		assertEquals("BotasAlas", vaciarYLeer());
	}
	@Test
	void buscarElementoExistente() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Botas", lista.buscar("Botas"));
	}
	@Test
	void buscarPrimerYUltimo() {
		llenarLista("Alas", "Botas", "Cola");
		assertEquals("Alas", lista.buscar("Alas"));
		assertEquals("Cola", lista.buscar("Cola"));
	}
	@Test
	void buscarInexistente() {
		llenarLista("Alas", "Botas", "Cola");
		assertNull(lista.buscar("Datos"));
	}
	@Test
	void buscarNoModifica() {
		llenarLista("Alas", "Botas", "Cola");
		lista.buscar("Alas");
		assertEquals(3, lista.darTamanio());
		assertEquals("AlasBotasCola", vaciarYLeer());
	}
	
	
}
