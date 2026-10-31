package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.DiaRutinaDuplicadoException;
import com.gymtracker.exceptions.DiaSemanaInvalidaException;
import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.OrdenInvalidoException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;

public class RutinaServiceTest {
	private RutinaService rutinaService;
	private Usuario Isabel;
	private Usuario Michael;
	private Ejercicio sentadilla;
	private Ejercicio bulgara;
	private Ejercicio plancha;
	
	@BeforeEach
	void setUp() {
		rutinaService = new RutinaService();
		Isabel = new Usuario(1, "100", "Isabel", 19, 1.7, Objetivo.HIPERTROFIA, "Clave189");
		Michael = new Usuario(2, "200", "Michael", 21, 1.8, Objetivo.HIPERTROFIA, "Clave2910");
		sentadilla = new Ejercicio(1, "SENT-01", "Sentadilla", "Cuadriceps", "Piernas",TipoEjercicio.PESO );
		bulgara = new Ejercicio(2, "SENT-02", "Sentadilla bulgara", "Cuadriceps", "Piernas",TipoEjercicio.PESO );
		plancha = new Ejercicio(3, "COR-01", "Plancha", "Abdominales", "Core",TipoEjercicio.TIEMPO );
	}
	
	//////////////////////Metodos auxiliares///////////////////////////
	/// Crea una nueva rutina para el usuario dado
	private Rutina crearRutina(Usuario usuario, String nombre, DiaSemana dia) {
		return rutinaService.crearRutina(usuario, nombre, dia);
	}
	/** Agrega un ejercicio con 3 sets y 10 repeticiones */
	private RutinaEjercicio agregarEjercicio(Rutina rutina, Ejercicio ejercicio) {
		return rutinaService.agregarEjercicio(rutina, ejercicio, 3, 10);
	}
	////////////////////Pruebas de estado inicial//////////////////////////////
	@Test
	void serviceNuevo() {
		assertTrue(rutinaService.listarRutinas(Isabel).isEmpty());
		assertFalse(rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES).isPresent()); //verifica si existe una rutina asociada a Isabel para el día Lunes en el sistema(no debe haberla, resultado=false)
	}
	///////////////////Pruebas de crear rutina/////////////////////////////
	@Test
	void crearRutinaCorrectamente() {
		Rutina rutina = crearRutina(Isabel, "Dia de upper", DiaSemana.MARTES);
		
		assertEquals(1, rutina.getId());
		assertEquals("Dia de upper", rutina.getNombre());
		assertSame(Isabel, rutina.getUsuario());
		assertEquals(DiaSemana.MARTES, rutina.getDiaSemana());
	}
	
	@Test
	void idSecuenciales() {
		assertEquals(1, crearRutina(Isabel, "A",DiaSemana.LUNES).getId());
		assertEquals(2, crearRutina(Isabel, "B",DiaSemana.MARTES).getId());
		assertEquals(3, crearRutina(Michael, "C",DiaSemana.LUNES).getId());
	}
	@Test
	void crearEnDiaOcupado() {
		 crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		 assertThrows(DiaRutinaDuplicadoException.class, ()->  crearRutina(Isabel, "A",DiaSemana.LUNES));
		 assertEquals(1, rutinaService.listarRutinas(Isabel).size());
	}
	@Test
	void crearRutinaDuplicada() {
		
		Rutina rutina1=  crearRutina(Isabel, "Dia de upper",DiaSemana.LUNES);
		assertThrows(DiaRutinaDuplicadoException.class, ()->  crearRutina(Isabel, "A",DiaSemana.LUNES));
		
		//////////////////?????
		Optional<Rutina> lunes = rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES); //Puede no haber rutina un dia (dia de descanso)
		assertSame(rutina1, lunes.get());
		assertEquals("Dia de upper", lunes.get().getNombre());
	}
	
	@Test
	void rutinaDuplicadaNoConsumeId() {
		crearRutina(Isabel, "Dia de upper",DiaSemana.LUNES);
		assertThrows(DiaRutinaDuplicadoException.class, ()->  crearRutina(Isabel, "A",DiaSemana.LUNES));
		Rutina siguiente= crearRutina(Isabel, "A",DiaSemana.MARTES);
		assertEquals(2, siguiente.getId());
		
	}
	@Test
	void dosUsuariosConRutinaElMismoDia() {
		Rutina isa = crearRutina(Isabel, "Dia de upper",DiaSemana.LUNES);
		Rutina michael = crearRutina(Michael, "Dia de upper",DiaSemana.LUNES);
		assertNotSame(isa, michael);
		assertEquals(1, rutinaService.listarRutinas(Isabel).size());
		assertEquals(1, rutinaService.listarRutinas(Michael).size());
		
	}
	@Test
	void rutinaCadaDiaSemana() {
		DiaSemana[] dias = { DiaSemana.LUNES,  DiaSemana.MARTES,  DiaSemana.MIERCOLES,
				 DiaSemana.JUEVES, DiaSemana.VIERNES, DiaSemana.SABADO, DiaSemana.DOMINGO};
		
	for(DiaSemana dia : dias) {
		crearRutina(Isabel, "Rutina " + dia.name(), dia);
	}
	assertEquals(7, rutinaService.listarRutinas(Isabel).size());
	for(DiaSemana dia : dias) {
		assertTrue(rutinaService.buscarRutinaPorDia(Isabel, dia).isPresent());
	}
	}
	@Test
	void crearRutinaUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, () ->crearRutina(null, "Dia de Pierna", DiaSemana.LUNES));
	}
	@Test
	void crearConNombreInvalido() {
		assertThrows(NombreNullException.class, () ->crearRutina(Isabel, "", DiaSemana.LUNES));
		assertThrows(NombreNullException.class, () ->crearRutina(Isabel, null, DiaSemana.LUNES));
		assertTrue(rutinaService.listarRutinas(Isabel).isEmpty());
		assertEquals(1, crearRutina(Isabel, "Dia de Pierna", DiaSemana.LUNES).getId());
	}
	@Test
	void crearConDiaNulo() {
		assertThrows(DiaSemanaInvalidaException.class, () -> crearRutina(Isabel, "Dia de Pierna", null));
		assertTrue(rutinaService.listarRutinas(Isabel).isEmpty());
		assertEquals(1, crearRutina(Isabel, "Dia de Pierna", DiaSemana.LUNES).getId());
	}
	//////////////////////////Pruebas de buscar rutina por dia///////////////////
	@Test
	void buscarCorrectamente() {
		Rutina lunes = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		Rutina martes = crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		assertSame(lunes, rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES).get()); //get() se usa para compara la rutina en si, ya sabemos que hay una rutina, necesitamos obtenerla, sacarla
		assertSame(martes, rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.MARTES).get());
	}
	@Test
	void buscarRutinaPorDiaSinRutina() {
		crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		assertFalse(rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES).isPresent());
	}
	@Test
	void devuelveLaRutinaCorrecta() {
		crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		assertFalse(rutinaService.buscarRutinaPorDia(Michael, DiaSemana.MARTES).isPresent());
	}
	@Test
	void cadaUsuarioTieneSuPropiaRutina() {
		Rutina isa = crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		Rutina michael= crearRutina(Michael, "Dia de Upper",DiaSemana.MARTES);
		
		assertSame(isa, rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.MARTES).get());
		assertSame(michael, rutinaService.buscarRutinaPorDia(Michael, DiaSemana.MARTES).get());
	}
	
	@Test
	void identificaAlUsuarioPorDocumento() {
		//Crea otro objeto Usuario con el mismo documento de Isabel pero diferente id. Dos usuarios con el mismo documento son la misma persona
		Rutina rutina = crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		Usuario user = new Usuario(99, "100", "Isabel", 19, 1.7, Objetivo.HIPERTROFIA, "Clave189");
		assertSame(rutina, rutinaService.buscarRutinaPorDia(user, DiaSemana.MARTES).get());
	}
	@Test
	void buscarConUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, ()-> rutinaService.buscarRutinaPorDia(null, DiaSemana.LUNES));
	}
	/////////////////////////////////Pruebas de listar//////////////////////////////
	@Test
	void listarDeUsuariosSinRutinas() {
		assertTrue(rutinaService.listarRutinas(Isabel).isEmpty());
	}
	@Test
	void devuelveLasDelUsuarioEnOrden() {
		
		Rutina rutina1 = crearRutina(Isabel, "Dia de Pierna",DiaSemana.VIERNES);
		crearRutina(Michael, "Dia de Upper",DiaSemana.MARTES);
		Rutina rutina2 = crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		Rutina rutina3 = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		List<Rutina> rutinas = rutinaService.listarRutinas(Isabel);
		assertEquals(3, rutinas.size());
		assertSame(rutina1, rutinas.get(0));
		assertSame(rutina2, rutinas.get(1));
		assertSame(rutina3, rutinas.get(2));
	}
	@Test
	void devuelveUnaCopia(){
		crearRutina(Isabel, "Dia de Pierna",DiaSemana.VIERNES);
		List<Rutina> rutinas = rutinaService.listarRutinas(Isabel);
		rutinas.clear();
		//listarRutinas devuelve una copia nueva en cada llamada, llamar a clear no debe afertar las rutinas originales
		assertEquals(1, rutinaService.listarRutinas(Isabel).size()); // vuelve a pedir la lista y verifica que sigue habiendo una rutina
	}

	@Test
	void listarUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, () -> rutinaService.listarRutinas(null));
	}
	/////////////////////////Pruebas de editar la rutina cambiando su dia//////////////////////////
	@Test
	void cambiaElDiaYBuscaBien() {
		Rutina rutina1 = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		rutinaService.editarRutina(rutina1, DiaSemana.MIERCOLES);
		
		assertEquals(DiaSemana.MIERCOLES, rutina1.getDiaSemana());
		assertFalse(rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES).isPresent());
		assertSame(rutina1, rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.MIERCOLES).get());
	}
	@Test
	void conservaLosEjercicios() {
		Rutina rutina = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		agregarEjercicio(rutina, bulgara);
		agregarEjercicio(rutina, sentadilla);
		rutinaService.editarRutina(rutina, DiaSemana.LUNES);
		assertEquals(2, rutinaService.obtenerEjercicios(rutina).size());
	}
	@Test
	void cambiarAUnDiaOcupado() {
		Rutina lunes= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		assertThrows(DiaRutinaDuplicadoException.class, ()-> rutinaService.editarRutina(lunes, DiaSemana.MARTES));
		assertEquals(DiaSemana.LUNES, lunes.getDiaSemana());
		assertSame(lunes, rutinaService.buscarRutinaPorDia(Isabel, DiaSemana.LUNES).get());
	}
	@Test
	void cambiarAlMismoDia() {
		Rutina rutina= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		rutinaService.editarRutina(rutina, DiaSemana.LUNES);
		assertEquals(DiaSemana.LUNES, rutina.getDiaSemana());
		
	}
	@Test
	void diaOcupadoPorOtroUsuario() {
		crearRutina(Isabel, "Dia de Pierna",DiaSemana.MARTES);
		Rutina rut2 = crearRutina(Michael, "Dia de Pierna",DiaSemana.LUNES);
		rutinaService.editarRutina(rut2, DiaSemana.MARTES);
		assertEquals(DiaSemana.MARTES, rut2.getDiaSemana());
	}
	@Test
	void diaLiberadoSeVuelveAUsar() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		Rutina rutina2= crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		rutinaService.editarRutina(rutina1, DiaSemana.MIERCOLES); //libera el lunes
		rutinaService.editarRutina(rutina2, DiaSemana.LUNES); //El lunes deberia estar libre para volverse a usar
		assertEquals(DiaSemana.LUNES, rutina2.getDiaSemana());
		assertEquals(DiaSemana.MIERCOLES, rutina1.getDiaSemana());
		
	}
	@Test
	void editarRutinaNula() {
		assertThrows(RutinaInvalidaException.class, ()-> rutinaService.editarRutina(null, DiaSemana.JUEVES));
		
	}
	@Test
	void editarRutinaConDiaNulo() {
		Rutina rutina= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		assertThrows(DiaSemanaInvalidaException.class, ()-> rutinaService.editarRutina(rutina, null));
		assertEquals(DiaSemana.LUNES, rutina.getDiaSemana());
		
	}
	
