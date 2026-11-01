package com.gymtracker.service;

import java.util.ArrayList;
import java.util.List;

import com.gymtracker.exceptions.ContrasenaInvalidaException;
import com.gymtracker.exceptions.UsuarioDuplicadoException;
import com.gymtracker.exceptions.UsuarioNoEncontradoException;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;
import com.gymtracker.structures.TablaHash;

/**
 * Implementa la tabla Hash para que las operaciones de los metodos sean O(1) en promedio
 */
public class UsuarioService {
	
	
	private final List<Usuario> usuarios;
    private final TablaHash<String, Usuario> usuariosPorDocumento;
    private int siguienteId;

    /**
     * Constructor por defecto. Inicializa el servicio con una lista vacía
     * delengando en el constructor principal.
     */
    public UsuarioService() {
       
    	this(new ArrayList<Usuario>()); 
    }

    /**
     * 
     * Constructor principal que inicializa el servicio a partir de 
     * una lista existente de usuarios 
     * 
     * Este cosntructor realiza dos tareas clave:
     * <ul> 
     * 		<li> Almaccena la lista de usuarios y construye la tabla hash indexada por documento.</li>
     * 		<li> Recorre la lista para encontrar el Id más alto y calcula automáticamente el {@code siguienteId} disponible para evitar duplicados.</li>
     * </ul>
     * @param usuarios
     */
    public UsuarioService(List<Usuario> usuarios) {
    	
    	this.usuarios = usuarios;
    	this.usuariosPorDocumento = new TablaHash<>();
    	
    	int maximo = 0; //Inicializa la variable en 0 
    	for(Usuario u: usuarios) {
    		
    		//Inserta el documento y el usuario a la tabla hash 
    		usuariosPorDocumento.insertar(u.getDocumento(), u); 
    		if(u.getId()>maximo) {
    			
    			maximo = u.getId(); //Actualiza el máximo si encuentra un Id mayor
    		}
    		
    	}
    	this.siguienteId = maximo + 1; 
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
                                    double estatura, Objetivo objetivo, String contrasena) {
    	//Se crea el usuario antes de consultar la tabla y valida internamente los parametros, responde con excepciones propias
    	Usuario nuevo = new Usuario(siguienteId, documento, nombre, edad, estatura, objetivo, contrasena);
        if (usuariosPorDocumento.contieneClave(nuevo.getDocumento())) {
            throw new UsuarioDuplicadoException("Ya existe un usuario con ese documento");
        }
        
        usuarios.add(nuevo);//lo agrega también a la lista
        usuariosPorDocumento.insertar(nuevo.getDocumento(), nuevo); //Agrega a la tabla hash 
        
        siguienteId++;   // después de inssertar, el id solo se asigna si el registro se completa
        return nuevo;
    }

    
    /**
     * R16: busca por documento y lanza excepción si no existe.
     * @param documento
     * @return
     */
    public Usuario buscarPorDocumento(String documento) {
    	Usuario usuario= usuariosPorDocumento.buscarValor(documento);
        if(usuario == null) {
        	throw new UsuarioNoEncontradoException("No existe un usuario con ese documento");
        }
        return usuario;
        
    }
    
    

   
    /**
     * Ingreso a la app: devuelve el usuario que usará el menú.
     * verifica que el usuario y la contraseña sean correctos 
     * 
     * @param documento
     *@param contrasena
     * @return
     */
    public Usuario ingresar(String documento, String contrasena) {
    	Usuario usuario= usuariosPorDocumento.buscarValor(documento);
        	if(usuario==null || !usuario.verificarContrasena(contrasena)) {
        		throw new ContrasenaInvalidaException("Documento o credenciales invalidas");
        	}
        	return usuario;
        }
       

    
    
    /**
     * 
     * @param documento
     * @return
     */
    public boolean existe(String documento) {
        return usuariosPorDocumento.contieneClave(documento);
    }

    public int darCantidad() {
        return usuariosPorDocumento.darTamanio();
    }
}