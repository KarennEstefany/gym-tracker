package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.CategoriaInvalidaException;

import com.gymtracker.exceptions.GrupoMuscularInvalidoException;
import com.gymtracker.exceptions.NombreNullException;

public class EjercicioTest {
	Ejercicio ejercicio;
	@BeforeEach
	public void setUp() {
		ejercicio= new Ejercicio (1, "SENT-01", "Sentadilla libre", "Cuadriceps", "Tren inferior", TipoEjercicio.PESO);
	}
	
	@Test
	void constructorValido() {
		assertEquals(1, ejercicio.getId());
		assertEquals("SENT-01", ejercicio.getCodigo());
		assertEquals("Sentadilla libre", ejercicio.getNombre());
		assertEquals("Cuadriceps", ejercicio.getGrupoMuscular());
		assertEquals("Tren inferior", ejercicio.getCategoria());
		assertEquals(TipoEjercicio.PESO, ejercicio.getTipo());
	}
	@Test
	void constructorinValido() {
		assertThrows(NombreNullException.class, () ->{ new Ejercicio (1, "SENT-01", null, "Cuadriceps", "Tren inferior", TipoEjercicio.PESO);
		});
		assertThrows(GrupoMuscularInvalidoException.class, () ->{ new Ejercicio (1, "SENT-01", "Sentadilla libre", null, "Tren inferior", TipoEjercicio.PESO);
		});
		assertThrows(CategoriaInvalidaException.class, () ->{ new Ejercicio (1, "SENT-01", "Sentadilla libre", "Cuadriceps", null, TipoEjercicio.PESO);
		});
	}
	
	@Test
	void setterValidoActualiza(){
		ejercicio.setCategoria("Pierna");
		assertEquals("Pierna", ejercicio.getCategoria());
		ejercicio.setGrupoMuscular("Cuadriceps y gluteos");
		assertEquals("Cuadriceps y gluteos", ejercicio.getGrupoMuscular());
		ejercicio.setNombre("Sentadilla con barra");
		assertEquals("Sentadilla con barra", ejercicio.getNombre());
		
		
	}

}
