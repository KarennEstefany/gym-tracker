package com.gymtracker.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.gymtracker.algorithms.OrdenarPorInsercion;
import com.gymtracker.exceptions.CatalogoInvalidoException;
import com.gymtracker.exceptions.EjercicioDuplicadoException;
import com.gymtracker.exceptions.EjercicioNoEncontradoException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.structures.TablaHash;

public class EjercicioService {

    private final TablaHash<String, Ejercicio> ejercicios;
    private int siguienteId;

    public EjercicioService() {
        this.ejercicios = new TablaHash<>();
        this.siguienteId = 1;
    }

    /** R2: registra un ejercicio nuevo; el código no puede repetirse. */
    public Ejercicio registrarEjercicio(String codigo, String nombre, String grupoMuscular, String categoria,
                                         TipoEjercicio tipo) {
    	// El constructor de Ejercicio recibe primero grupoMuscular y luego categoria
    	//Se crea el ejercicio antes de consultar en la tabla y valida internamente los parametros, responde con excepciones propias
    	Ejercicio nuevo = new Ejercicio(siguienteId, codigo, nombre, grupoMuscular, categoria, tipo);
    	
        if (ejercicios.contieneClave(nuevo.getCodigo())) {
            throw new EjercicioDuplicadoException("Ya existe un ejercicio con el código " + codigo);
        }
  
       
        ejercicios.insertar(nuevo.getCodigo(), nuevo);
        siguienteId++;   // después de insertar y construir,  el id solo se asigna si el registro se completa
        return nuevo;
    }

    /**
     * R2b: carga el catálogo desde un archivo de texto. Cada línea tiene el formato
     * codigo,nombre,categoria,grupoMuscular,tipo
     */
    public int cargarCatalogo(String ruta) {
        int idInicial = siguienteId;
        List<String> codigosCargados= new ArrayList<>();
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
                Ejercicio registrado=registrarEjercicio(partes[0].trim(), partes[1].trim(),
                        partes[2].trim(), partes[3].trim(), tipo);
                codigosCargados.add(registrado.getCodigo());
                
            }
        } catch (IOException e) {
        	deshacerCarga(codigosCargados, idInicial);
            throw new CatalogoInvalidoException("No se pudo leer el catálogo: " + e.getMessage());
        }
        catch (RuntimeException e) {
        	deshacerCarga(codigosCargados, idInicial);
            throw e;
        }
        return codigosCargados.size();
    }
    private void deshacerCarga(List<String> codigos, int idInicial) {
        for (String codigo : codigos) {
            ejercicios.eliminar(codigo);   // reutiliza TablaHash.eliminar
        }
        siguienteId = idInicial;           // los ids vuelven a donde estaban
    }

    /** R17: busca por código y lanza excepción si no existe. */
    public Ejercicio buscarPorCodigo(String codigo) {
    	Ejercicio nuevo = ejercicios.buscarValor(codigo);
        
            if (nuevo==null) {
            	 throw new EjercicioNoEncontradoException("No existe un ejercicio con el código " + codigo);
            }
            return nuevo;
        }
       
    

    public boolean existe(String codigo) {

        return ejercicios.contieneClave(codigo);
    
    }

    /** Devuelve una lista ordenada de los ejercicios por nombre */
    public List<Ejercicio> listarEjercicios() {
    	List<Ejercicio> ejerciciosLista = ejercicios.listarValores();
    	OrdenarPorInsercion.ordenar(ejerciciosLista, Comparator.comparing(Ejercicio::getNombre, String.CASE_INSENSITIVE_ORDER));
    	return ejerciciosLista;
    }
    /** Devuelve una lista ordenada de los ejercicios por el criterio solicitado */
    public List<Ejercicio> listarEjercicios(Comparator<Ejercicio> criterio) {
    	List<Ejercicio> ejerciciosLista = ejercicios.listarValores();
    	OrdenarPorInsercion.ordenar(ejerciciosLista, criterio);
    	return ejerciciosLista;
    }
}