package com.gymtracker.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gymtracker.algorithms.BusquedaLineal;
import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.GrupoMuscularInvalidoException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.exceptions.PesoInvalidoException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.SetRegistro;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.structures.TablaHash;

/**
 * Filtra y calcula los records personales en cada consulta, lee el historial se SesionService para responder
 *La interfaz lo requiere para mostrar records personales(PRs) y el volumen semanal
 */
public class RecordService {
	private static final double TOLERANCIA_PESO= 0.0001;
	private final SesionService sesionService;
	
	public RecordService(SesionService sesionService) {
		if(sesionService== null) {
			throw new ObjetoNuloException(" El sesion service no puede ser nulo");
			
		}
		this.sesionService=sesionService;
	}
	//////////==========================Requisito funcional #11: Calcular PR de peso maximo===================//////////
	
	/** Devuelve el set con el mayor peso que el usuario ha levantado en ese ejercicio
	 * El record de cada ejercicio empieza en null
	 * 	Utiliza optional que puede guardar un setPeso o nada, retorna un mensaje que se decide en la interfaz si esta vacio
	 * Si esta vacio, es normal ya que no existe record de un ejercicio que no se ha practicado
	 *  Optional<SetPeso> Guarda un Set o serie ya que un record no solo es el peso, sino todo lo correspondiente a ese set (repeticiones, peso, fecha, ejercicio)
	 * 
	 */
	
	public Optional<SetPeso> obtenerRecordPeso(Usuario usuario, Ejercicio ejercicio){
		validarUsuario(usuario);
		validarEjercicioDePeso(ejercicio); 
		
		SetPeso record = null;
		for(SetPeso set : setDePeso(usuario, ejercicio)) {
			if(record == null || set.getPeso()> record.getPeso()) {
				record = set;
			}
			
		}
		return Optional.ofNullable(record); 
	}
	
//////////=====================Requisito funcional #12: Calcular PR de repeticiones maximas=====================//////////

 /** Devuelve el set con la mayor cantidad de repeticiones que el usuario ha realizado en ese ejercicio con ese peso exacto
	 * El record de cada ejercicio empieza en null
	 * 	Utiliza optional que puede guardar un setPeso o nada, retorna un mensaje que se decide en la interfaz si esta vacio
	 * Si esta vacio, es normal ya que no existe record de un ejercicio que no se ha practicado
	 *  Optional<SetPeso> Guarda un Set o serie ya que un record no solo es las repeticiones, sino todo lo correspondiente a ese set (repeticiones, peso, fecha, ejercicio)
	 * @throws PesoInvalidoException      si el peso es negativo
	 * @throws EjercicioInvalidoException si el ejercicio no se mide por peso
	 */
	
	public Optional<SetPeso> obtenerRecordRepeticiones( Usuario usuario, Ejercicio ejercicio, double peso) {
		validarUsuario(usuario);
		validarEjercicioDePeso(ejercicio);
		
		if(peso<0) {
			throw new PesoInvalidoException("El peso no puede ser negativo");
		}
		SetPeso record = null;
		for (SetPeso set : setDePeso(usuario, ejercicio)) {
			boolean mismoPeso = Math.abs(set.getPeso()-peso) < TOLERANCIA_PESO;
			if(mismoPeso && (record== null || set.getRepeticiones()> record.getRepeticiones())) {
				record=set;
			}
		}
		
		return Optional.ofNullable(record); 
	}
	
//////////=====================Requisito funcional #13: Calcular el volumen semanal=============================//////////

	/**
	 * Calcula el numero de sets que hizo el usuario para ese grupo muscular en la semana (lunes a domingo) que contiene la fecha ingresada
	 * Cuenta sets de peso y sets de tiempo
	 * @param usuario 
	 * @param grupoMuscular grupo muscular del que se quiere saber el volumen
	 * @param fecha
	 * @return el numero de sets 
	 */
	public int calcularVolumenSemanal(Usuario usuario, String grupoMuscular, LocalDate fecha) {
		validarUsuario(usuario);
		validarGrupoMuscular(grupoMuscular);
		validarFecha(fecha);
		LocalDate inicio = inicioSemana(fecha); //Obtiene el lunes de esa semana
		LocalDate fin = inicio.plusDays(6); // Llega al domingo
		
		//La busqueda lineal contiene 3 condicioness(validacion de pertenencia al usuario, al grupo muscular y el rango de fechas)
		int sets =  BusquedaLineal.filtrar(sesionService.getHistorial(), s -> perteneceAlUsuario(s, usuario) && s.getEjercicio().getGrupoMuscular().equals(grupoMuscular)
				&& fechaEstaEntre(s.getSesion().getFecha(), inicio, fin)).size();
		return sets;
	}
	
