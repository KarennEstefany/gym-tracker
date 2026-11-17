package com.gymtracker.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.gymtracker.exceptions.MedicionInvalidaException;

/**
 * 
 */
public class MedicionInBody implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
			
	private final int id;
	private final Usuario usuario;
	private LocalDate fecha;
	private double peso;
	private double porcentajeGrasa;
	private double masaMuscular;
	private double aguaCorporal;
	private double imc;
	
	/**
	 * @param id
	 * @param usuario
	 * @param fecha
	 * @param peso
	 * @param porcentajeGrasa
	 * @param masaMuscular
	 * @param aguaCorporal
	 * @param imc
	 */
	public MedicionInBody(int id, Usuario usuario, LocalDate fecha, double peso, double porcentajeGrasa, double masaMuscular,
			double aguaCorporal, double imc) {
		
		if(usuario == null) {
			
			throw new MedicionInvalidaException("La medición debe tener un usuario ");
			
		}
		
		this.id = id;
		this.usuario = usuario;
		setFecha(fecha);
		setPeso(peso);
		setPorcentajeGrasa(porcentajeGrasa);
		setMasaMuscular(masaMuscular);
		setAguaCorporal(aguaCorporal);
		this.imc = imc;
	}

	/**
	 * @return the fecha
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(LocalDate fecha) {
		if(fecha == null) {
			
			throw new MedicionInvalidaException("La fecha no puede ser nula");
		}	
		
		if (fecha.isAfter(LocalDate.now())) {
			
			
			throw new MedicionInvalidaException("La fecha no puede ser futura");
		}
			
		this.fecha = fecha;
	}

	/**
	 * @return the peso
	 */
	public double getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(double peso) {
	if(peso <= 0) {
				
				throw new MedicionInvalidaException("El peso no puede ser menor a cero");
	}
		this.peso = peso;
	}

	/**
	 * @return the porcentajeGrasa
	 */
	public double getPorcentajeGrasa() {
		return porcentajeGrasa;
	}

	/**
	 * @param porcentajeGrasa the porcentajeGrasa to set
	 */
	public void setPorcentajeGrasa(double porcentajeGrasa) {
		
	if(porcentajeGrasa < 0 || porcentajeGrasa > 100) {
				
				throw new MedicionInvalidaException("El porcentaje de grase debe estar entre 0 y 100, porcentaje: " + porcentajeGrasa);
		}
		this.porcentajeGrasa = porcentajeGrasa;
	}

	/**
	 * @return the masaMuscular
	 */
	public double getMasaMuscular() {
		return masaMuscular;
	}

	/**
	 * @param masaMuscular the masaMuscular to set
	 */
	public void setMasaMuscular(double masaMuscular) {
	
	if(masaMuscular <= 0) {
				
				throw new MedicionInvalidaException("La masa muscular debe ser mayor que 0");
	}
		this.masaMuscular = masaMuscular;
	}

	/**
	 * @return the aguaCorporal
	 */
	public double getAguaCorporal() {
		return aguaCorporal;
	}

	/**
	 * @param aguaCorporal the aguaCorporal to set
	 */
	public void setAguaCorporal(double aguaCorporal) {
		
	if(aguaCorporal <= 0) {
				
				throw new MedicionInvalidaException("La cantidad de agua corporal no puede ser nula");
		}
		this.aguaCorporal = aguaCorporal;
	}

	/**
	 * @return the imc
	 */
	public double getImc() {
		return imc;
	}

	/**
	 * @param imc the imc to set
	 */
	public void setImc(double imc) {
		this.imc = imc;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	
	
	
	
	
	
	
	
}
