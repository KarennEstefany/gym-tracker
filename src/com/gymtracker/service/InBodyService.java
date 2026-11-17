package com.gymtracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.gymtracker.algorithms.OrdenarPorInsercion;
import com.gymtracker.exceptions.MedicionInvalidaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.MedicionInBody;
import com.gymtracker.model.Usuario;
import com.gymtracker.model.VariacionInBody;

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
	
	public MedicionInBody registrarMedicion(Usuario usuario, LocalDate fecha, double peso, double porcentajeGrasa, double masaMuscular, double aguaCorporal) {
		
		if(usuario == null) {
			throw new UsuarioInvalidoException("Debe indicar un usuario");
		}
		
		
		//se agrega nuevo 
		if(buscarPorFecha(usuario, fecha) != null) {
			
			throw new MedicionInvalidaException("Ya existe una medición en esa fecha");
		}
		
		
		double imc = calcularImc(peso, usuario.getEstatura());
		MedicionInBody medicion = new MedicionInBody(siguienteId, usuario, fecha, peso, porcentajeGrasa, masaMuscular, aguaCorporal, imc);
		
		mediciones.add(medicion);
		siguienteId++;
		
		return medicion;
		 
	}
	
	
	//TODO
	public List<MedicionInBody> listarPorUsuario(Usuario usuario){
		if(usuario == null) {
			
			throw new UsuarioInvalidoException("Debe indicar un usuario");
		}
		List<MedicionInBody> resultado = new ArrayList<MedicionInBody>();
		
		for(MedicionInBody m: mediciones) {
			
			if(m.getUsuario().equals(usuario)) {
				
				resultado.add(m);
			}
		}
		
		OrdenarPorInsercion.ordenar(resultado, new Comparator<MedicionInBody>(){
			
			@Override 
			public int compare(MedicionInBody a, MedicionInBody b) {
				
				return a.getFecha().compareTo(b.getFecha());
				
			}
			
		});
		
		
		return resultado;
		
	}
	
	public Optional<MedicionInBody> obtenerUltimaMedicion(Usuario usuario){
		
		List<MedicionInBody> ordenadas = listarPorUsuario(usuario);
		if(ordenadas.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(ordenadas.get(ordenadas.size() -1));
	}
	
	public VariacionInBody comparar(Usuario usuario, LocalDate fechaAnterior, LocalDate fechaPosterior) {
		
		
		if(fechaAnterior == null || fechaPosterior == null) {
			
			
			throw new MedicionInvalidaException("Debe indicar las dos fechas");
		}
		
		if(!fechaAnterior.isBefore(fechaPosterior)) {
				
				throw new MedicionInvalidaException("La primera fecha debe ser anterior a la segunda. ");
		}
		
		if(usuario == null) {
			
			throw new UsuarioInvalidoException("Debe indicar un usuario ");
	}
		
		MedicionInBody anterior = buscarPorFecha(usuario, fechaAnterior);
		MedicionInBody posterior = buscarPorFecha(usuario, fechaPosterior);
		
		if(anterior == null || posterior == null) {
			throw new MedicionInvalidaException("No hay medición en alguna de las fechas indicadas.");
			
		}
		return new VariacionInBody(anterior, posterior);
	}
	
	private MedicionInBody buscarPorFecha(Usuario usuario, LocalDate fecha) {
		
		for( MedicionInBody m: mediciones) {
			
			if(m.getUsuario().equals(usuario) && m.getFecha().equals(fecha)) {
				
				return m;
			}
		}
		return null;
	}
	
	
	
	
	/**
	 * 
	 * @param peso
	 * @param estatura
	 * @return
	 */
	
	
	
	//Calcular el imc
	private double calcularImc(double peso, double estatura) {  if (peso<=0 || estatura <=0) {
		
		return 0;
		
	}
	
	return peso/(estatura *estatura);
	}
	
	
	//TODO comparar e importar se agrega después 
}
