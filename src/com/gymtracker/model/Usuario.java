package com.gymtracker.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.gymtracker.exceptions.EdadEstaturaInvalidaException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;
import com.gymtracker.exceptions.ContrasenaInvalidaException;
import com.gymtracker.exceptions.DocumentoInvalidoException;
public class Usuario {
	private int id;
	private String documento;
	private String nombre;
	private int edad;
	private double estatura;
	private Objetivo objetivo;
	private final String contrasenaHash;
	/**
	 * @param id
	 * @param documento
	 * @param nombre
	 * @param edad
	 * @param estatura
	 * @param objetivo
	 * @param contrasena 
	 */
	public Usuario(int id, String documento, String nombre, int edad, double estatura, Objetivo objetivo, String contrasena) {
		super();
		this.id = id;
		
		setNombre(nombre);
		setEdad(edad);
		setEstatura(estatura);
		setObjetivo (objetivo);
		
		
		if(documento == null ||  documento.isEmpty() ) {
					
					throw new DocumentoInvalidoException("El documento del usuario no puede ser nulo");
					
				}
		this.documento = documento;
		
		if(contrasena == null || contrasena.length()<6 || contrasena.isEmpty() ) {
			
			throw new ContrasenaInvalidaException("La contraseña debe tener al menos 6 caracteres");
			
		}
		this.contrasenaHash = hashear(contrasena);	
		
	}
	
	public int getId() {
		return id;
	}
	public String getDocumento() {
		return documento;
	}
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new NombreNullException("El nombre del usuario no puede estar vacio");
		}
		
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	
	public void setEdad(int edad) {
		if (edad <=0 || edad>120 ) {
			throw new EdadEstaturaInvalidaException("La edad debe estar entre 1 y 120");
		}
		this.edad = edad;
	}
	public void setEstatura(double estatura) {
		if (estatura<=0.49 || estatura >2.30) {
			throw new EdadEstaturaInvalidaException("La estatura debe estar entre 50 y 2.30m");
		}
		this.estatura = estatura;
	}
	public double getEstatura() {
		return estatura;
	}
	public Objetivo getObjetivo() {
		return objetivo;
	}
	public void setObjetivo(Objetivo objetivo) {
		if (objetivo== null) {
			throw new ObjetivoInvalidoException("El objetivo del usuario no puede estar vacio");
		}
		this.objetivo = objetivo;
	}
	

	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	        return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	        return false;
	    }
	    Usuario otro = (Usuario) o;
	    return Objects.equals(documento, otro.documento);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(documento);
	}
	
	public boolean verificarContrasena(String intento) {
		
		return intento!=null && contrasenaHash.equals(hashear(intento));
	
	}
	
	/**
	 * @param texto
	 * @return
	 */
	public static String hashear(String texto) {
		
		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes  = md.digest(texto.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for(byte b: bytes) {
				
				sb.append(String.format("%02x",b));
			}
			
			return sb.toString();
		}catch(NoSuchAlgorithmException e) {
			
			throw new IllegalStateException("SHA-256",e);
		}
		

	}
	

}
