package com.gymtracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gymtracker.model.MedicionInBody;
import com.gymtracker.model.Usuario;

public class InBodyService {

	private final List<MedicionInBody> mediciones;
	private int siguienteId;
	
	public InBodyService() {
		
		this(new ArrayList<MedicionInBody>());
	}
	
	public InBodyService(List<MedicionInBody> mediciones) {
		
		this.mediciones = mediciones;
		
		int maximo = 0;
		for(MedicionInBody m: mediciones) {
			
			if(m.getId()>maximo) {
				
				maximo = m.getId();
			}
		}
		this.siguienteId = maximo +1;
	}
	
	//Registrar Medicion 
	
	public MedicionInBody registrarMedicion(Usuario usuario, LocalDate fecha, double peso, double porcentajeGrasa, double masaMuscular,
			double aguaCorporal, double imc ) {
		
		MedicionInBody medicion = new MedicionInBody(siguienteId, usuario, fecha, peso, porcentajeGrasa, masaMuscular, aguaCorporal, imc);
		
		mediciones.add(medicion);
		siguienteId++;
		
		return medicion;
		
	}
	
	
	//TODO
	public List<MedicionInBody> listarPorUsuario(Usuario usuario){
		
		List<MedicionInBody> mediciones = new ArrayList<>();
		
		for(MedicionInBody m: mediciones) {
			
			if(m.getUsuario().equals(usuario)) {
				
				mediciones.add(m);
			}
		}
		
		return mediciones;
		
		
	}
	
	
	//TODO comparar e importar se agrega después 
	
	
	
}
