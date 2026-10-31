package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.GrupoMuscularInvalidoException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.exceptions.PesoInvalidoException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.structures.TablaHash;

/**
 * Pruebas de RecordService.
 * <p>
 * El historial de sets solo se puede crear a través de SesionService (iniciar una sesión y
 * registrar sets), así que cada prueba arma su historial con esas mismas operaciones.
 * <p>
 * HISTORIAL BASE que carga el método cargarHistorialBase, en el orden en que se registra:
 * <pre>
 *  #  Usuario  Fecha        Ejercicio (grupo)           Set
 *  1  Sofia    lun 26 oct   Sentadilla (Cuádriceps)     80 kg x 10
 *  2  Sofia    lun 26 oct   Sentadilla (Cuádriceps)     90 kg x 5
 *  3  Sofia    lun 26 oct   Plancha (Abdominales)       45 s
 *  4  Sofia    mar 27 oct   Press banca (Pectoral)      60 kg x 8
 *  5  Ana      lun 26 oct   Sentadilla (Cuádriceps)     100 kg x 3
 *  6  Sofia    lun 2 nov    Sentadilla (Cuádriceps)     90 kg x 8
 *  7  Sofia    lun 2 nov    Sentadilla (Cuádriceps)     85 kg x 6
 * </pre>
 * Maria es una usuaria sin sesiones; se usa para los casos "sin datos" y para armar
 * historiales a medida dentro de cada prueba.
 */
public class RecordServiceTest {
	private static final LocalDate LUNES = LocalDate.of(2026, 10, 26);
	private static final LocalDate MARTES = LUNES.plusDays(1) ;
	private static final LocalDate PROXIMO_LUNES = LUNES.plusDays(7) ;
	
	private RutinaService rutinaService;
	private SesionService sesionService;
	private RecordService recordService;
	
	private Usuario sofia;
	private Usuario ana;
	private Usuario maria;
	
	private Ejercicio sentadilla;
	private Ejercicio extension;
	private Ejercicio plancha;
	private Ejercicio pressBanca;
	
