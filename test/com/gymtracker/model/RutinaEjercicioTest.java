package com.gymtracker.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.OrdenInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;

import com.gymtracker.exceptions.ObjetoNuloException;

class RutinaEjercicioTest {
	RutinaEjercicio rutinaE;
	Rutina rutina;
	Ejercicio ejercicio;
	
	@BeforeEach
	void setUp() {
		Usuario usuario = new Usuario(1, "2918827018", "Sofia", 19, 1.6, Objetivo.FUERZA);
		rutina = new Rutina(1, "Día de pierna", usuario, DiaSemana.LUNES); // ajusta al constructor real
		ejercicio = new Ejercicio(4, "SENT-01", "Sentadilla libre", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
		rutinaE= new RutinaEjercicio(1, rutina, ejercicio, 5, 3, 12);
	}
	@Test
	void constructorValido() {
		assertEquals(1, rutinaE.getId());
		assertSame(rutina, rutinaE.getRutina());
		assertSame(ejercicio, rutinaE.getEjercicio());
		assertEquals(5, rutinaE.getOrdenEnRutina());
		assertEquals(3, rutinaE.getSetsObjetivo());
		assertEquals(12, rutinaE.getRepsObjetivo());
		
	}
	@Test
	void constructorOrdenInvalidoLanzaExcepcion() {
		assertThrows(OrdenInvalidoException.class, () ->{ new RutinaEjercicio (1,rutina,ejercicio,0,3,12);
		});
		
	}
	@Test
	void constructorSetObjetivoCeroLanzaExcepcion() {
		assertThrows(NumeroSetInvalidoException.class, () ->{ new RutinaEjercicio (1,rutina,ejercicio,5,0,12);
		});
		
	}
	@Test
	void constructorRepsObjetivoCeroLanzaExcepcion() {
		assertThrows(RepeticionesInvalidasException.class, () ->{ new RutinaEjercicio (1,rutina,ejercicio,5,2,0);
		});
		
	}
	@Test
	void constructorRutinaNulaLanzaExcepcion() {
		assertThrows(ObjetoNuloException.class,
				() -> new RutinaEjercicio(1, null, ejercicio, 5, 3, 12));
	}

	@Test
	void constructorEjercicioNuloLanzaExcepcion() {
		assertThrows(ObjetoNuloException.class,
				() -> new RutinaEjercicio(1, rutina, null, 5, 3, 12));
	}
	@Test 
	void setterValidoActualiza(){
		rutinaE.setOrdenEnRutina(5);
		assertEquals(5, rutinaE.getOrdenEnRutina());
		rutinaE.setRepsObjetivo(10);
		assertEquals(10,rutinaE.getRepsObjetivo());
		rutinaE.setSetsObjetivo(4);
		assertEquals(4, rutinaE.getSetsObjetivo());
		
	}
	@Test
	void setterValoresBordeValidos(){
		rutinaE.setOrdenEnRutina(1);
		assertEquals(1, rutinaE.getOrdenEnRutina());
		rutinaE.setRepsObjetivo(1);
		assertEquals(1,rutinaE.getRepsObjetivo());
		rutinaE.setSetsObjetivo(1);
		assertEquals(1, rutinaE.getSetsObjetivo());
		rutinaE.setOrdenEnRutina(10);
		assertEquals(10, rutinaE.getOrdenEnRutina());
		rutinaE.setRepsObjetivo(30);
		assertEquals(30,rutinaE.getRepsObjetivo());
		rutinaE.setSetsObjetivo(30);
		assertEquals(30, rutinaE.getSetsObjetivo());
		
	}
	@Test
	void setterValoresBordeInvalidos(){
		
		assertThrows(OrdenInvalidoException.class, () ->{ rutinaE.setOrdenEnRutina(0);
		});
		
		assertThrows(RepeticionesInvalidasException.class, () ->{rutinaE.setRepsObjetivo(0);
		});
		
		assertThrows(NumeroSetInvalidoException.class, () ->{ rutinaE.setSetsObjetivo(0);
		});
		
		assertThrows(OrdenInvalidoException.class, () ->{rutinaE.setOrdenEnRutina(11);
		});
		
		assertThrows(RepeticionesInvalidasException.class, () ->{ rutinaE.setRepsObjetivo(31);
		});
		
		assertThrows(NumeroSetInvalidoException.class, () ->{ rutinaE.setSetsObjetivo(31);
		});
		
	}
	
	
}
