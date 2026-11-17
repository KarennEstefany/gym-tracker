package com.gymtracker.model;

import java.time.LocalDate;

public class VariacionInBody{
	
	private final LocalDate fechaAnterior;
	private final LocalDate fechaPosterior;
	private final double deltaPeso;
	private final double deltaGrasa;
	private final double deltaMasaMuscular;
	public VariacionInBody(MedicionInBody anterior, MedicionInBody posterior) {
		
		
		this.fechaAnterior = anterior.getFecha();
		this.fechaPosterior = posterior.getFecha();
		this.deltaPeso = posterior.getPeso() - anterior.getPeso();
		this.deltaGrasa = posterior.getPorcentajeGrasa()- anterior.getPorcentajeGrasa();
		this.deltaMasaMuscular = posterior.getMasaMuscular() - anterior.getMasaMuscular();
	}
	/**
	 * @return the fehcaAnterior
	 */
	public LocalDate getFechaAnterior() {
		return fechaAnterior;
	}
	/**
	 * @return the fechaPosterior
	 */
	public LocalDate getFechaPosterior() {
		return fechaPosterior;
	}
	/**
	 * @return the deltaPeso
	 */
	public double getDeltaPeso() {
		return deltaPeso;
	}
	/**
	 * @return the deltaGrasa
	 */
	public double getDeltaGrasa() {
		return deltaGrasa;
	}
	/**
	 * @return the deltaMasaMuscular
	 */
	public double getDeltaMasaMuscular() {
		return deltaMasaMuscular;
	}
	
	
	
	

}
