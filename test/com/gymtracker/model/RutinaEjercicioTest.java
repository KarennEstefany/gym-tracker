package com.gymtracker.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.OrdenInvalidoException;
import com.gymtracker.exceptions.RepeticionesInvalidasException;

class RutinaEjercicioTest {
	RutinaEjercicio rutina1;
	
	@BeforeEach
	void setUp() {
		rutina1= new RutinaEjercicio(1, 1, 4, 5, 3, 12);
	}
	@Test
	void constructorValido() {
		assertEquals(1, rutina1.getId());
		assertEquals(1, rutina1.getRutinaId());
		assertEquals(4, rutina1.getEjercicioId());
		assertEquals(5, rutina1.getOrdenEnRutina());
		assertEquals(3, rutina1.getSetsObjetivo());
		assertEquals(12, rutina1.getRepsObjetivo());
		
	}
	@Test
	void constructorinValido() {
		assertThrows(OrdenInvalidoException.class, () ->{ new RutinaEjercicio (1,1,4,0,3,12);
		});
		
	}
	@Test
	void setObjetivoCeroLanzaExcepcion() {
		assertThrows(NumeroSetInvalidoException.class, () ->{ new RutinaEjercicio (1,1,4,5,0,12);
		});
		
	}
	@Test
	void repsObjetivoCeroLanzaExcepcion() {
		assertThrows(RepeticionesInvalidasException.class, () ->{ new RutinaEjercicio (1,1,4,5,2,0);
		});
		
	}
	@Test 
	void setterValidoActualiza(){
		rutina1.setOrdenEnRutina(5);
		assertEquals(5, rutina1.getOrdenEnRutina());
		rutina1.setRepsObjetivo(10);
		assertEquals(10,rutina1.getRepsObjetivo());
		rutina1.setSetsObjetivo(4);
		assertEquals(4, rutina1.getSetsObjetivo());
		
	}
	@Test
	void setterValoresBordeValidos(){
		rutina1.setOrdenEnRutina(1);
		assertEquals(1, rutina1.getOrdenEnRutina());
		rutina1.setRepsObjetivo(1);
		assertEquals(1,rutina1.getRepsObjetivo());
		rutina1.setSetsObjetivo(1);
		assertEquals(1, rutina1.getSetsObjetivo());
		rutina1.setOrdenEnRutina(10);
		assertEquals(10, rutina1.getOrdenEnRutina());
		rutina1.setRepsObjetivo(30);
		assertEquals(30,rutina1.getRepsObjetivo());
		rutina1.setSetsObjetivo(30);
		assertEquals(30, rutina1.getSetsObjetivo());
		
	}
	@Test
	void setterValoresBordeInvalidos(){
		
		assertThrows(OrdenInvalidoException.class, () ->{ rutina1.setOrdenEnRutina(0);
		});
		
		assertThrows(RepeticionesInvalidasException.class, () ->{rutina1.setRepsObjetivo(0);
		});
		
		assertThrows(NumeroSetInvalidoException.class, () ->{ rutina1.setSetsObjetivo(0);
		});
		
		assertThrows(OrdenInvalidoException.class, () ->{rutina1.setOrdenEnRutina(11);
		});
		
		assertThrows(RepeticionesInvalidasException.class, () ->{ rutina1.setRepsObjetivo(31);
		});
		
		assertThrows(NumeroSetInvalidoException.class, () ->{ rutina1.setSetsObjetivo(31);
		});
		
	}
	
	
}
