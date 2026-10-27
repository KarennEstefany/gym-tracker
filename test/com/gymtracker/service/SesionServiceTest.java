package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.gymtracker.exceptions.DuracionInvalidaException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.EstructuraVaciaException;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.SesionEnCursoException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.SinRutinaParaHoyException;
import com.gymtracker.exceptions.SinSesionActivaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.exceptions.PilaVaciaException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.EstadoSesion;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.SetTiempo;
import com.gymtracker.exceptions.SinSesionActivaException;

public class SesionServiceTest {
	
	private static final LocalDate LUNES = LocalDate.of(2026, 10, 26);
	private static final LocalDate MARTES = LUNES.plusDays(1);
	private static final LocalDate MIERCOLES = LUNES.plusDays(2);
	
	private RutinaService rutinaService;
	private SesionService sesionService; 
	private Usuario usuario;
	private Ejercicio sentadilla;
	private Ejercicio plancha;
	
	@BeforeEach
	void setUp() {
		rutinaService= new RutinaService();
		sesionService = new SesionService(rutinaService);
		
		usuario = new Usuario(1, "2293013836", "Maria", 18, 1.73, Objetivo.RESISTENCIA, "Clave390");
		sentadilla = new Ejercicio(1, "SENT-01", "Sentadilla Libre", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
		plancha = new Ejercicio(2, "PLAN-01", "Plancha", "Abdominales", "Core", TipoEjercicio.TIEMPO);
		
		Rutina rutinaLunes = crearRutina(usuario, "Dia de pierna", DiaSemana.LUNES);
		agregarEjercicio(rutinaLunes, sentadilla, 1, 2, 10);
		agregarEjercicio(rutinaLunes, plancha, 2, 2, 30);
	}
	/////////////////Metodos auxiliares para las pruebas////////////////////////////////////////////////////
	private Rutina crearRutina(Usuario usuario, String nombre, DiaSemana dia) {
		return rutinaService.crearRutina(usuario, nombre, dia);
	}
	private void agregarEjercicio( Rutina rutina, Ejercicio ejercicio, int orden, int sets, int reps) {
		rutinaService.agregarEjercicio(rutina, ejercicio, sets, reps);
	}
	private SesionEntrenamiento iniciarLunes() {
		return sesionService.iniciarSesion(usuario, LUNES);
	}
	private void registrarLasDosSentadillas() {
		sesionService.registrarSetPeso(80, 10, 8, 2);
		sesionService.registrarSetPeso(85, 8, 9, 1);
	}
	private void registrarTodosLosSets() {
		registrarLasDosSentadillas();
		sesionService.registrarSetTiempo(35, 8, 2);
		sesionService.registrarSetTiempo(40, 8, 1);
	}
	/////////////////////////////////Prueba estado inicial: No hay sesionactiva, ni sets pendientes, ni sesiones///////////////////////////
	@Test 
	void serviceNuevo() {
		assertNull(sesionService.getSesionActiva());
		assertFalse(sesionService.haySetsPendientes());
		assertTrue(sesionService.listarSesiones(usuario).isEmpty());
	}
	//////////////////////Pruebas iniciar sesion////////////////////////////////////////////////////////////////////
	@Test
	void crearUnaSesionEnCurso() {
		SesionEntrenamiento sesion=iniciarLunes();
		
		assertSame(sesion, sesionService.getSesionActiva());
		assertEquals(EstadoSesion.EN_CURSO, sesion.getEstado());
		assertSame(usuario, sesion.getUsuario());
		assertTrue(sesionService.haySetsPendientes());
		
	}
	@Test
	void asociarLaRutinaDelDia() {
		SesionEntrenamiento sesion = iniciarLunes();
		assertEquals("Dia de pierna", sesion.getRutina().getNombre());
		
	}
	
	@Test 
	void primerSetPendienteEsElPrimeroEnLaRutina() {
		iniciarLunes();
		SetPlaneado primerSet = sesionService.verSetPendiente();
		
		assertSame(sentadilla, primerSet.getEjercicio());
		assertEquals(1, primerSet.getNumeroSet());
			
	}
	@Test
	void iniciarSesionConUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, () -> sesionService.iniciarSesion(null, LUNES));
	}
	@Test
	void iniciarSesionConDiaNulo() {
		assertThrows(FechaInvalidaException.class, () -> sesionService.iniciarSesion(usuario, null));
	}
	@Test
	void noHayRutina() {
		assertThrows(SinRutinaParaHoyException.class, ()-> sesionService.iniciarSesion(usuario, MIERCOLES));
		assertNull(sesionService.getSesionActiva());
		assertFalse(sesionService.haySetsPendientes());
		assertTrue(sesionService.listarSesiones(usuario).isEmpty());
	}
	
	@Test
	void rutinaSinEjercicios() {
		crearRutina(usuario, "Dia vacio prueba", DiaSemana.MARTES);
		assertThrows(RutinaInvalidaException.class, ()-> sesionService.iniciarSesion(usuario, MARTES));
		assertNull(sesionService.getSesionActiva());
		
	}
	@Test
	void inicarSesionConOtraEnCurso() {
		SesionEntrenamiento primeraSesion= iniciarLunes();
		assertThrows(SesionEnCursoException.class, () -> sesionService.iniciarSesion(usuario, LUNES.plusDays(7)));
		assertSame(primeraSesion, sesionService.getSesionActiva());
		assertEquals(1, sesionService.listarSesiones(usuario).size());
	}
	
	@Test
	void finalizarSesionYEmpezarOtra() {
		SesionEntrenamiento primeraSesion = iniciarLunes();
		sesionService.finalizarSesion();
		
		SesionEntrenamiento segundaSesion = sesionService.iniciarSesion(usuario, LUNES.plusDays(7));
		assertNotSame(primeraSesion, segundaSesion);
		assertSame(segundaSesion, sesionService.getSesionActiva());
		assertEquals(EstadoSesion.EN_CURSO, segundaSesion.getEstado());
		assertEquals(2, sesionService.listarSesiones(usuario).size());
	}
	@Test
	void anadirUnaNuevaSesion() {
		iniciarLunes();
		registrarLasDosSentadillas();
		sesionService.finalizarSesion();
		
		sesionService.iniciarSesion(usuario, LUNES.plusDays(7));
		SetPlaneado primerSet=sesionService.verSetPendiente();
		
		assertSame(sentadilla, primerSet.getEjercicio());
		assertEquals(1, primerSet.getNumeroSet());
	}
	
	@Test 
	void cadaDiaTieneUnaRutina() {
		DiaSemana[] diasSemana = {DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES,
				DiaSemana.VIERNES, DiaSemana.SABADO, DiaSemana.DOMINGO};
		
		//i=1 para que inicie en el martes, el lunes se inicializo en el setUp
		for(int i=1; i<diasSemana.length; i++) {
			Rutina rutina = crearRutina(usuario, "Rutina del dia " + diasSemana[i].name(), diasSemana[i]);
			agregarEjercicio(rutina, sentadilla, 1, 1, 10);
		}
		for (int i=0; i<diasSemana.length; i++) {
			SesionEntrenamiento sesion= sesionService.iniciarSesion(usuario, LUNES.plusDays(i));
			String esperado = (i==0) ?"Dia de pierna" : "Rutina del dia " + diasSemana[i].name();
			assertEquals(esperado, sesion.getRutina().getNombre());
			sesionService.finalizarSesion();
		}
		}
	//////////////////Pruebas de sets pendientes////////////////////
	/// 
	@Test
	void verSetPendiente() {
		assertThrows(SinSesionActivaException.class, ()-> sesionService.verSetPendiente());}
	@Test
	void verSetNoLoDesencola() {
		iniciarLunes();
		SetPlaneado primerSet = sesionService.verSetPendiente();
		SetPlaneado segundoSet = sesionService.verSetPendiente();
		
		assertSame(primerSet, segundoSet);
	}
	@Test
	void verSetConSesionFinalizada() {
		iniciarLunes();
		sesionService.finalizarSesion();
		assertThrows(SesionYaFinalizadaException.class, () -> sesionService.verSetPendiente());
	}
	@Test
	void noHaySetRestantes() {
		iniciarLunes();
		registrarTodosLosSets();
		
		assertFalse(sesionService.haySetsPendientes());
		assertThrows(EstructuraVaciaException.class, ()->sesionService.verSetPendiente());
		
	}
	//////////////////////////Pruebas de registrar un set de un ejercicio de peso////////////////////////////////////////////////
	@Test
	void registrarCorrectamente() {
		iniciarLunes();
		SetPeso setP= sesionService.registrarSetPeso(80.5, 10, 7, 2);
		assertSame(sentadilla, setP.getEjercicio());
		assertEquals(1, setP.getNumeroSet());
		assertEquals(80.5, setP.getPeso(), 0.001);
		assertEquals(10, setP.getRepeticiones());
		assertEquals(7, setP.getRpe());
		assertEquals(2, setP.getRir());
	}
	@Test
	void avanzaAlSiguienteSet() {
		iniciarLunes();
		sesionService.registrarSetPeso(82, 7, 8, 1);
		
		SetPlaneado siguiente= sesionService.verSetPendiente();
		assertSame(sentadilla, siguiente.getEjercicio());
		assertEquals(2, siguiente.getNumeroSet());
	}
	@Test
	void ordenCorrectoDeLaRutina() {
		iniciarLunes();
		registrarLasDosSentadillas();
		
		SetPlaneado siguiente= sesionService.verSetPendiente();
		assertSame(plancha, siguiente.getEjercicio());
		assertEquals(1, siguiente.getNumeroSet());
	}
	@Test
	void noHaySesionActiva() {
		assertThrows(SinSesionActivaException.class, () ->sesionService.registrarSetPeso(10, 3, 2, 5));
	}
	@Test
	void sesionYaFinalizada() {
		iniciarLunes();
		sesionService.finalizarSesion();
		assertThrows(SesionYaFinalizadaException.class, () ->sesionService.registrarSetPeso(10, 3, 2, 5));
	}
	@Test
	void registrarSetSinSetPendientes() {
		iniciarLunes();
		registrarTodosLosSets();
		assertThrows(EstructuraVaciaException.class, () ->sesionService.registrarSetPeso(10, 3, 2, 5));
	}
	@Test
	void intentarSetPesoEnEjercicioTiempo() {
		iniciarLunes();
		registrarLasDosSentadillas();
		SetPlaneado setPendiente= sesionService.verSetPendiente();
		assertThrows(EjercicioInvalidoException.class, ()-> sesionService.registrarSetPeso(40, 9, 4, 3));
		assertSame(setPendiente, sesionService.verSetPendiente());	}
	@Test
	void registrarDatosInvalidos() {
		iniciarLunes();
		SetPlaneado setPendiente= sesionService.verSetPendiente();
		assertThrows(GymTrackerException.class, () ->sesionService.registrarSetPeso(-4, 3, 2, 5)); //Peso negativo
		assertThrows(GymTrackerException.class, () ->sesionService.registrarSetPeso(10, 0, 2, 5)); //Repeticiones =0
		assertThrows(GymTrackerException.class, () ->sesionService.registrarSetPeso(10, 3, 14, 5)); //RPE mayor a 10
		
		assertSame(setPendiente, sesionService.verSetPendiente());
	}
