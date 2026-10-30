package com.gymtracker.persistencia;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.gymtracker.exceptions.PersistenciaException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;

class PersistenciaTest {
	
	@TempDir
	Path directorio;

	private String ruta;
	
	@BeforeEach
	void setUp() throws Exception {
		
		ruta = directorio.resolve("gymtracker.dat").toString();
	}

	@Test
	void cargar_archivoInexistente_devuelveDatosVacios() {
		
		DatosApp datos = Persistencia.cargar(ruta);
		
		assertNotNull(datos);
		assertTrue(datos.getUsuarios().isEmpty());
		assertTrue(datos.getRutinas().isEmpty());
		
	}
	
	@Test
	void guardarYCargar_conservaLosUsuarios() {
		
		DatosApp datos = new DatosApp();
		datos.getUsuarios().add(new Usuario(1,"123", "Ana", 25,1.65, Objetivo.HIPERTROFIA, "clave123"));
		
		
		Persistencia.guardar(datos, ruta);
		DatosApp cargado = Persistencia.cargar(ruta);
		
		assertEquals(1, cargado.getUsuarios().size());
		assertEquals("123", cargado.getUsuarios().get(0).getDocumento());
		assertEquals("Ana", cargado.getUsuarios().get(0).getNombre());
		
		
	}
	
	
	@Test
	void guardarYCargar_laRutinaApuntaAlMismoUsuarioDeLaLista() {
		Usuario ana = new Usuario(1,"123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
		Rutina rutina = new Rutina(1, "Día de pierna", ana, DiaSemana.LUNES);
		
		DatosApp datos = new DatosApp();
		datos.getUsuarios().add(ana);
		datos.getRutinas().add(rutina);
		
		Persistencia.guardar(datos, ruta);
		DatosApp cargado = Persistencia.cargar(ruta);
		
		Usuario usuarioDeLaLista = cargado.getUsuarios().get(0);
		Usuario usuarioDeLaRutina = cargado.getRutinas().get(0).getUsuario();
		
		
		//assertSame compara referencias, ya que debe ser el mismo objeto, no una copia igual 
		assertSame(usuarioDeLaLista, usuarioDeLaRutina);
		
		}
	
	@Test 
	void guardarYCargar_conTodasLasListas_noFallarPorClasesNoSerializables() {
		Usuario ana = new Usuario(1,"123", "Ana", 25, 1.65, Objetivo.HIPERTROFIA, "clave123");
		Ejercicio sentadilla = new Ejercicio(1, "SENT-01", "Sentadilla", "Cuádriceps", "Piernas", TipoEjercicio.PESO );
		Rutina rutina = new Rutina(1, "Día de pierna", ana, DiaSemana.LUNES);
		RutinaEjercicio rutinaEjercicio = new RutinaEjercicio(1, rutina, sentadilla, 1,4,10);
		SesionEntrenamiento sesion = new SesionEntrenamiento(1,LocalDate.of(2026, 10, 26), ana, rutina);
		SetPeso set = new SetPeso(1, sesion, sentadilla, 1,8,2,80.0,10);
		
		DatosApp datos  = new DatosApp();
		datos.getUsuarios().add(ana);
		datos.getEjercicio().add(sentadilla);
		datos.getRutinas().add(rutina);
		datos.getEjerciciosDeRutinas().add(rutinaEjercicio);
		datos.getSesiones().add(sesion);
		datos.getHistorial().add(set);
		
		Persistencia.guardar(datos, ruta);
		DatosApp cargado = Persistencia.cargar(ruta);
		
		assertEquals(1, cargado.getEjercicio().size());
		assertEquals(1, cargado.getEjerciciosDeRutinas().size());
		assertEquals(1, cargado.getSesiones().size());
		assertEquals(1, cargado.getHistorial().size());
		assertEquals(80.0, ( (SetPeso) cargado.getHistorial().get(0)).getPeso());
		
		
	}
	
	@Test 
	void guardar_creaLaCarpetaSiNoExiste() {
		
		String rutaConSubcarpeta = directorio.resolve("datos").resolve("gymtracker.dat").toString();
		
		Persistencia.guardar(new DatosApp(), rutaConSubcarpeta);
		
		assertTrue(Files.exists(directorio.resolve("datos").resolve("gymtracker.dat")));
		
	}
	
	
	@Test 
	void cargar_archivoCorrupto_lanzaPersistenciaException() throws IOException {
		
		Files.write(directorio.resolve("gymtracker.dat"), "esto no es un objeto".getBytes());
		
		assertThrows(PersistenciaException.class, () -> Persistencia.cargar(ruta));
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
