package com.gymtracker.model;

import java.time.LocalDate;

public class MedicionInBody {
	
	private final String id;
	private final Usuario usuario;
	private LocalDate fecha;
	private double peso;
	private double porcentajeGrasa;
	private double masaMuscular;
	private double aguaCorporal;
	private double imc;
	
	public MedicionInBody(Usuario usuario, LocalDate fecha, double peso, double porcentajeGrasa, double masaMuscular,
			double aguaCorporal, double imc) {
		super();
		this.id = "";
		this.usuario = usuario;
		this.fecha = fecha;
		this.peso = peso;
		this.porcentajeGrasa = porcentajeGrasa;
		this.masaMuscular = masaMuscular;
		this.aguaCorporal = aguaCorporal;
		this.imc = imc;
	}
	
	
	
	
	
	
	
}
