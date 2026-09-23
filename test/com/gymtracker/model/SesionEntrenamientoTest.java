package com.gymtracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;

class SesionEntrenamientoTest {
	
	Usuario usuario;
	Rutina rutina;
	

	@BeforeEach
	void setUp() throws Exception {
		
		 usuario = new Usuario(2,"234","Marta",34, 1.65, Objetivo.PERDIDA_DE_GRASA);
		rutina = new Rutina(2, "Día de pierna", usuario, DiaSemana.MARTES);
		
	}

	@Test
	void constructorLanza_ExcepcionSiUsuarioEsNulo() {
		
		assertThrows(UsuarioInvalidoException.class, () ->{
		
			SesionEntrenamiento sesion1 = new SesionEntrenamiento(1, LocalDate.of(2026, 9, 23), null, rutina);
			
		
		});
		
	}
	
	@Test
	void constructorLanza_ExcepcionSiRutinaEsNula() {
		
		assertThrows(RutinaInvalidaException.class, () ->{
		
			SesionEntrenamiento sesion1 = new SesionEntrenamiento(1, LocalDate.of(2026, 9, 23), usuario, null);
			
		
		});
		
	}
	 
	@Test
	void constructorLanza_ExcepcionSiFechaEsNula() {
		
		assertThrows(FechaInvalidaException.class, () ->{
		
			SesionEntrenamiento sesion1 = new SesionEntrenamiento(1, null, usuario, rutina);
			
		
		});
		
	}
	
	@Test
	void constructor_sesionCreadaQuedaEnCurso() {
	    SesionEntrenamiento sesion1 = new SesionEntrenamiento(1, LocalDate.of(2026, 9, 23), usuario, rutina);

	    assertEquals(EstadoSesion.EN_CURSO, sesion1.getEstado());
	}
	
	@Test 
	
	void finalizarSesion_CambiaEstadoASesionFinalizada() {
		
		SesionEntrenamiento sesion1 = new SesionEntrenamiento( 1, LocalDate.of(2026, 9, 23), usuario, rutina);
		
		 sesion1.finalizarSesion();
		assertEquals(EstadoSesion.FINALIZADA, sesion1.getEstado());
	}
	
	@Test 
	
	void finalizarSesion_LanzaExcepcionSiYaEstaFinalizada() {
		
		SesionEntrenamiento sesion1 = new SesionEntrenamiento( 1, LocalDate.of(2026, 9, 23), usuario, rutina);
		sesion1.finalizarSesion();
		
		
		assertThrows(SesionYaFinalizadaException.class, () ->{
			
			sesion1.finalizarSesion();

		});
		
		
		
	}
	
	

}
