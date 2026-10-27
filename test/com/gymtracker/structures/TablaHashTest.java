package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.CapacidadInvalidaException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;

public class TablaHashTest {

	private static class ClaveColision {
		private final String id;
 
		ClaveColision(String id) {
			this.id = id;
		}
 
		@Override
		public int hashCode() {
			return 7;
		}
 
		@Override
		public boolean equals(Object otro) {
			return otro instanceof ClaveColision && ((ClaveColision) otro).id.equals(id);
		}
	}
	private TablaHash<String, String> tabla;
	
	@BeforeEach
	void setUp() {
		tabla = new TablaHash<>();
	}
	
	@Test
	void tablaVaciaTamanioCero() {
		assertTrue(tabla.estaVacia());
		assertEquals(0, tabla.darTamanio());
	}
	
	@Test 
	void capacidadCorrecta() {
		assertEquals(16, tabla.darCapacidad());
	}
	@Test
	void capacidadNegativa() {
		assertThrows(CapacidadInvalidaException.class, ()-> new TablaHash<String, String>(0));
		assertThrows(CapacidadInvalidaException.class, ()-> new TablaHash<String, String>(-3));
	}
	//////////////////////////////Pruebas de insertar y buscar/////////////////////////////////
	@Test 
	void insertarYBuscar(){
		tabla.insertar("SENT-01", "Sentadilla");
		assertEquals("Sentadilla", tabla.buscarValor("SENT-01"));
		assertEquals(1, tabla.darTamanio());
		assertFalse(tabla.estaVacia());
	}
	@Test
	void buscarClaveInexistente() {
		tabla.insertar("SENT-01", "Sentadilla");
		assertNull(tabla.buscarValor("PRES-01"));
	}
	@Test
	void buscarEnTablaVacia() {
		assertNull(tabla.buscarValor("PRES-01"));
	}
	@Test
	void insertarClaveRepetida() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.insertar("SENT-01", "Sentadilla sissy");
		assertEquals(1, tabla.darTamanio());
		assertEquals("Sentadilla sissy", tabla.buscarValor("SENT-01"));
	}
	@Test
	void insertarDistintasClaves() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.insertar("PRES-01", "Press de banca");
		tabla.insertar("SENT-02", "Sentadilla isometrica");
		assertEquals(3, tabla.darTamanio());
		assertEquals("Sentadilla", tabla.buscarValor("SENT-01"));
		assertEquals("Press de banca", tabla.buscarValor("PRES-01"));
		assertEquals("Sentadilla isometrica", tabla.buscarValor("SENT-02"));
	}
	////////////////////Pruebas del metodo contiene////////////////////////
	@Test
	void contieneCorrectamentes() {
		tabla.insertar("SENT-01", "Sentadilla");
		assertTrue(tabla.contieneClave("SENT-01"));
		assertFalse(tabla.contieneClave("PRES-01"));
	}
	////////////////////Pruebas de eliminar//////////////////////////
	@Test
	void eliminarClaveExistente() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.insertar("PRES-01", "Press de banca");
		assertEquals("Sentadilla", tabla.eliminar("SENT-01"));
		assertEquals(1, tabla.darTamanio());
		assertNull(tabla.buscarValor("SENT-01"));
		assertEquals("Press de banca", tabla.buscarValor("PRES-01"));
		
	}
	@Test
	void eliminarClaveInexistente() {
		tabla.insertar("PRES-01", "Press de banca");
		assertNull(tabla.eliminar("SENT-01"));
		assertEquals(1, tabla.darTamanio());
	}
	@Test
	void eliminarEnTablaVacia() {
		assertNull(tabla.eliminar("SENT-01"));
	}
	@Test
	void insertarClaveEliminada() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.eliminar("SENT-01");
		tabla.insertar("SENT-01", "Sentadilla sissy");
		assertEquals("Sentadilla sissy", tabla.buscarValor("SENT-01"));
		assertEquals(1, tabla.darTamanio());
	}
	
	////////////////Pruebas de colisiones///////////////////
	
	@Test
	void clavesQueColisionanSeConservanTodas() {
		TablaHash<ClaveColision, String> t = new TablaHash<>();
		ClaveColision a = new ClaveColision("a");
		ClaveColision b = new ClaveColision("b");
		ClaveColision c = new ClaveColision("c");
		t.insertar(a, "1");
		t.insertar(b, "2");
		t.insertar(c, "3");
 
		assertEquals(3, t.darTamanio());
		assertEquals("1", t.buscarValor(a));
		assertEquals("2", t.buscarValor(b));
		assertEquals("3", t.buscarValor(c));
	}
 
	@Test
	void eliminarLaCabezaDeUnaCadenaConColisiones() {
		TablaHash<ClaveColision, String> t = new TablaHash<>();
		ClaveColision a = new ClaveColision("a");
		ClaveColision b = new ClaveColision("b");
		ClaveColision c = new ClaveColision("c");
		t.insertar(a, "1");
		t.insertar(b, "2");
		t.insertar(c, "3"); // se inserta al inicio: es la cabeza de la cadena
 
		assertEquals("3", t.eliminar(c));
		assertEquals(2, t.darTamanio());
		assertNull(t.buscarValor(c));
		assertEquals("1", t.buscarValor(a));
		assertEquals("2", t.buscarValor(b));
	}
 
	@Test
	void eliminarUnNodoDelMedioDeUnaCadenaConColisiones() {
		TablaHash<ClaveColision, String> t = new TablaHash<>();
		ClaveColision a = new ClaveColision("a");
		ClaveColision b = new ClaveColision("b");
		ClaveColision c = new ClaveColision("c");
		t.insertar(a, "1");
		t.insertar(b, "2");
		t.insertar(c, "3");
 
		assertEquals("2", t.eliminar(b));
		assertEquals(2, t.darTamanio());
		assertNull(t.buscarValor(b));
		assertEquals("1", t.buscarValor(a));
		assertEquals("3", t.buscarValor(c));
	}
 
	@Test
	void eliminarElUltimoNodoDeUnaCadenaConColisiones() {
		TablaHash<ClaveColision, String> t = new TablaHash<>();
		ClaveColision a = new ClaveColision("a");
		ClaveColision b = new ClaveColision("b");
		ClaveColision c = new ClaveColision("c");
		t.insertar(a, "1");
		t.insertar(b, "2");
		t.insertar(c, "3");
 
		assertEquals("1", t.eliminar(a)); // el primero que se insertó queda al final
		assertEquals(2, t.darTamanio());
		assertNull(t.buscarValor(a));
		assertEquals("2", t.buscarValor(b));
		assertEquals("3", t.buscarValor(c));
	}
 
	@Test
	void actualizarUnaClaveConColisionNoDuplicaLaEntrada() {
		TablaHash<ClaveColision, String> t = new TablaHash<>();
		ClaveColision a = new ClaveColision("a");
		ClaveColision b = new ClaveColision("b");
		t.insertar(a, "1");
		t.insertar(b, "2");
		t.insertar(a, "nuevo");
 
		assertEquals(2, t.darTamanio());
		assertEquals("nuevo", t.buscarValor(a));
	}
	
	/////////////////Pruebas de factor de carga y rehashing///////////////////
	@Test
	void superarElFactorDeCarga() {
		TablaHash<String, Integer> tablaT = new TablaHash<>(2);
		for(int i =0; i< 100; i++) {
			tablaT.insertar("k" + i, i);
		}
		assertEquals(100, tablaT.darTamanio());
		assertTrue(tablaT.darCapacidad() >2);
		for(int i =0; i< 100; i++) {
			assertEquals(i, tablaT.buscarValor("k" + i));
		}
		
	}
	@Test
	void conservaLasClavesQueColisionan() {
		TablaHash<ClaveColision, String> tt= new TablaHash<>(2);
		ClaveColision[] claves = new ClaveColision [10];
		for (int i =0; i< claves.length; i++) {
			claves[i]= new ClaveColision("c" + i);
			tt.insertar(claves[i], "v" + i);
		}
		assertEquals(10, tt.darTamanio());
		for(int i=0; i< claves.length; i++) {
			assertEquals("v" + i, tt.buscarValor(claves[i]));
		}
	}
	///////////////////Pruebas de listar valores y claves///////////////////
	@Test
	void devolverValores() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.insertar("PRES-01", "Press de banca");
		tabla.insertar("SENT-02", "Sentadilla isometrica");
		List<String> valores = tabla.listarValores();
		assertEquals(3, valores.size());
		assertTrue(valores.containsAll(List.of("Sentadilla","Press de banca", "Sentadilla isometrica")));
	}
	@Test
	void devolverClaves() {
		tabla.insertar("SENT-01", "Sentadilla");
		tabla.insertar("PRES-01", "Press de banca");
		List<String> claves = tabla.listarClaves();
		assertEquals(2, claves.size());
		assertTrue(claves.containsAll(List.of("SENT-01","PRES-01")));
	}
	
	@Test
	void listarEnTablaVacia() {
		assertTrue(tabla.listarValores().isEmpty());
		assertTrue(tabla.listarClaves().isEmpty());
	}
	
	/////////////////////Pruebas con parametros nulos/////////////////////
	 @Test
	void claveNula() {
		 assertThrows(ObjetoNuloException.class, ()-> tabla.insertar(null, "N"));
	 }
	 @Test
		void valorNulo() {
			 assertThrows(ObjetoNuloException.class, ()-> tabla.insertar("N", null));
		 }
	 @Test
	 void buscarYEliminarConClaveNula() {
		 assertThrows(ObjetoNuloException.class, ()-> tabla.buscarValor(null));
		 assertThrows(ObjetoNuloException.class, ()-> tabla.contieneClave( null));
		 assertThrows(ObjetoNuloException.class, ()-> tabla.eliminar(null));
	 }
	 
	 /////////////////////Pruebas de uso real en el proyecto////////////////////
	 @Test
	 void catalogoDeEjercicioBuscadoPorCodigo() {
		 TablaHash<String, Ejercicio> catalogoEjercicios = new TablaHash<>();
		 Ejercicio sentadilla = new Ejercicio (1, "SENT-01", "Sentadilla libre", "Cuádriceps", "Piernas",
					TipoEjercicio.PESO);
			Ejercicio pressBanca = new Ejercicio(2, "PRES-01", "Press banca", "Pectoral", "Empuje", TipoEjercicio.PESO);
			catalogoEjercicios.insertar(sentadilla.getCodigo(), sentadilla);
			catalogoEjercicios.insertar(pressBanca.getCodigo(), pressBanca);
			
			assertSame(sentadilla, catalogoEjercicios.buscarValor("SENT-01"));
			assertSame(pressBanca, catalogoEjercicios.buscarValor("PRES-01"));
			assertNull(catalogoEjercicios.buscarValor("SENT-02"));
	 }
	
		
}
