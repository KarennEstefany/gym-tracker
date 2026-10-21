package com.gymtracker.service;

import java.util.ArrayList;
import java.util.List;

import com.gymtracker.exceptions.UsuarioDuplicadoException;
import com.gymtracker.exceptions.UsuarioNoEncontradoException;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;

/**
 * 
 */
public class UsuarioService {

    private final List<Usuario> usuarios;
    private int siguienteId;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.siguienteId = 1;
    }

    /**
     * 
     * R1: registra un usuario nuevo; el documento no puede repetirse.
     * @param documento
     * @param nombre
     * @param edad
     * @param estatura
     * @param objetivo
     * @return
     */
    public Usuario registrarUsuario(String documento, String nombre, int edad,
                                    double estatura, Objetivo objetivo) {
        if (existe(documento)) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con ese documento");
        }
        Usuario nuevo = new Usuario(siguienteId, documento, nombre, edad, estatura, objetivo);
        siguienteId++;   // después de construir, para no gastar ids si el constructor falla
        usuarios.add(nuevo);
        return nuevo;
    }

    
    /**
     * R16: busca por documento y lanza excepción si no existe.
     * @param documento
     * @return
     */
    public Usuario buscarPorDocumento(String documento) {
        for (Usuario u : usuarios) {
            if (u.getDocumento().equals(documento)) {
                return u;
            }
        }
        throw new UsuarioNoEncontradoException("No existe un usuario con ese documento");
    }
    
    

   
    /**
     * Ingreso a la app: devuelve el usuario que usará el menú.
     * @param documento
     * @return
     */
    public Usuario ingresar(String documento) {
        return buscarPorDocumento(documento);
    }

    
    
    /**
     * 
     * @param documento
     * @return
     */
    public boolean existe(String documento) {
        for (Usuario u : usuarios) {
            if (u.getDocumento().equals(documento)) {
                return true;
            }
        }
        return false;
    }

    public int darCantidad() {
        return usuarios.size();
    }
}