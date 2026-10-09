package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EdadEstaturaInvalidaException;

import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;
class UsuarioTest {
	Usuario usuario;
	@BeforeEach
	public void setUp() {
		usuario= new Usuario (2, "2918827018", "Sofia", 19, 1.6, Objetivo.FUERZA);
	}
	@Test
	void constructorValido() {
		assertEquals(2, usuario.getId());
		assertEquals("2918827018", usuario.getDocumento());
		assertEquals("Sofia", usuario.getNombre());
		assertEquals(19, usuario.getEdad());
		assertEquals(1.6, usuario.getEstatura(), 0.001);
		assertEquals(Objetivo.FUERZA, usuario.getObjetivo());
	}
	@Test
	void constructorinValido() {
		assertThrows(EdadEstaturaInvalidaException.class, () ->{ new Usuario (2, "2918827018", "Sofia", -2, 1.6, Objetivo.FUERZA);
		});
		assertThrows(EdadEstaturaInvalidaException.class, () ->{ new Usuario (2, "2918827018", "Sofia", 19, 0, Objetivo.FUERZA);
		});
		assertThrows(NombreNullException.class, () ->{ new Usuario (2, "2918827018", null, 19, 1.6, Objetivo.FUERZA);
		});
		assertThrows(ObjetivoInvalidoException.class, () ->{ new Usuario (2, "2918827018", "Sofia", 19, 1.6, null);
		});
	}
	
	@Test
	void setterValidoActualiza(){
		usuario.setEdad(20);
		assertEquals(20, usuario.getEdad());
		usuario.setEstatura(1.67);
		assertEquals(1.67, usuario.getEstatura());
		usuario.setNombre("Isabel");
		assertEquals("Isabel", usuario.getNombre());
		usuario.setObjetivo(Objetivo.HIPERTROFIA);
		assertEquals(Objetivo.HIPERTROFIA, usuario.getObjetivo());
		
	}
	@Test
	void setterValoresBordeValidos(){
		usuario.setEdad(1);
		assertEquals(1, usuario.getEdad());
		usuario.setEstatura(0.5);
		assertEquals(0.5,usuario.getEstatura(), 0.001);
		
		
		usuario.setEdad(120);
		assertEquals(120, usuario.getEdad());
		usuario.setEstatura(2.3);
		assertEquals(2.3,usuario.getEstatura(), 0.001);
		
	}
	@Test
	void setterValoresBordeInvalidos(){
		
		assertThrows(EdadEstaturaInvalidaException.class, () ->{ usuario.setEdad(0);
		});
		
		assertThrows(EdadEstaturaInvalidaException.class, () ->{usuario.setEstatura(0.49);
		});
		
		assertThrows(EdadEstaturaInvalidaException.class, () ->{ usuario.setEdad(121);
		});
		
		assertThrows(EdadEstaturaInvalidaException.class, () ->{usuario.setEstatura(2.4);
		});
	}

}
