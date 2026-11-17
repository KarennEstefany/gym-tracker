package com.gymtracker.model;

import static org.junit.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.MedicionInvalidaException;

class MedicionInBodyTest {
	
	private Usuario usuario;
	private final LocalDate fecha  = LocalDate.of(2026 ,10 ,1);

	@BeforeEach
	void setUp() throws Exception {
		
		usuario = new Usuario(1, "123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
	}

	@Test
	void usuarioNulo_LanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, null, fecha, 66.0, 25.0, 28.0, 40.0, 24.2));
	
	}
	
	@Test
	void fechaNula_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, null, 66.0, 25.0, 28.0, 40.0, 24.2));
	}
	
	@Test
	void fechaFutura_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, LocalDate.now().plusDays(1), 66.0, 25.0, 28.0, 40.0, 24.2));
	}
	
	@Test
	void pesoCero_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, fecha, 0, 25.0, 28.0, 40.0, 24.2));
	}
	
	@Test
	void grasaMayorCien_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, fecha, 66.0, 101.0, 28.0, 40.0, 24.2));
	}
	
	@Test
	void masaMuscularNegativa_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, fecha, 66.0, 25.0, -1.0, 40.0, 24.2));
	}
	
	@Test
	void aguaCorporalCero_lanzaExcepcion() {
		
		assertThrows(MedicionInvalidaException.class, ()->
		
		new MedicionInBody(1, usuario, fecha, 66.0, 25.0, 28.0, 0, 24.2));
	}
	
	@Test
	
	void grasaEnLosLimites_esValida() {
		
		new MedicionInBody(1, usuario, fecha, 66.0, 0.0, 28.0, 40.0, 24.2 );
		new MedicionInBody(2, usuario, fecha, 66.0, 100.0, 28.0, 40.0, 24.2 );
	}

}
