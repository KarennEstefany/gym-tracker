package com.gymtracker.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;

public class BusquedaLinealTest {
	private List<Integer> numeros;
	private Ejercicio sentadilla;
	private Ejercicio prensa;
	private Ejercicio pressBanca;
	private Ejercicio aperturas;
	private List<Ejercicio> ejercicios;
	
	@BeforeEach 
	void setUp() {
		numeros = new ArrayList<>(Arrays.asList(4,8,15,16,23,42));
		
		sentadilla = new Ejercicio(1, "SENT-01", "Sentadilla Libre", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
		prensa = new Ejercicio(2, "PREN-01", "Prensa", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
		pressBanca = new Ejercicio(3, "PRES-01", "Press banca", "Pectoral", "Empuje", TipoEjercicio.PESO);
		aperturas = new Ejercicio(4, "APER-01", "Aperturas", "Pectoral", "Empuje", TipoEjercicio.PESO);
		ejercicios = new ArrayList<>(Arrays.asList(sentadilla, prensa, pressBanca, aperturas));
	}
	
	////////////////////////Pruebas del metodo buscar con numeros//////////////////////////////////////////////////
	@Test
	void buscarElPrimerElemento() {
		assertEquals(4, BusquedaLineal.buscar(numeros, n->n ==4));
	}
	
	@Test
	void buscarElElementoDelMedio() {
		assertEquals(15, BusquedaLineal.buscar(numeros, n->n ==15));
	}
	@Test
	void buscarElUltimoElemento() {
		assertEquals(42, BusquedaLineal.buscar(numeros, n->n ==42));
	}
	@Test
	void buscarElementoInexistente() {
		assertNull(BusquedaLineal.buscar(numeros, n->n ==99));
	}
	@Test
	void buscarEnListaVacia() {
		assertNull(BusquedaLineal.buscar(new ArrayList<Integer>(), n->true));
	}
	@Test
	void buscarConVariasCoincidencias() {
		Ejercicio encontrado = BusquedaLineal.buscar(ejercicios, e -> e.getGrupoMuscular().equals("Cuádriceps"));
		assertSame(sentadilla, encontrado);
	}
	@Test
	void buscarConTextos() {
		List<String> nombres = Arrays.asList("Ana", "Maria", "Pedro");
		assertEquals("Maria", BusquedaLineal.buscar(nombres, s-> s.startsWith("M")));
	}
	@Test
	void buscarNoModificaLaLista() {
		BusquedaLineal.buscar(numeros, n->n ==4);
		assertEquals(Arrays.asList(4,8,15,16,23,42), numeros);
	}
	@Test
	void buscarConListaNula() {
		BusquedaLineal.buscar(numeros, n->n ==4);
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.<Integer>buscar(null, n-> true));
	}
	@Test
	void buscarConCriterioNulo() {
		BusquedaLineal.buscar(numeros, n->n ==4);
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.buscar(numeros, null));
	}
	
	/////////////////////////////Pruebas del metodo buscarPorIndice///////////////////////////////
	
	@Test
	void buscarPrimerIndice() {
		
		assertEquals(0, BusquedaLineal.buscarIndice(numeros, n->n ==4));
	}
	
	@Test
	void buscarIndiceMedio() {
		
		assertEquals(2, BusquedaLineal.buscarIndice(numeros, n->n ==15));
	}
	
	@Test
	void buscarUltimoIndice() {
		
		assertEquals(5, BusquedaLineal.buscarIndice(numeros, n->n ==42));
	}
	
	@Test
	void buscarIndiceInexistente() {
		
		assertEquals(-1, BusquedaLineal.buscarIndice(numeros, n->n ==99));
	}
	@Test
	void buscarIndiceEnListaVacia() {
		
		assertEquals(-1, BusquedaLineal.buscarIndice(new ArrayList<Integer>(), n->true));
	}
	@Test
	void buscarIndiceVariasCoincidencias() {
		
		assertEquals(2, BusquedaLineal.buscarIndice(ejercicios, e -> e.getGrupoMuscular().equals("Pectoral")));
	}
	
	@Test
	void buscarIndiceConListaNula() {
	
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.<Integer>buscarIndice(null, n-> true));
	}
	@Test
	void buscarIndiceCriterioNulo() {
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.buscarIndice(numeros,null));
	}
	
	//////////////////////////Pruebas del metodo filtrar/////////////////////////////////
		
	@Test
	void filtrarDevuelveEnOrden() {
		List<Integer> pares = BusquedaLineal.filtrar(numeros, n-> n %2==0);
		assertEquals(Arrays.asList(4,8,16,42), pares);
	}
	@Test
	void filtrarSinCoincidencia() {
		List<Integer> resultado = BusquedaLineal.filtrar(numeros, n -> n>100);
		assertTrue(resultado.isEmpty());
	}
	@Test
	void filtrarCuandoTodosCumplen() {
		List<Integer> resultado = BusquedaLineal.filtrar(numeros, n-> n >0);
		assertEquals(numeros,resultado);
	}
	@Test
	void filtrarEnListaVacia() {
		List<Integer> resultado = BusquedaLineal.filtrar(new ArrayList<Integer>(), n-> true);
		assertTrue(resultado.isEmpty());
	}
	@Test
	void filtrarNoModificaListaOriginal() {
		List<Integer> resultado = BusquedaLineal.filtrar(numeros, n -> n>10);
		assertNotSame(numeros, resultado);
		assertEquals(6, numeros.size());
		resultado.clear();
		assertEquals(6, numeros.size());
	}
	@Test
	void filtrarPorGrupoMuscular() {
		List<Ejercicio> ejerciciosCuadriceps = BusquedaLineal.filtrar(ejercicios, e -> e.getGrupoMuscular().equals("Cuádriceps"));
		assertSame(sentadilla, ejerciciosCuadriceps.get(0));
		assertEquals(2, ejerciciosCuadriceps.size());
		assertSame(prensa, ejerciciosCuadriceps.get(1));
	}
	@Test
	void filtrarConListaNula() {
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.<Integer>filtrar(null, n -> true));
	}
	@Test
	void filtrarConCriterioNulo() {
		assertThrows(ObjetoNuloException.class, () -> BusquedaLineal.<Integer>filtrar(numeros, null));
	}
}