//////////////////////////////////Pruebas de agregar un ejercicio/////////////////////////
	@Test
	void agregarCorrectamente() {
		Rutina rutina= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		RutinaEjercicio rutE= rutinaService.agregarEjercicio(rutina, bulgara, 3, 8);
		
		assertSame(rutina, rutE.getRutina());
		assertSame(bulgara, rutE.getEjercicio());
		assertEquals(1, rutE.getOrdenEnRutina());
		assertEquals(3, rutE.getSetsObjetivo());
		assertEquals(8, rutE.getObjetivo());
	}
	@Test
	void seAsignaAlFinalDeLaRutina() {
		Rutina rutina= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		assertEquals(1, agregarEjercicio(rutina, bulgara).getOrdenEnRutina());
		assertEquals(2, agregarEjercicio(rutina, sentadilla).getOrdenEnRutina());
		assertEquals(3, agregarEjercicio(rutina, plancha).getOrdenEnRutina());
	}
	@Test
	void cadaRutinaTieneSuPropioOrden() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		Rutina rutina2= crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		agregarEjercicio(rutina1, bulgara);
		agregarEjercicio(rutina1, sentadilla);
		RutinaEjercicio ejercicioMartes = agregarEjercicio(rutina2, plancha);
		assertEquals(1, ejercicioMartes.getOrdenEnRutina());
	}
	@Test
	void idsSecuencialesEntreRutinas() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		Rutina rutina2= crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		assertEquals(1, agregarEjercicio(rutina1, bulgara).getId());
		assertEquals(2, agregarEjercicio(rutina1, sentadilla).getId());
		assertEquals(3, agregarEjercicio(rutina2, plancha).getId());
	}
	@Test
	void agregarUnEjercicioConRutinaNula() {
		assertThrows(RutinaInvalidaException.class, ()-> rutinaService.agregarEjercicio(null, bulgara, 3, 8));
	}
	@Test
	void agregarEjercicioNulo() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		assertThrows(EjercicioInvalidoException.class, ()-> rutinaService.agregarEjercicio(rutina1, null, 4, 10));
		assertTrue(rutinaService.obtenerEjercicios(rutina1).isEmpty());
		
	}
	@Test
	void agregarEjercicioConDatosInvalidos() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		assertThrows(GymTrackerException.class, () -> rutinaService.agregarEjercicio(rutina1, bulgara, 0, 10)); //Ejercicio con 0 sets
		assertThrows(GymTrackerException.class, () -> rutinaService.agregarEjercicio(rutina1, bulgara, 4, 0)); //Ejercicio con 0 reps
		assertTrue(rutinaService.obtenerEjercicios(rutina1).isEmpty());
		RutinaEjercicio ejercicio = agregarEjercicio(rutina1, bulgara);
		assertEquals(1, ejercicio.getId());
		assertEquals(1, ejercicio.getOrdenEnRutina());
	}
	@Test
	void maximoDiezEjerciciosPorRutina() {
		Rutina rutina1= crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		for(int i =0; i<10; i++) {
			Ejercicio ejercicio = new Ejercicio (100+i, "E-" + i, "Ejercicio " +i, "Grupo", "Categoria", TipoEjercicio.PESO);
			agregarEjercicio(rutina1, ejercicio);
		}
		assertThrows(OrdenInvalidoException.class, ()-> agregarEjercicio(rutina1, bulgara));
		assertEquals(10, rutinaService.obtenerEjercicios(rutina1).size());
	}
	/////////////////////Pruebas de obtener ejercicios/////////////////////////
	@Test
	void rutinaSinEjercicios() {
		Rutina rutina = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		assertTrue( rutinaService.obtenerEjercicios(rutina).isEmpty());
	}
	@Test
	void devuelveEnOrdenDeAgregacion() {
		Rutina rutina = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		RutinaEjercicio primero = agregarEjercicio(rutina, bulgara);
		RutinaEjercicio segundo = agregarEjercicio(rutina, sentadilla);
		RutinaEjercicio tercero = agregarEjercicio(rutina, plancha);
		List<RutinaEjercicio> ejercicios =rutinaService.obtenerEjercicios(rutina);
		
		assertEquals(3,  ejercicios.size());
		assertSame(primero, ejercicios.get(0));
		assertSame(segundo, ejercicios.get(1));
		assertSame(tercero, ejercicios.get(2));
	}
	@Test
	void noMezclaLaRutinas() {
		Rutina rutina1 = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		Rutina rutina2 = crearRutina(Isabel, "Dia de Upper",DiaSemana.MARTES);
		agregarEjercicio(rutina1, sentadilla);
		agregarEjercicio(rutina2, plancha);
		agregarEjercicio(rutina1, bulgara);
		List<RutinaEjercicio> ejercicios1 =rutinaService.obtenerEjercicios(rutina1);
		List<RutinaEjercicio> ejercicios2 =rutinaService.obtenerEjercicios(rutina2);
		
		assertEquals(2, ejercicios1.size());
		assertEquals(1, ejercicios2.size());
		assertSame(plancha, ejercicios2.get(0).getEjercicio());
	}
	@Test
	void obtenerDevuelveUnaCopia() {
		Rutina rutina1 = crearRutina(Isabel, "Dia de Pierna",DiaSemana.LUNES);
		agregarEjercicio(rutina1, bulgara);
		List<RutinaEjercicio> ejercicios1 =rutinaService.obtenerEjercicios(rutina1);
		ejercicios1.clear();
		assertEquals(1, rutinaService.obtenerEjercicios(rutina1).size());
	}
	@Test
	void rutinaNula() {
		assertThrows(RutinaInvalidaException.class, ()-> rutinaService.obtenerEjercicios(null));
	}
}
