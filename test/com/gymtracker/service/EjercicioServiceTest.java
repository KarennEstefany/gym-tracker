package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.gymtracker.exceptions.CatalogoInvalidoException;
import com.gymtracker.exceptions.EjercicioDuplicadoException;
import com.gymtracker.exceptions.EjercicioNoEncontradoException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;

class EjercicioServiceTest {

    private EjercicioService service;

    @BeforeEach
    void setUp() {
        service = new EjercicioService();
    }

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
    void buscarCodigoInexistenteLanzaExcepcion() {
        assertThrows(EjercicioNoEncontradoException.class, () -> {
            service.buscarPorCodigo("CODIGO_FALSO");
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
}