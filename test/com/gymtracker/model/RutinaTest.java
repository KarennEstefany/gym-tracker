package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.DiaSemanaInvalidaException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.UsuarioInvalidoException;

class RutinaTest {
	
	
	Usuario usuario;
	Rutina rutina;

	@BeforeEach
	void setUp() {
		
		usuario  = new Usuario (1, "123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
			}
	
	//Verifica que la excepcion DiaSemanaInvalidaException se lance cuando el día de la semana que el usuario ingresa es nulo


	@Test
	void lanzaExcepcion_DiaSemanaInvalidaException() {
		
		assertThrows(DiaSemanaInvalidaException.class, () -> {
			new Rutina (1, "Día de pierna", usuario, null);
			
		});
		
	}
	
	//Verifica que la excepcion NombreNullException se lance cuando el nombre es nulo. 
		
	
	@Test
	void lanzaExcepcion_NombreNullException() {
		
		assertThrows(NombreNullException.class, () -> {
			new Rutina (1, null, usuario, DiaSemana.LUNES);
			
		});
		
	}
	
	
	//Verifica que la excepcion UsuarioInvalidoException se lance cuando el usuario es nulo. 
	
	@Test
	void lanzaExcepcion_UsuarioInvalidoException() {
		
		assertThrows(UsuarioInvalidoException.class, () -> {
			new Rutina (1, "Día de pierna", null, DiaSemana.LUNES);
			
		});
		
	}
	
	@Test
	void constructor_creaRutinaConDatosValidos() {
	    Rutina rutina1 = new Rutina(1, "Día de pierna", usuario, DiaSemana.LUNES);

	    //Se comprueba que se crea el objeto cuando todos los argumentos son válidos. 
	    //Para esto se utiliza asertAll, que realiza cada prueba aunque una de ellas falle. 
	    assertAll(
	        () -> assertNotNull(rutina1),
	        () -> assertEquals("Día de pierna", rutina1.getNombre()),
	        () -> assertEquals(usuario, rutina1.getUsuario()),
	        () -> assertEquals(DiaSemana.LUNES, rutina1.getDiaSemana())
	    );
	}}
