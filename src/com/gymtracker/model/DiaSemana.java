package com.gymtracker.model;

public enum DiaSemana {
	LUNES("Lunes"), MARTES("Martes"), MIERCOLES("Miércoles"), JUEVES("Jueves"), VIERNES("Viernes"), SABADO("Sábado"), DOMINGO("Domingo");
	
	
	private  final String etiqueta;
	
	private DiaSemana(String etiqueta) {
		
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		
		return etiqueta;
	}

}
