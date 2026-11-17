package com.gymtracker.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.MedicionInvalidaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.MedicionInBody;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;
import com.gymtracker.model.VariacionInBody;

class InBodyServiceTest {
	
	private InBodyService servicio;
	private Usuario ana;
	private Usuario luis;
	

	@BeforeEach
	void setUp(){
		
		servicio = new InBodyService();
		ana = new Usuario( 1, "123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
		luis = new Usuario( 2, "456", "Luis", 30, 1.80, Objetivo.FUERZA, "clave123");
	}

	@Test
	void registrarMedicion_calcularImcYAsignaIds() {
		
		MedicionInBody primera = servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 40.0);
		MedicionInBody segunda = servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 8), 65.0, 24.0, 28.5, 41.0);
		
		assertEquals(1, primera.getId());
		assertEquals(2, segunda.getId());
		
	}
	
	@Test
	
	void registrarMedicion_usuarioNulo_lanzaExcepcion() {
		
		assertThrows(UsuarioInvalidoException.class, () ->
		servicio.registrarMedicion(null, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 40.0));
		
	}
	
@Test
	
	void registrarMedicion_fechaRepetida_lanzaExcepcion() {
	servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 40.0);
	
		assertThrows(UsuarioInvalidoException.class, () ->
		servicio.registrarMedicion(null, LocalDate.of(2026, 10, 1), 65.0, 24.0, 28.5, 41.0));
		
	}


	void registrarMedicion_mismaFechaOtroUsuario_esValido() {
	servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 40.0);
	
	MedicionInBody deLuis = servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 8), 80.0, 18.0, 38.5, 50.0);
		
	assertEquals(luis, deLuis.getUsuario());
		
	}
	
	void registrarMedicion_datosInvalidos_noDejaHuecosEnLosIds() {
		assertThrows(UsuarioInvalidoException.class, () ->
		servicio.registrarMedicion(null, LocalDate.of(2026, 10, 1), -5.0, 25.0, 28.0, 40.0));
		
		MedicionInBody valida = servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 8), 66.0, 25.0, 28.5, 40.0);
		
		assertEquals(1, valida.getId());
	}
	
	
	//Listar por usuario
	
	@Test
	void listarPorUsuario_devuelveSoloLasDelUsuarioOrdenadasPorFecha() {
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 20), 64.0, 23.0, 29.0, 41.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 41.0);
		servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 5),  80.0, 18.0, 38.5, 50.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 10), 65.0, 24.0, 28.5, 40.5);
		
		List<MedicionInBody> lista = servicio.listarPorUsuario(ana);
		
		
		assertEquals(3, lista.size());
		assertEquals(LocalDate.of(2026, 10, 1), lista.get(0).getFecha());
		assertEquals(LocalDate.of(2026, 10, 10), lista.get(1).getFecha());
		assertEquals(LocalDate.of(2026, 10, 20), lista.get(2).getFecha());
		
	}
	
	@Test
	void listarPorUsuario_sinMediciones_devuelveListaVacia() {
		
		assertTrue(servicio.listarPorUsuario(ana).isEmpty());
	}
	
	@Test
	void listarPorUsuario_usuarioNulo_lanzaExcepcion() {
		assertThrows(UsuarioInvalidoException.class, ()-> servicio.listarPorUsuario(null));
	}
	
	//Obtener última medición
	
	void obtenerUltimaMedicion_devuelveLaFechaMasReciente() {
		
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 20), 64.0, 23.0, 29.0, 41.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 66.0, 25.0, 28.0, 40.0);
		
		Optional<MedicionInBody> ultima = servicio.obtenerUltimaMedicion(ana);
		
		assertTrue(ultima.isPresent());
		assertEquals(LocalDate.of(2026, 10, 20), ultima.get().getFecha() );
	}
	
	
	@Test
	void obtenerUltimaMedicion_sinMediciones_devuelveOptionalVacio() {
		
		assertFalse(servicio.obtenerUltimaMedicion(ana).isPresent());
	}
	
	//Comparar
	
	@Test
	
	void comparar_consecuticas_devuelveLosDeltas() {
		
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 80.0, 25.0, 30.0, 40.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 8), 78.0, 23.5, 30.5, 41.0);
		
		VariacionInBody v = servicio.comparar(ana, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 8));
		
		assertEquals(-2.0, v.getDeltaPeso(), 0.0001);
		assertEquals(-1.5, v.getDeltaGrasa(), 0.0001);
		assertEquals(0.5, v.getDeltaMasaMuscular(), 0.0001);
	}
	
	@Test
	
	void comparar_saltandoUnaMedicionIntermedia_funciona() {
		
		
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 80.0, 25.0, 30.0, 40.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 8), 79.0, 24.5, 30.2, 40.5);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 15), 78.0, 23.5, 30.5, 41.0);
		
		
		VariacionInBody comparar = servicio.comparar(ana, LocalDate.of(2026, 10, 1),LocalDate.of(2026, 10, 15));
		assertEquals(-2.0, comparar.getDeltaPeso());
		
		
	}
	
	@Test
	void comparar_fechaSinMedicion_lanzaExcepcion() {
		
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 80.0, 25.0, 30.0, 40.0);
		
		assertThrows(MedicionInvalidaException.class, () ->
		servicio.comparar(ana, LocalDate.of(2026, 10, 1),LocalDate.of(2026, 10, 8)));
	}
	
	@Test
	void comparar_ignoraLasMedicionesDeOtrosUsuarios() {
		
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 80.0, 25.0, 30.0, 40.0);
		servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 4), 90.0, 20.0, 40.0, 50.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 8), 78.0, 23.0, 30.5, 41.0);
		
		VariacionInBody v = servicio.comparar(ana,LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 8));
		
		assertEquals(-2.0, v.getDeltaPeso(), 0.0001);
		
	}
	
	@Test
	void comparar_fechasEnOrdenInverso_lanzaExcepcion() {
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 1), 80.0, 25.0, 30.0, 40.0);
		servicio.registrarMedicion(luis, LocalDate.of(2026, 10, 4), 90.0, 20.0, 40.0, 50.0);
		servicio.registrarMedicion(ana, LocalDate.of(2026, 10, 8), 78.0, 23.0, 30.5, 41.0);
		
		
		assertThrows(MedicionInvalidaException.class, () ->
		servicio.comparar(ana, LocalDate.of(2026, 10, 8),LocalDate.of(2026, 10, 1)));
		
	}
	
	

}
