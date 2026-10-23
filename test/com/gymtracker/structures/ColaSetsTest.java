package com.gymtracker.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EstructuraVaciaException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;

public class ColaSetsTest {
	private ColaSets<String> cola;
	private RutinaEjercicio sentadillaRutina1;
	private RutinaEjercicio extensionRutina1;
	
	@BeforeEach
	void setUp() {
		cola= new ColaSets<>();
		Usuario usuario = new Usuario (1, "28993086", "Maria", 18, 1.7, Objetivo.HIPERTROFIA);
		Rutina rutina1 = new Rutina(1, "Dia de pierna", usuario, DiaSemana.LUNES);
		Ejercicio sentadilla = new Ejercicio (1, "SENT-1", "Sentadilla Libre", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
		Ejercicio extension = new Ejercicio (2, "EXT-01", "Extension de cuadriceps", "Cuadriceps", "Piernas", TipoEjercicio.PESO );
		sentadillaRutina1= new RutinaEjercicio(1, rutina1, sentadilla,1, 4, 8 );
		extensionRutina1= new RutinaEjercicio(2, rutina1, extension,2, 3, 10 );
	}
	///////////////Metodo auxiliar////////////////////////////////////////
	/// 
	private ColaSets<SetPlaneado> construirColaDeSets(RutinaEjercicio... plan) {
		ColaSets<SetPlaneado> colaSets = new ColaSets<>();
		for (RutinaEjercicio re : plan) {
			for (int n = 1; n <= re.getSetsObjetivo(); n++) {
				colaSets.encolar(new SetPlaneado(n, re.getEjercicio(),re.getRepsObjetivo()));
			}
		}
		return colaSets;
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
	@Test 
	void encolarYDesencolarIntercalados() {
		cola.encolar("A");
		cola.encolar("B");
		assertEquals("A", cola.desencolar());
		cola.encolar("C");
		assertEquals("B", cola.desencolar());
		assertEquals(1, cola.darTamanio());
	}
	@Test
	void encolarAumentaElTamanioYDesencolarLoReduce() {
		cola.encolar("1");
		cola.encolar("2");
		cola.encolar("3");
		assertEquals(3, cola.darTamanio());
		cola.desencolar();
		cola.desencolar();
		cola.desencolar();
		assertEquals(0, cola.darTamanio());
	}
	/////////////////////Pruebas cola de sets con los sets planeados//////////////////////////
	@Test
	void unaEntradaPorRutina() {
		ColaSets<SetPlaneado> colaSeries = construirColaDeSets(sentadillaRutina1, extensionRutina1);
		int setsEsperados= sentadillaRutina1.getSetsObjetivo() + extensionRutina1.getSetsObjetivo();
		assertEquals(7, setsEsperados);
		assertEquals(setsEsperados, colaSeries.darTamanio());
	}
	@Test
	void seRespetaElOrdenEnRutina() {
		ColaSets<SetPlaneado> colaSeries = construirColaDeSets(sentadillaRutina1, extensionRutina1);
		String[] ejerciciosEsperados = { "Sentadilla Libre", "Sentadilla Libre", "Sentadilla Libre", "Sentadilla Libre", 
				"Extension de cuadriceps", "Extension de cuadriceps", "Extension de cuadriceps"};
		int setsEsperados[]= {1, 2, 3, 4, 1, 2, 3};
		
		for(int i=0; i< ejerciciosEsperados.length; i++) {
			SetPlaneado serieActual = colaSeries.desencolar();
			assertEquals(ejerciciosEsperados[i], serieActual.getEjercicio().getNombre());
			assertEquals(setsEsperados[i], serieActual.getNumeroSet());
		}
		assertTrue(colaSeries.estaVacia());
		}
	@Test
	void seConservanLasRepsObjetivosDeCadaSet() {
		ColaSets<SetPlaneado> colaSeries = construirColaDeSets(sentadillaRutina1, extensionRutina1);
		
		int[] repsEsperadas= {8,8,8,8,10,10,10};
		for (int reps : repsEsperadas) {
			assertEquals(reps, colaSeries.desencolar().getRepsObjetivo());
		}
	}
	@Test
	void avanzaSetPorSetHastaVaciar() {
		ColaSets<SetPlaneado> colaSeries = construirColaDeSets(sentadillaRutina1, extensionRutina1);
		
		int seriesRegistradas=0;
		while(!colaSeries.estaVacia()) {
			SetPlaneado pendiente=  colaSeries.verTope();
			SetPlaneado registrado = colaSeries.desencolar();
			assertSame(pendiente, registrado);
			seriesRegistradas++;
			
		}
		assertEquals(7, seriesRegistradas);
		assertThrows(EstructuraVaciaException.class, () -> colaSeries.verTope());
	}
		
		
		
	
}
