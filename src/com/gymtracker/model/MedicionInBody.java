package com.gymtracker.model;

import java.io.Serializable;
import java.time.LocalDate;

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
		super();
		this.id = id;
		this.usuario = usuario;
		this.fecha = fecha;
		this.peso = peso;
		this.porcentajeGrasa = porcentajeGrasa;
		this.masaMuscular = masaMuscular;
		this.aguaCorporal = aguaCorporal;
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