	/**
	 * Calcula el numero de sets que hizo el usuario para cada grupo muscular
	 * Los grupos musculares que no se trabajaron en la semana no aparecen
	 * @param usuario
	 * @param fecha
	 * @return
	 */
	public TablaHash<String, Integer> calcularVolumenSemanalTotal(Usuario usuario, LocalDate fecha){
		validarUsuario(usuario);
		validarFecha(fecha);
		LocalDate inicio = inicioSemana(fecha);
		LocalDate fin = inicio.plusDays(6);
		//Solo filtra los sets del usuario para esa semana, sin filtrar por grupo muscular
		List<SetRegistro> setsSemana = BusquedaLineal.filtrar(sesionService.getHistorial(), s-> perteneceAlUsuario(s, usuario) && fechaEstaEntre(s.getSesion().getFecha(), inicio, fin));
	
		//Cuenta por grupo muscular, usando el grupo como clave
		TablaHash < String, Integer> volumen = new TablaHash<String, Integer>();
		for (SetRegistro set : setsSemana) {
			String grupo = set.getEjercicio().getGrupoMuscular();
			Integer actual = volumen.buscarValor(grupo);
			volumen.insertar(grupo, actual == null ? 1: actual +1);
		}
		return volumen;
	}
	
	
	
	/////////////////////////////////////////  Metodos auxiliares            /////////////////////////////////
	
	
	//=================Filtra los sets de peso de un usuario para un ejercicio especifico buscando en el historial===================//
	private List<SetPeso> setDePeso(Usuario usuario, Ejercicio ejercicio){
	// Filtrar se le aplica a cada set del historial, solo devuelve los que pertenecen al usuario y son del ejercicio solicitado
		List<SetRegistro> registroEjercicio = BusquedaLineal.filtrar(sesionService.getHistorial(), s -> perteneceAlUsuario(s, usuario) && mismoEjercicio(s, ejercicio));
		
		List<SetPeso> resultado = new ArrayList<>();
		for(SetRegistro set: registroEjercicio) {
			if(set instanceof SetPeso setPeso) { //Verifica que solo se adicionen los sets de peso al verificar el tipo
				resultado.add(setPeso);
			}
		}
		return resultado;
	}
	
	//===============Verifica que la sesion del set pertenezca al usuario=================///////////////////
	private boolean perteneceAlUsuario(SetRegistro set, Usuario usuario) {
		return set.getSesion().getUsuario().equals(usuario);
	}
	///================== Compara ejercicios por su codigo =============================///////////////////////////
	private boolean mismoEjercicio(SetRegistro set, Ejercicio ejercicio) {
		return set.getEjercicio().getCodigo().equals(ejercicio.getCodigo());
	}
	//===========La semana de entreno va de lunes a domingo, devuelve el inicio de la semana (lunes) de la fecha ingresada ===============//
	private LocalDate inicioSemana(LocalDate fecha) {
		return fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // previosaOrSame devuelve el lunes de esa semana ( si se ingresa la fecha del lunes, deja esa fecha)
	}
	//=======Calcula si la fecha esta dentro del rango, incluye los extremos==========/////////////
	private boolean fechaEstaEntre(LocalDate fecha, LocalDate inicio, LocalDate fin) {
		return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
	}
		
	
	

	
	
	
	//=======================Metodos que validan los datos y devuelven excepciones propias==============================//
	
	private void validarUsuario(Usuario usuario) {
		if(usuario == null) {
			throw new UsuarioInvalidoException("Indique un usuario");
		}
	}
	private void validarEjercicioDePeso(Ejercicio ejercicio) {
		if(ejercicio == null) {
			throw new EjercicioInvalidoException("Ingrese un nombre valido del ejercicio");
		}
		if(ejercicio.getTipo()!= TipoEjercicio.PESO) {
			throw new EjercicioInvalidoException("El ejercicio " + ejercicio.getNombre() +" no se mide por peso, ingrese un ejercicio de peso");
		}
	}
	private void validarGrupoMuscular(String grupoMuscular) {
		if(grupoMuscular== null || grupoMuscular.trim().isEmpty()) {
			throw new GrupoMuscularInvalidoException("Ingrese el grupo muscular que corresponde al ejercicio");}
			
		}
		private void validarFecha(LocalDate fecha) {
			if (fecha== null) {
				throw new FechaInvalidaException("La fecha no puede ser nula");
			}
		
	}
}
