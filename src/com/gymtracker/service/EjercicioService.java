package com.gymtracker.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.gymtracker.exceptions.CatalogoInvalidoException;
import com.gymtracker.exceptions.EjercicioDuplicadoException;
import com.gymtracker.exceptions.EjercicioNoEncontradoException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;

public class EjercicioService {

    private final List<Ejercicio> ejercicios;
    private int siguienteId;

    public EjercicioService() {
        this.ejercicios = new ArrayList<>();
        this.siguienteId = 1;
    }

    /** R2: registra un ejercicio nuevo; el código no puede repetirse. */
    public Ejercicio registrarEjercicio(String codigo, String nombre, String categoria,
                                        String grupoMuscular, TipoEjercicio tipo) {
        if (existe(codigo)) {
            throw new EjercicioDuplicadoException("Ya existe un ejercicio con el código " + codigo);
        }
        // El constructor de Ejercicio recibe primero grupoMuscular y luego categoria
        Ejercicio nuevo = new Ejercicio(siguienteId, codigo, nombre, grupoMuscular, categoria, tipo);
        siguienteId++;   // después de construir, para no gastar ids si el constructor falla
        ejercicios.add(nuevo);
        return nuevo;
    }

    /**
     * R2b: carga el catálogo desde un archivo de texto. Cada línea tiene el formato
     * codigo,nombre,categoria,grupoMuscular,tipo
     */
    public int cargarCatalogo(String ruta) {
        int cargados = 0;
        try (BufferedReader lector = new BufferedReader(new FileReader(ruta, StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;   // se ignoran las líneas en blanco
                }
                String[] partes = linea.split(",");
                if (partes.length != 5) {
                    throw new CatalogoInvalidoException(
                            "Línea " + numeroLinea + ": se esperaban 5 campos y hay " + partes.length);
                }
                TipoEjercicio tipo;
                try {
                    tipo = TipoEjercicio.valueOf(partes[4].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new CatalogoInvalidoException(
                            "Línea " + numeroLinea + ": tipo desconocido '" + partes[4].trim() + "'");
                }
                registrarEjercicio(partes[0].trim(), partes[1].trim(),
                        partes[2].trim(), partes[3].trim(), tipo);
                cargados++;
            }
        } catch (IOException e) {
            throw new CatalogoInvalidoException("No se pudo leer el catálogo: " + e.getMessage());
        }
        return cargados;
    }

    /** R17: busca por código y lanza excepción si no existe. */
    public Ejercicio buscarPorCodigo(String codigo) {
        for (Ejercicio e : ejercicios) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        throw new EjercicioNoEncontradoException("No existe un ejercicio con el código " + codigo);
    }

    public boolean existe(String codigo) {
        for (Ejercicio e : ejercicios) {
            if (e.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    /** Copia de la lista, para que quien la use no modifique el catálogo por fuera. */
    public List<Ejercicio> listarEjercicios() {
        return new ArrayList<>(ejercicios);
    }
}