//////////////////////////Pruebas de registrar un set de un ejercicio de tiempo////////////////////////////////////////////////
	@Test
	void registrarSetTiempoCorrectamente() {
		iniciarLunes();
		registrarLasDosSentadillas();
		
		SetTiempo setPendiente1= sesionService.registrarSetTiempo(35, 6, 3);
		SetTiempo setPendiente2= sesionService.registrarSetTiempo(45, 7, 2);
		
		assertSame(plancha, setPendiente1.getEjercicio());
		assertSame(plancha, setPendiente2.getEjercicio());
		assertEquals(1, setPendiente1.getNumeroSet());
		assertEquals(2, setPendiente2.getNumeroSet());
		assertFalse(sesionService.haySetsPendientes());
	}
	@Test
	void intentarSetTiempoEnEjercicioPeso() {
		iniciarLunes();
		SetPlaneado setPendiente= sesionService.verSetPendiente();
		assertThrows(EjercicioInvalidoException.class, ()-> sesionService.registrarSetTiempo(45, 7, 2));
		assertSame(setPendiente, sesionService.verSetPendiente());	}
 
	@Test
	void registrarTiempoInvalido() {
		iniciarLunes();
		registrarLasDosSentadillas();
		SetPlaneado setPendiente= sesionService.verSetPendiente();
		assertThrows(DuracionInvalidaException.class, ()-> sesionService.registrarSetTiempo(-45, 7, 2));//Tiempo negativo
		assertThrows(DuracionInvalidaException.class, ()-> sesionService.registrarSetTiempo(-0, 7, 2));//Tiempo =0
		
		assertSame(setPendiente, sesionService.verSetPendiente());	}
	@Test
	void noHaySesionActivaRegistrarTiempo() {
		assertThrows(SinSesionActivaException.class, () ->sesionService.registrarSetTiempo(100, 3, 2));
	}
	
	///////////////////////////Pruebas de deshacer el ultimo set//////////////////////////
	@Test
	void deshacerSinSesionActiva() {
		assertThrows(SinSesionActivaException.class, () ->sesionService.deshacerUltimoSet());
	}
	@Test
	void noHaySetsRegistrados() {
		iniciarLunes();
		assertThrows(PilaVaciaException.class, () ->sesionService.deshacerUltimoSet());
	}
	@Test
	void deshacerConSesionYaFinalizada() {
		iniciarLunes();
		sesionService.registrarSetPeso(20, 12, 5, 5);
		sesionService.finalizarSesion();
		assertThrows(SesionYaFinalizadaException.class, () ->sesionService.deshacerUltimoSet());
	}
	@Test
	void devuelveElSetAlFrenteDeLaColaa() {
		iniciarLunes();
		SetPlaneado setPrueba= sesionService.verSetPendiente();
		sesionService.registrarSetPeso(30, 15, 6, 4);
		sesionService.deshacerUltimoSet();
		
		assertSame(setPrueba, sesionService.verSetPendiente());
		assertTrue(sesionService.haySetsPendientes());
		
	}
	@Test
	void seRespetaOrdenLIFO() {
		iniciarLunes();
		registrarTodosLosSets();
		sesionService.deshacerUltimoSet();
		assertSame(plancha, sesionService.verSetPendiente().getEjercicio());
		
		sesionService.deshacerUltimoSet();
		assertSame(plancha, sesionService.verSetPendiente().getEjercicio());
		sesionService.deshacerUltimoSet();
		SetPlaneado segundaSentadilla= sesionService.verSetPendiente();
		assertSame(sentadilla, sesionService.verSetPendiente().getEjercicio());
		assertEquals(2, segundaSentadilla.getNumeroSet());
		sesionService.deshacerUltimoSet();
		SetPlaneado primeraSentadilla= sesionService.verSetPendiente();
		assertSame(sentadilla, sesionService.verSetPendiente().getEjercicio());
		assertEquals(1, primeraSentadilla.getNumeroSet());
	}
	@Test
	void deshacerDespuesDeRegistrar() {
		iniciarLunes();
		registrarTodosLosSets();
		assertFalse(sesionService.haySetsPendientes());
		
		sesionService.deshacerUltimoSet();
		assertTrue(sesionService.haySetsPendientes());
		assertSame(plancha, sesionService.verSetPendiente().getEjercicio());
	}
	@Test 
	void deshacerYregistrarNuevosDatos() {
		iniciarLunes();
		sesionService.registrarSetPeso(75, 10, 8, 2);
		sesionService.deshacerUltimoSet();
		
		SetPeso nuevoSet =sesionService.registrarSetPeso(75, 11, 8, 1);
		assertEquals(1, nuevoSet.getNumeroSet());
		assertEquals(75, nuevoSet.getPeso());
		assertEquals(2, sesionService.verSetPendiente().getNumeroSet());
	}
	@Test
	void deshacerUnaSolaVez() {
		iniciarLunes();
		sesionService.registrarSetPeso(80, 20, 7, 2);
		sesionService.registrarSetPeso(85, 17, 6, 1);
		sesionService.deshacerUltimoSet();
		
		SetPlaneado setPendiente = sesionService.verSetPendiente();
		assertEquals(2, setPendiente.getNumeroSet());
	}
	@Test
	void seReiniciaLaPilaEnCadaSesion() {
		iniciarLunes();
		registrarTodosLosSets();
		sesionService.finalizarSesion();
		sesionService.iniciarSesion(usuario, LUNES.plusDays(7));
		
		//No debe haber sets porque la pila se reinicio al iniciar una nueva sesion
		assertThrows(PilaVaciaException.class, () -> sesionService.deshacerUltimoSet());
		
	}
	
	/////////////////////////////Pruebas de finalizar sesion///////////////////////////////
	@Test
	void finalizarSinSesionActiva() {
		assertThrows(SinSesionActivaException.class, () -> sesionService.finalizarSesion());
		
	}
	@Test
	void finalizarCambiaElEstado() {
		SesionEntrenamiento sesionE= iniciarLunes();
		registrarTodosLosSets();
		sesionService.finalizarSesion();
		assertEquals(EstadoSesion.FINALIZADA, sesionE.getEstado());
		
	}
	@Test
	void finalizarConSetsPendientes() {
		SesionEntrenamiento sesionE = iniciarLunes();
		sesionService.registrarSetPeso(85, 20, 7, 2);
		sesionService.finalizarSesion();
		
		assertEquals(EstadoSesion.FINALIZADA, sesionE.getEstado());
	}
	@Test
	void finalizarUnaSesionFinalizada() {
		iniciarLunes();
		sesionService.finalizarSesion();
		assertThrows(SesionYaFinalizadaException.class, () -> sesionService.finalizarSesion());
		
	}
	
	////////////////////////////Pruebas de listar Sesiones///////////////////////////////////////
	@Test
	void listarSesionesCuandoNoHaySesiones() {
		assertTrue(sesionService.listarSesiones(usuario).isEmpty());
	}
	@Test
	void seDevuelvenEnOrden() {
		SesionEntrenamiento primeraSesion = iniciarLunes();
		sesionService.finalizarSesion();
		SesionEntrenamiento segundaSesion = sesionService.iniciarSesion(usuario, LUNES.plusDays(7));
		sesionService.finalizarSesion();
		SesionEntrenamiento terceraSesion = sesionService.iniciarSesion(usuario, LUNES.plusDays(14));
		List<SesionEntrenamiento> sesiones = sesionService.listarSesiones(usuario);
		
		assertEquals(3, sesiones.size());
		assertSame(primeraSesion, sesiones.get(0));
		assertSame(segundaSesion, sesiones.get(1));
		assertSame(terceraSesion, sesiones.get(2));
	}
	@Test
	void devuelveSoloLasDelUsuario() {
		Usuario usuario2 = new Usuario(2, "2293013296", "Ana", 18, 1.73, Objetivo.FUERZA, "Clave3900");
		Rutina rutina = crearRutina(usuario2, "Rutina del Ana " ,DiaSemana.LUNES);
		agregarEjercicio(rutina, sentadilla, 1, 1, 10);
		
		SesionEntrenamiento sesionMaria=iniciarLunes();
		sesionService.finalizarSesion();
		SesionEntrenamiento sesionAna = sesionService.iniciarSesion(usuario2, LUNES);
		List<SesionEntrenamiento> sesionesMaria = sesionService.listarSesiones(usuario);
		List<SesionEntrenamiento> sesionesAna = sesionService.listarSesiones(usuario2);
		
		assertEquals(1, sesionesMaria.size());
		assertSame(sesionMaria, sesionesMaria.get(0));
		assertEquals(1, sesionesAna.size());
		assertSame(sesionAna, sesionesAna.get(0));
		
	}
	@Test
	void noAfectaElHistorialInterno() {
		iniciarLunes();
		List<SesionEntrenamiento> resultado = sesionService.listarSesiones(usuario);
		resultado.clear();
		
		assertEquals(1, sesionService.listarSesiones(usuario).size());
	}
	}

	





