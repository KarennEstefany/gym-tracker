package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.gymtracker.exceptions.CatalogoInvalidoException;
import com.gymtracker.exceptions.EjercicioDuplicadoException;
import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.EjercicioNoEncontradoException;
import com.gymtracker.exceptions.GymTrackerException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;

class EjercicioServiceTest {

    private EjercicioService service;

    @BeforeEach
    void setUp() {
        service = new EjercicioService();
    }
    /**Clase auxiliar que devuelve los nombres de los ejercicios en el orden de la lista */
    private List<String> nombres (List<Ejercicio> lista){
    	 List<String> resultado = new ArrayList<>();
         for (Ejercicio e : lista) {
             resultado.add(e.getNombre());
         }
         return resultado;
    }
////////////////////Pruebas de registro/////////////////////////
    @Test
    void registrarYBuscarEjercicioExitoso() {
        Ejercicio creado = service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
        assertNotNull(creado);
        
        Ejercicio encontrado = service.buscarPorCodigo("E01");
        assertNotNull(encontrado);
        assertEquals("E01", encontrado.getCodigo());
        assertEquals("Press de Banca", encontrado.getNombre());
    }

    @Test
    void registrarEjercicioDuplicadoLanzaExcepcion() {
        service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.TIEMPO);
        
        assertThrows(EjercicioDuplicadoException.class, () -> {
            service.registrarEjercicio("E01", "Press Inclinado", "Pecho", "Fuerza", TipoEjercicio.TIEMPO);
        });
    }
    @Test
    void registroFallidoNoConsumeId() {
        // First successful registration gets ID 1
        Ejercicio e1 = service.registrarEjercicio("E01", "Ejercicio 1", "Cat", "Grupo", TipoEjercicio.TIEMPO);
        assertEquals(1, e1.getId());

        // Failed registration due to duplicate code (should not increment siguienteId)
        assertThrows(EjercicioDuplicadoException.class, () -> {
            service.registrarEjercicio("E01", "Ejercicio Duplicado", "Cat", "Grupo", TipoEjercicio.PESO);
        });

        // The next successful registration should receive ID 2 (meaning ID was preserved)
        Ejercicio e2 = service.registrarEjercicio("E02", "Ejercicio 2", "Cat", "Grupo", TipoEjercicio.TIEMPO);
        assertEquals(2, e2.getId());
    }

    @Test 
    void seGuardaCorrectamente() {
    	Ejercicio ejercicio = service.registrarEjercicio("SENT-01", "Sentadilla libre", "Cuadriceps", "Piernas", TipoEjercicio.PESO
    	);
    	assertEquals("SENT-01", ejercicio.getCodigo());
    	assertEquals("Sentadilla libre", ejercicio.getNombre());
    	assertEquals("Cuadriceps", ejercicio.getGrupoMuscular());
    	assertEquals("Piernas", ejercicio.getCategoria());
    	assertEquals(TipoEjercicio.PESO, ejercicio.getTipo());
    }
    @Test
    void losIdSonSecuenciales() {
    	Ejercicio ejercicio1 = service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	Ejercicio ejercicio2 = service.registrarEjercicio("SENT-02", "Sentadilla goblet", "Cuadriceps", "Piernas", TipoEjercicio.PESO
    	    	);
    	Ejercicio ejercicio3 = service.registrarEjercicio("SENT-03", "Sentadilla isometrica", "Cuadriceps", "Piernas", TipoEjercicio.PESO
    	    	);
    	assertEquals(1, ejercicio1.getId());
    	assertEquals(2, ejercicio2.getId());
    	assertEquals(3, ejercicio3.getId());
    }
    
    @Test 
    void registrarDuplicadoNoReemplazaElOriginal() {
    	Ejercicio ejercicio = service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	assertThrows(EjercicioDuplicadoException.class, ()-> service.registrarEjercicio("SENT-01", "Sentadilla", 
    			"Cuadriceps", "Piernas", TipoEjercicio.TIEMPO));
    	assertSame(ejercicio, service.buscarPorCodigo("SENT-01"));
    	assertEquals("Sentadilla libre", service.buscarPorCodigo("SENT-01").getNombre());
    	assertEquals(1, service.listarEjercicios().size());
    }
    @Test
    void registrarDatosInvalidos() {
    	assertThrows(GymTrackerException.class, ()-> service.registrarEjercicio("SENT-01", "", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO));
    	assertFalse(service.existe("SENT-01"));
    	assertTrue(service.listarEjercicios().isEmpty());
    	Ejercicio ejercicio = service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	assertEquals(1, ejercicio.getId());
    }
    @Test
    void registrarCodigoNulo() {
    	assertThrows(GymTrackerException.class, ()->service.registrarEjercicio(null, "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO));
    	assertThrows(GymTrackerException.class, ()->service.registrarEjercicio("", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO));
    	assertTrue(service.listarEjercicios().isEmpty());
    }
    @Test
    void registrarTipoNulo() {
    	assertThrows(EjercicioInvalidoException.class, ()->service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", null));
    	assertFalse(service.equals("SENT-01"));
    }
    @Test
    void registrarMuchosEjercicios() {
    	for(int i=0; i<50; i++) {
    		service.registrarEjercicio("E" +i, "Ejercicio" + i, "Cat", "Grupo", TipoEjercicio.TIEMPO);
    	}
    	assertEquals(50, service.listarEjercicios().size());
    	for(int i=0; i<50; i++) {
    		assertEquals("Ejercicio"+i, service.buscarPorCodigo("E" +i).getNombre());
    		assertEquals(i+1, service.buscarPorCodigo("E" +i).getId());
    	}
    	
    }
    //////////Pruebas de busqueda//////////////////
   
    
    @Test
    void buscarPorCodigoCorrectamente() {
    	Ejercicio ejercicio1 = service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	assertSame(ejercicio1, service.buscarPorCodigo("SENT-01"));
    }
    @Test
    void buscarCodigoInexistenteLanzaExcepcion() {
        assertThrows(EjercicioNoEncontradoException.class, () -> {
            service.buscarPorCodigo("CODIGO_FALSO");
        });
    }
    @Test
    void buscarPorCodigoNulo() {
    	assertThrows(ObjetoNuloException.class, ()-> service.buscarPorCodigo(null));
    }

    //////////////////Pruebas del metodo existe/////////////////////////
    @Test
    void codigosRegistrados() {
    	service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	assertTrue(service.existe("SENT-01"));
    	assertFalse(service.existe("SENT-02"));
    	
    }
    @Test
    void codigoNulo() {
    	assertThrows(ObjetoNuloException.class, () -> service.existe(null));
    }
//////////////////////Pruebas de cargar el catalogo//////////////////
    @Test
    void cargarCatalogoValidoDevuelveCantidadCorrecta(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("catalogo_valido.txt");
        String contenido = "E01,Press de Banca,Pecho,Fuerza,PESO\n" +
                           "E02,Sentadilla,Piernas,Fuerza,PESO\n" +
                           "E03,Trote,Cardio,Resistencia,TIEMPO\n";
        Files.writeString(archivo, contenido);

        int cargados = service.cargarCatalogo(archivo.toString());
        assertEquals(3, cargados);
        assertEquals(3, service.listarEjercicios().size());
    }

    @Test
    void cargarCatalogoCamposInsuficientesLanzaExcepcion(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("catalogo_invalido.txt");
        String contenido = "E01,Press de Banca,Pecho,Fuerza\n"; // Missing the 5th field (tipo)
        Files.writeString(archivo, contenido);

        assertThrows(CatalogoInvalidoException.class, () -> {
            service.cargarCatalogo(archivo.toString());
        });
    }

    @Test
    void cargarCatalogoTipoInvalidoLanzaExcepcion(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("catalogo_tipo_invalido.txt");
        String contenido = "E01,Press de Banca,Pecho,Fuerza,TIPO_INCORRECTO\n";
        Files.writeString(archivo, contenido);

        assertThrows(CatalogoInvalidoException.class, () -> {
            service.cargarCatalogo(archivo.toString());
        });
    }

    @Test
    void cargarCatalogoRutaInexistenteLanzaExcepcion() {
        assertThrows(CatalogoInvalidoException.class, () -> {
            service.cargarCatalogo("ruta/inventada/que/no/existe.txt");
        });
    }
    @Test
    void ignoraLasLineasEnBlanco(@TempDir Path tempDir) throws IOException {
    	Path archivo= tempDir.resolve("con_blacos.txt");
    }
    
    @Test
    void cargarConCodigoRepetido(@TempDir Path tempDir) throws IOException {
    	service.registrarEjercicio("E01", "Ya existía", "Cat", "Grupo", TipoEjercicio.PESO);
        Path archivo = tempDir.resolve("catalogo.txt");
        Files.writeString(archivo, "E01,Press de Banca,Pecho,Pectoral,PESO\n");
    	assertThrows(EjercicioDuplicadoException.class, () -> service.cargarCatalogo(archivo.toString()));

    	assertTrue(service.existe("E01"));
    	assertFalse(service.existe("E02"));
    	assertFalse(service.listarEjercicios().isEmpty());
    }
    @Test
    void cargarCatalogoIgnoraLasLineasEnBlanco(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("con_blancos.txt");
        String contenido = "\n" +
                           "E01,Press de Banca,Pecho,Pectoral,PESO\n" +
                           "\n" +
                           "   \n" +
                           "E02,Sentadilla,Piernas,Cuádriceps,PESO\n" +
                           "\n";
        Files.writeString(archivo, contenido);
 
        assertEquals(2, service.cargarCatalogo(archivo.toString()));
        assertEquals(2, service.listarEjercicios().size());
    }
 
    @Test
    void cargarCatalogoRecortaEspaciosYAceptaTipoEnMinusculas(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("con_espacios.txt");
        Files.writeString(archivo, "  E01 , Press de Banca ,  Pectoral, Pecho , peso \n");
 
        service.cargarCatalogo(archivo.toString());
 
        Ejercicio e = service.buscarPorCodigo("E01");
        assertEquals("Press de Banca", e.getNombre());
        assertEquals("Pecho", e.getCategoria());
        assertEquals("Pectoral", e.getGrupoMuscular());
        assertEquals(TipoEjercicio.PESO, e.getTipo());
    }
 
    @Test
    void cargarArchivoVacioDevuelveCeroYNoRegistraNada(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("vacio.txt");
        Files.writeString(archivo, "");
 
        assertEquals(0, service.cargarCatalogo(archivo.toString()));
        assertTrue(service.listarEjercicios().isEmpty());
    }
 
    @Test
    void cargarCatalogoConMasDeCincoCamposLanzaExcepcion(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("demasiados_campos.txt");
        Files.writeString(archivo, "E01,Press de Banca,Pecho,Pectoral,PESO,sobrante\n");
 
        assertThrows(CatalogoInvalidoException.class, () -> service.cargarCatalogo(archivo.toString()));
    }
 
    @Test
    void elMensajeDeErrorIndicaElNumeroDeLineaInvalida(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("error_linea_dos.txt");
        Files.writeString(archivo, "E01,Press de Banca,Pecho,Pectoral,PESO\n" +
                                   "E02,Sentadilla,Piernas,Cuádriceps\n");
 
        CatalogoInvalidoException ex = assertThrows(CatalogoInvalidoException.class,
                () -> service.cargarCatalogo(archivo.toString()));
 
        assertTrue(ex.getMessage().contains("Línea 2"), "El mensaje debe indicar la línea: " + ex.getMessage());
    }
 
    @Test
    void cargarCatalogoConCodigoRepetidoLanzaExcepcionYConservaLoCargadoAntes(@TempDir Path tempDir)
            throws IOException {
        Path archivo = tempDir.resolve("codigo_repetido.txt");
        Files.writeString(archivo, "E01,Press de Banca,Pecho,Pectoral,PESO\n" +
                                   "E02,Sentadilla,Piernas,Cuádriceps,PESO\n" +
                                   "E01,Otro ejercicio,Piernas,Glúteos,PESO\n");
 
        assertThrows(EjercicioDuplicadoException.class, () -> service.cargarCatalogo(archivo.toString()));
 
        // La carga es transaccional: lo registrado en el mismo archivo antes de la linea del error se borra
        assertFalse(service.existe("E01"));
        assertFalse(service.existe("E02"));
        assertEquals(0, service.listarEjercicios().size());
        assertTrue(service.listarEjercicios().isEmpty());
    }
 
    @Test
    void cargarCatalogoConUnCampoVacioLanzaExcepcion(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("campo_vacio.txt");
        Files.writeString(archivo, "E01,,Pecho,Pectoral,PESO\n"); // nombre vacío
 
        assertThrows(GymTrackerException.class, () -> service.cargarCatalogo(archivo.toString()));
        assertFalse(service.existe("E01"));
    }
 
    @Test
    void cargarCatalogoConservaLosAcentos(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("acentos.txt");
        Files.writeString(archivo, "E01,Extensión de cuádriceps,Cuádriceps,Piernas,PESO\n");
 
        service.cargarCatalogo(archivo.toString());
 
        assertEquals("Extensión de cuádriceps", service.buscarPorCodigo("E01").getNombre());
        assertEquals("Cuádriceps", service.buscarPorCodigo("E01").getGrupoMuscular());
    }
 
    @Test
    void cargarCatalogoAsignaIdsSecuencialesEnElOrdenDelArchivo(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("ids.txt");
        Files.writeString(archivo, "E01,Uno,Cat,Grupo,PESO\n" +
                                   "E02,Dos,Cat,Grupo,PESO\n" +
                                   "E03,Tres,Cat,Grupo,TIEMPO\n");
 
        service.cargarCatalogo(archivo.toString());
 
        assertEquals(1, service.buscarPorCodigo("E01").getId());
        assertEquals(2, service.buscarPorCodigo("E02").getId());
        assertEquals(3, service.buscarPorCodigo("E03").getId());
    }
 
    @Test
    void despuesDeCargarElCatalogoLosIdsContinuanAlRegistrarAMano(@TempDir Path tempDir) throws IOException {
        Path archivo = tempDir.resolve("catalogo.txt");
        Files.writeString(archivo, "E01,Uno,Cat,Grupo,PESO\n" +
                                   "E02,Dos,Cat,Grupo,PESO\n");
        service.cargarCatalogo(archivo.toString());
 
        Ejercicio manual = service.registrarEjercicio("PERS-01", "Personalizado", "Cat", "Grupo", TipoEjercicio.PESO);
 
        assertEquals(3, manual.getId());
        assertEquals(3, service.listarEjercicios().size());
    }
 
   
 
    @Test
    void elCatalogoRealDelProyectoSeCargaSinErrores() {
        Path catalogo = Path.of("data", "ejercicios.txt");
        Assumptions.assumeTrue(Files.exists(catalogo), "No existe data/ejercicios.txt: prueba omitida");
 
        int cargados = service.cargarCatalogo(catalogo.toString());
 
        assertTrue(cargados > 0);
        assertEquals(cargados, service.listarEjercicios().size());
    }
    @Test
    void cargarArchivoCorregidoDespuesDeCargaFallida(@TempDir Path tempDir)  throws IOException{
    	Path archivo = tempDir.resolve("ejercicios.txt");
    	Files.writeString(archivo, "E01, Sentadilla, Cuadriceps, Piernas, PESO \n" +
    			"E02, Flexiones, Pectoral, Superior, PESO \n" +
    			"E01, Press de Banca, Pectoral, Superior, PESO \n");
    	assertThrows(EjercicioDuplicadoException.class , () -> service.cargarCatalogo(archivo.toString()));
    	
    	Files.writeString(archivo, "E01, Sentadilla, Cuadriceps, Piernas, PESO \n" +
    			"E02, Flexiones, Pectoral, Superior, PESO \n" +
    			"E03, Press de Banca, Pectoral, Superior, PESO \n");
    	assertEquals(3, service.cargarCatalogo(archivo.toString()));
    	assertEquals(1, service.buscarPorCodigo("E01").getId());
    	assertEquals(2, service.buscarPorCodigo("E02").getId());
    	assertEquals(3, service.buscarPorCodigo("E03").getId());
    }
    
    @Test
    void lineaMalFormada(@TempDir Path tempDir)  throws IOException{
    	Path archivo = tempDir.resolve("malFormada.txt");
    	Files.writeString(archivo, "E01, Sentadilla, Cuadriceps, Piernas, PESO \n" +
    			"E02, Flexiones, Pectoral\n" +
    			"E01, Press de Banca, Pectoral, Superior, PESO \n");
    	assertThrows(CatalogoInvalidoException.class , () -> service.cargarCatalogo(archivo.toString()));
    	assertTrue(service.listarEjercicios().isEmpty());
    	assertEquals(1, service.registrarEjercicio("SENT-01", "Sentadilla libre", 
    			"Cuadriceps", "Piernas", TipoEjercicio.PESO).getId());
    }
 
//////////////////////////Pruebas listar ejercicios///////////////////////////////////
    @Test
    void listarEjerciciosDevuelveCopia() {
        service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
        
        List<Ejercicio> lista1 = service.listarEjercicios();
        assertEquals(1, lista1.size());

        // Modify the returned list externally
        lista1.clear();
        assertEquals(0, lista1.size(), "La lista externa fue limpiada");

        // The service's internal catalog should remain untouched because listarEjercicios() returns a copy
        List<Ejercicio> lista2 = service.listarEjercicios();
        assertEquals(1, lista2.size(), "El catálogo interno no debe verse afectado por cambios externos en la lista");
    }
    
    
    
    @Test
    void serviceVacio() {
    	assertTrue(service.listarEjercicios().isEmpty());
    }
    @Test
    void ordenaIgnorandoMayuscula() {
    	service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
    	service.registrarEjercicio("E02", "Sentadilla", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	service.registrarEjercicio("E03", "Bulgara", "Gluteo", "Piernas", TipoEjercicio.PESO);
    	service.registrarEjercicio("E04", "Dominadas", "Espalda", "Superior", TipoEjercicio.PESO);
    	
    	List<String> esperado = List.of("Bulgara", "Dominadas", "Press de Banca", "Sentadilla");
    	assertEquals(esperado, nombres(service.listarEjercicios()));
    }
    @Test 
    void listarConCriterio() {
    	service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
    	service.registrarEjercicio("E02", "Sentadilla", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	service.registrarEjercicio("E03", "Bulgara", "Gluteo", "Piernas", TipoEjercicio.PESO);
    	List<Ejercicio> lista = service.listarEjercicios(Comparator.comparingInt(Ejercicio::getId));
    	
    	assertEquals(List.of("Press de Banca", "Sentadilla", "Bulgara"), nombres(lista));
    }
    @Test 
    void listarCriterioInvertido() {
    	service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
    	service.registrarEjercicio("E02", "Sentadilla", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	service.registrarEjercicio("E03", "Bulgara", "Gluteo", "Piernas", TipoEjercicio.PESO);
    	List<Ejercicio> lista = service.listarEjercicios(Comparator.comparing(Ejercicio::getNombre).reversed());
    	assertEquals(List.of("Sentadilla", "Press de Banca", "Bulgara"), nombres(lista));
    }
    @Test
    void listarGrupoMuscular() {
    	service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
    	service.registrarEjercicio("E02", "Sentadilla", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	service.registrarEjercicio("E03", "Bulgara", "Gluteo", "Piernas", TipoEjercicio.PESO);
    	List<Ejercicio> lista = service.listarEjercicios(Comparator.comparing(Ejercicio::getGrupoMuscular));
    	assertEquals(List.of("Sentadilla", "Bulgara", "Press de Banca"), nombres(lista));
    }
    @Test
    void listarCorrectamente() {
    	Ejercicio ejercicio1=service.registrarEjercicio("E01", "Press de Banca", "Pecho", "Fuerza", TipoEjercicio.PESO);
    	Ejercicio ejercicio2= service.registrarEjercicio("E02", "Sentadilla", "Cuadriceps", "Piernas", TipoEjercicio.PESO);
    	List<Ejercicio> lista = service.listarEjercicios();
    	
    	assertEquals(2, lista.size());
    	assertTrue(lista.contains(ejercicio1));
    	assertTrue(lista.contains(ejercicio2));
    	
    }
    
}