	@BeforeEach
	void setUp() {
		rutinaService= new RutinaService();
		sesionService= new SesionService(rutinaService);
		recordService= new RecordService(sesionService);
		
		sofia = new Usuario (1, "100", "Sofia", 19, 1.7, Objetivo.FUERZA, "Clave13345");
		ana = new Usuario (2, "200", "Ana", 18, 1.6, Objetivo.HIPERTROFIA, "Clave 23365");
		maria = new Usuario (3, "300", "Maria", 20, 1.75, Objetivo.RESISTENCIA, "Clave8902");
		
		sentadilla = new Ejercicio( 1, "SENT-01", "Sentadilla libre", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
		extension = new Ejercicio( 2, "EXT-01", "Extension", "Cuádriceps", "Piernas", TipoEjercicio.PESO);
		plancha = new Ejercicio( 3, "PLAN-01", "Plancha", "Abdominales", "Core", TipoEjercicio.TIEMPO);
		pressBanca = new Ejercicio( 4, "PRESS-01", "Press de banca", "Pectoral", "Superior", TipoEjercicio.PESO);
		
		Rutina lunesSofia = rutinaService.crearRutina(sofia, "Dia de pierna", DiaSemana.LUNES);
		rutinaService.agregarEjercicio(lunesSofia, sentadilla, 2, 10);
		rutinaService.agregarEjercicio(lunesSofia, plancha, 1, 10);
		Rutina martesSofia = rutinaService.crearRutina(sofia, "Dia de upper", DiaSemana.MARTES);
		rutinaService.agregarEjercicio(martesSofia, pressBanca, 1, 10);
		Rutina lunesAna = rutinaService.crearRutina(ana, "Dia de pierna", DiaSemana.LUNES);
		rutinaService.agregarEjercicio(lunesAna, sentadilla, 1, 10);
	}
	
	////////////////////////////Metodos auxiliares///////////////////////////////////////
	
	/** Metodo que carga el historial base de sesiones (entre Sofia y Ana) descrito en la documentacion del test*/
	private void cargarHistorialBase() {
		sesionService.iniciarSesion(sofia, LUNES);
		sesionService.registrarSetPeso(80, 10, 8, 2);
		sesionService.registrarSetPeso(90, 5, 8, 2);
		sesionService.registrarSetTiempo(45, 8, 2);
		sesionService.finalizarSesion();
		
		sesionService.iniciarSesion(sofia, MARTES);
		sesionService.registrarSetPeso(60, 8, 8, 2);
		sesionService.finalizarSesion();
		
		sesionService.iniciarSesion(ana, LUNES);
		sesionService.registrarSetPeso(100, 3, 8, 2);
		sesionService.finalizarSesion();
		
		sesionService.iniciarSesion(sofia, PROXIMO_LUNES);
		sesionService.registrarSetPeso(90, 8, 8, 2);
		sesionService.registrarSetPeso(85, 6, 8, 2);
		sesionService.finalizarSesion(); // finalizar la sesion aunque la plancha este pendiente esta permitido
	}
	
	/**Crea una rutina para Maria para el dia indicado con el numero de sets para la sentadilla que se defina*/
	private void crearRutinaDeMaria(DiaSemana dia, int seriesSentadilla) {
		Rutina rutina = rutinaService.crearRutina(maria, "Rutina " + dia.name()  , dia);
		rutinaService.agregarEjercicio(rutina, sentadilla, seriesSentadilla, 10);
	}
	
	private void registrarSetDeMaria(LocalDate fecha) {
		sesionService.iniciarSesion(maria, fecha);
		sesionService.registrarSetPeso(60, 10, 8, 2);
		sesionService.finalizarSesion();
	}
	
	//==========================   Prueba del constructor  ===================================//
	@Test
	void sesionServiceNulo() {
		assertThrows(ObjetoNuloException.class, () -> new RecordService(null));
	}
	
	/////////////////// Prueba del R11: obtenerRecordPeso  //////////////////////////////////

	@Test
	void recordDePesoCorrecto() {
		cargarHistorialBase();
		
		Optional<SetPeso> record = recordService.obtenerRecordPeso(sofia, sentadilla);
		// Se tiene que verificar que existe el set de record antes de sacarlo
		assertTrue(record.isPresent());
		assertEquals(90, record.get().getPeso(), 0.001);
	}
	// En caso de empate de peso, el record es el primero que se logro
	@Test
	void casoEmpate() {
		cargarHistorialBase();
		
		SetPeso record = recordService.obtenerRecordPeso(sofia, sentadilla).get();
		
		assertEquals(90, record.getPeso(), 0.001);
		assertEquals(5, record.getRepeticiones());
	}
	
	@Test
	void segundoCasoEmpate() {
		crearRutinaDeMaria(DiaSemana.LUNES, 2);
		sesionService.iniciarSesion(maria, LUNES);
		SetPeso primerSet = sesionService.registrarSetPeso(100, 6, 8, 2);
		sesionService.registrarSetPeso(100, 5, 8, 2);
		assertSame(primerSet, recordService.obtenerRecordPeso(maria, sentadilla).get());
	}
	
	@Test 
	void elRecordSeActualizaSolo() {
		crearRutinaDeMaria(DiaSemana.LUNES, 1);
		
		sesionService.iniciarSesion(maria, LUNES);
		sesionService.registrarSetPeso(100, 8, 8, 2);
		sesionService.finalizarSesion();
		assertEquals(100, recordService.obtenerRecordPeso(maria, sentadilla).get().getPeso(), 0.001);
		
		sesionService.iniciarSesion(maria, PROXIMO_LUNES);
		sesionService.registrarSetPeso(102.5, 5, 8, 2);
		sesionService.finalizarSesion();
		assertEquals(102.5, recordService.obtenerRecordPeso(maria, sentadilla).get().getPeso(), 0.001);
	}
	
	@Test
	void pesoMenorNoCambiaElRecord() {
		crearRutinaDeMaria(DiaSemana.LUNES, 2);
		sesionService.iniciarSesion(maria, LUNES);
		sesionService.registrarSetPeso(100, 8, 8, 2);
		sesionService.registrarSetPeso(60, 15, 8, 2);
		
		assertEquals(100, recordService.obtenerRecordPeso(maria, sentadilla).get().getPeso(), 0.001);
		
	}
	@Test
	void unSetDeshechoNoSuma() {
		crearRutinaDeMaria(DiaSemana.LUNES, 2);
		sesionService.iniciarSesion(maria, LUNES);
		sesionService.registrarSetPeso(100, 8, 8, 2);
		sesionService.registrarSetPeso(105, 5, 8, 2);
		assertEquals(105, recordService.obtenerRecordPeso(maria, sentadilla).get().getPeso(), 0.001);
		sesionService.deshacerUltimoSet();
		
		SetPeso record = recordService.obtenerRecordPeso(maria, sentadilla).get();
		assertEquals(100, record.getPeso());
		assertEquals(8, record.getRepeticiones());
	}
	
	@Test
	void elRecordEsElMismoQueElRegistrado() { //Verifica si es la misma instancia
		crearRutinaDeMaria(DiaSemana.LUNES, 1);
		sesionService.iniciarSesion(maria, LUNES);
		
		SetPeso registrado = sesionService.registrarSetPeso(80, 6, 9, 2);
		assertSame(registrado, recordService.obtenerRecordPeso(maria, sentadilla).get());
		
	}
	@Test
	void conservaLosDatos() {
		cargarHistorialBase();
		
		SetPeso record = recordService.obtenerRecordPeso(sofia, sentadilla).get();
		
		assertSame(sentadilla, record.getEjercicio());
		assertEquals(5, record.getRepeticiones());
		assertEquals(LUNES, record.getSesion().getFecha());
	}
	
	@Test
	void soloConsideraLosSetsDelUsuario() {
		cargarHistorialBase();
		
		assertEquals(90, recordService.obtenerRecordPeso(sofia, sentadilla).get().getPeso(), 0.001);
		assertEquals(100, recordService.obtenerRecordPeso(ana, sentadilla).get().getPeso(), 0.001);
		
	}
	
	@Test
	void soloConsideraElEjercicioPedido() {
		cargarHistorialBase();
		SetPeso record = recordService.obtenerRecordPeso(sofia, pressBanca).get();
		
		assertEquals(60, record.getPeso(), 0.001);
		assertEquals(8, record.getRepeticiones());
	}
	@Test
	void ejercicioNoPracticadoOptionalVacio() {
		cargarHistorialBase();
		
		//isPresent(0 devuelve true si la caja(Optional) trae un valor, en este caso no lo trae
		assertFalse(recordService.obtenerRecordPeso(sofia, extension).isPresent());
		
	}
	
	@Test
	void usuarioSinSesionesOptionalVacio() {
		cargarHistorialBase();
		
		//La caja debe estar vacia 
		assertTrue(recordService.obtenerRecordPeso(maria, sentadilla).isEmpty());
	}
	@Test
	void historialVacioOptionalVacio() {
		assertTrue(recordService.obtenerRecordPeso(sofia, sentadilla).isEmpty());
	}
	@Test
	void usuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, () -> recordService.obtenerRecordPeso(null, extension));
	}
	@Test
	void ejercicioNulo() {
		assertThrows(EjercicioInvalidoException.class,() -> recordService.obtenerRecordPeso(sofia, null));
	}
	@Test
	void intentarConEjercicioDeTiempo() {
		assertThrows(EjercicioInvalidoException.class, ()-> recordService.obtenerRecordPeso(sofia, plancha));
	}
	/////////////////////// Pruebas del R12:obtener record de repeticiones  //////////////////////////
	
	@Test
	void recordCorrecto() {
		cargarHistorialBase();
		//Con 90kg Sofia tiene 2 registros: 5 reps y 8 reps, el record son 8 reps
		SetPeso record = recordService.obtenerRecordRepeticiones(sofia, sentadilla, 90).get();
		
		assertEquals(8, record.getRepeticiones());
		assertEquals(90, record.getPeso(), 0.001);
	}
	
	@Test
	void cadaPesoTieneSuRecord() {
		cargarHistorialBase();
		
		assertEquals(10, recordService.obtenerRecordRepeticiones(sofia, sentadilla, 80).get().getRepeticiones());
		assertEquals(6, recordService.obtenerRecordRepeticiones(sofia, sentadilla, 85).get().getRepeticiones());
		
	}
	
	@Test
	void pesoNoLevantadoOptionalVacio() {
		cargarHistorialBase();
		//No hay registro de 70kg para sofia
		assertTrue(recordService.obtenerRecordRepeticiones(sofia, sentadilla, 70).isEmpty());	}
	@Test
	void empateDeRepeticiones() { //El record debe ser el primer set realizado
		crearRutinaDeMaria(DiaSemana.LUNES, 2);
		sesionService.iniciarSesion(maria, LUNES);
		
		SetPeso primerSet = sesionService.registrarSetPeso(100, 5, 8, 2);
		sesionService.registrarSetPeso(100,5, 8, 2);
		
		assertSame(primerSet, recordService.obtenerRecordRepeticiones(maria, sentadilla, 100).get());
	}
	@Test
	void repsSoloConsideraLosSetsDelUsuario() {
		cargarHistorialBase();
		
		assertTrue(recordService.obtenerRecordRepeticiones(sofia, sentadilla, 100).isEmpty());
		assertEquals(3, recordService.obtenerRecordRepeticiones(ana, sentadilla, 100).get().getRepeticiones());
	}
	@Test
	void pesoTieneToleranciaPequena() {
		cargarHistorialBase();
		// La tolerancia es 0.0001, se considera el mismo peso una diferencia de 0.00001
		assertTrue(recordService.obtenerRecordRepeticiones(sofia, sentadilla, 90.00001).isPresent());
		//Una diferencia de 0.01 ya es otro peso
		assertTrue(recordService.obtenerRecordRepeticiones(sofia, sentadilla, 90.01).isEmpty());
		
	}
	
	@Test
	void pesoCeroEsValidoDevuelveVacioSiNuncaSeUso() {
		cargarHistorialBase();
		
		assertTrue(recordService.obtenerRecordRepeticiones(sofia, sentadilla, 0).isEmpty());
	}
	
	@Test
	void pesoNegativo() {
		assertThrows( PesoInvalidoException.class, ()-> recordService.obtenerRecordRepeticiones(sofia, sentadilla, -5));
	}
	
	@Test
	void obtenerUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, () -> recordService.obtenerRecordRepeticiones(null, sentadilla, 80));
	}
	@Test
	void repsEjercicioNulo() {
		assertThrows(EjercicioInvalidoException.class, ()-> recordService.obtenerRecordRepeticiones(sofia, null, 80));
		
	}
	@Test
	void repsIntentarEjercicioDeTiempo() {
		assertThrows(EjercicioInvalidoException.class, () -> recordService.obtenerRecordRepeticiones(sofia, plancha, 0));
	}
	
	////////////////////////////// Pruebas de R13: calcular volumen semanal /////////////////////////
	
	@Test
	void cuentaCorrectamenteLosSets() {
		cargarHistorialBase();
		
		// La semana del 26 de oct al 1 de nov: sofia hizo 2 sets de cuadriceps (solo los del lunes)
		assertEquals(2, recordService.calcularVolumenSemanal(sofia, "Cuádriceps", LUNES));
		
	}
	
	@Test
	void cadaSemanaCuentaSusSets() {
		cargarHistorialBase();
		
		//Sofia hizo 2 sets de cuadriceps en cada semana
		//La respuesta dede ser 2 en cada caso
		assertEquals(2,  recordService.calcularVolumenSemanal(sofia, "Cuádriceps", LUNES));
		assertEquals(2, recordService.calcularVolumenSemanal(sofia, "Cuádriceps", PROXIMO_LUNES));
	}
	
	@Test
	void cualquierDiaDeLaSemana() {
		cargarHistorialBase();
		
		for(int i= 0; i< 7; i++) {
			assertEquals(2, recordService.calcularVolumenSemanal(sofia,"Cuádriceps", LUNES.plusDays(i)));
		}
	}
	@Test
	void cuentaSetsDeTiempoYPeso() {
		cargarHistorialBase();
		
		assertEquals(1, recordService.calcularVolumenSemanal(sofia, "Abdominales", LUNES)); //Plancha, ejercicio de tiempo
		assertEquals(1, recordService.calcularVolumenSemanal(sofia, "Pectoral", LUNES)); //Press de banca, ejercicio de peso
	}
	@Test
	void grupoSinSetsEnLaSemana() {
		cargarHistorialBase();
		
		assertEquals(0, recordService.calcularVolumenSemanal(sofia, "Glúteos", LUNES)); // nunca se ha trabajado
		assertEquals(0, recordService.calcularVolumenSemanal(sofia, "Pectoral", PROXIMO_LUNES)); // no se trabajo esa semana
	}
	
	@Test
	void soloCuentaSetsDelUsuario() {
		cargarHistorialBase();
		
		assertEquals(2, recordService.calcularVolumenSemanal(sofia, "Cuádriceps", LUNES));
		assertEquals(1, recordService.calcularVolumenSemanal(ana, "Cuádriceps", LUNES));
		assertEquals(0, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES));
	}
	@Test
	void historialVacioElVolumenEsCero() {
		assertEquals(0, recordService.calcularVolumenSemanal(sofia, "Cuádriceps", LUNES));
	}
	
	@Test
	void limitesDeLaSemanaIncluyeAmbos() {
		crearRutinaDeMaria(DiaSemana.LUNES, 1);
		crearRutinaDeMaria(DiaSemana.DOMINGO, 1);
		
		registrarSetDeMaria(LUNES.minusDays(1)); //Domingo 25 de octubre (semana anterior)
		
		registrarSetDeMaria(LUNES); //lunes 26 de octubre (primer dia de la semana)
		registrarSetDeMaria(LUNES.plusDays(6));// domingo 1 nov
		registrarSetDeMaria(LUNES.plusDays(7));// lunes 2 nov (nueva semana)
		
		assertEquals(2, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES.plusDays(2)));
		assertEquals(2, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES));
		assertEquals(2, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES.plusDays(6))); // Consulta el domingo
		
		assertEquals(1, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES.minusDays(1)));
		assertEquals(1, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES.plusDays(7)));
		
	}
	
	@Test
	void setDeshechoNoCuenta() {
		crearRutinaDeMaria(DiaSemana.LUNES, 2);
		sesionService.iniciarSesion(maria, LUNES);
		sesionService.registrarSetPeso(80, 10, 8, 2);
		sesionService.registrarSetPeso(90, 5, 8, 2);
		assertEquals(2, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES));
		
		sesionService.deshacerUltimoSet();
		
		assertEquals(1, recordService.calcularVolumenSemanal(maria, "Cuádriceps", LUNES));
	}
	@Test
	void nombreDeGrupoSeComparaIgual() {
		cargarHistorialBase();
		
		assertEquals(0, recordService.calcularVolumenSemanal(sofia, "cuádriceps", LUNES));
	}
	
	@Test
	void volUsuarioNull() {
		assertThrows(UsuarioInvalidoException.class, () -> recordService.calcularVolumenSemanal(null, "Cuádriceps", LUNES));
	}
	@Test
	void volGrupoNulooVacio() {
		assertThrows(GrupoMuscularInvalidoException.class, ()-> recordService.calcularVolumenSemanal(sofia, null, LUNES));
		assertThrows(GrupoMuscularInvalidoException.class, ()-> recordService.calcularVolumenSemanal(sofia, " ", LUNES));
	}
	
	@Test
	void volFechaNula() {
		assertThrows(FechaInvalidaException.class, () -> recordService.calcularVolumenSemanal(sofia, "Cuadriceps", null));
		
	}
	///////////////////// Pruebas del R13: calcular volumen semanal total  //////////////////////////
	
	@Test
	void cuentaLosSetsDeCadaGrupo() {
		cargarHistorialBase();
		
		// La clave String es el grupo muscular y el valor Integer cuantos sets se hicieron
		TablaHash<String, Integer> volumen = recordService.calcularVolumenSemanalTotal(sofia, LUNES);
		
		assertEquals(3, volumen.darTamanio()); // 3 grupos musculares diferentes 
		assertEquals(2, volumen.buscarValor("Cuádriceps"));
		assertEquals(1, volumen.buscarValor("Abdominales"));
		assertEquals(1, volumen.buscarValor("Pectoral"));
		
	}
	
	@Test
	void gruposNoTrabajadosEnLaSemanaNoAparecen() {
		cargarHistorialBase();
		
		TablaHash<String, Integer> volumen = recordService.calcularVolumenSemanalTotal(sofia, LUNES);
		
		assertFalse(volumen.contieneClave("Gluteos"));
		assertNull(volumen.buscarValor("Gluteos")); //Buscar por valor devuelve null si la clave no existe
	}
	@Test
	void cadaSemanaTieneSuVolumenTotal() {
		cargarHistorialBase();
		
		TablaHash<String, Integer> volumenSemanaSiguiente = recordService.calcularVolumenSemanalTotal(sofia,PROXIMO_LUNES);
		
		assertEquals(1, volumenSemanaSiguiente.darTamanio()); //Solo hay sets de cuadriceps registrados
		assertEquals(2, volumenSemanaSiguiente.buscarValor("Cuádriceps"));
		assertFalse(volumenSemanaSiguiente.contieneClave("Pectoral"));
	}
	
	@Test
	void volSoloCuentaSetsDelUsuario() {
		cargarHistorialBase();
		
		TablaHash<String, Integer> volumenAna = recordService.calcularVolumenSemanalTotal(ana,LUNES);
		assertEquals(1, volumenAna.darTamanio());
		assertEquals(1, volumenAna.buscarValor("Cuádriceps"));
	}
	@Test
	void usuarioSinSetsVolumenVacio() {
		cargarHistorialBase();
		
		assertTrue(recordService.calcularVolumenSemanalTotal(maria, LUNES).estaVacia());
	}
	@Test
	void volTotalCoincideConVolPorGrupo() {
		cargarHistorialBase();
		TablaHash<String, Integer> volumen = recordService.calcularVolumenSemanalTotal(sofia, LUNES);
		
		for(String grupo : volumen.listarClaves()) {
			assertEquals(recordService.calcularVolumenSemanal(sofia, grupo, LUNES), volumen.buscarValor(grupo), "El volumen de " + grupo + " no coincide");
		}
	}
	@Test
	void volUsuarioNulo() {
		assertThrows(UsuarioInvalidoException.class, ()-> recordService.calcularVolumenSemanalTotal(null, LUNES));
	}
	@Test
	void volTFechaNula() {
		assertThrows(FechaInvalidaException.class, () -> recordService.calcularVolumenSemanalTotal(sofia, null));
	}
}
