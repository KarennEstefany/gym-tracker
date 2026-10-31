package com.gymtracker.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.EstructuraVaciaException;
import com.gymtracker.exceptions.FechaInvalidaException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.SesionEnCursoException;
import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.SinRutinaParaHoyException;
import com.gymtracker.exceptions.SinSesionActivaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.EstadoSesion;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetPeso;
import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.SetRegistro;
import com.gymtracker.model.SetTiempo;
import com.gymtracker.model.TipoEjercicio;
import com.gymtracker.model.Usuario;
import com.gymtracker.structures.ColaSets;
import com.gymtracker.structures.PilaAcciones;
import com.gymtracker.model.DiaSemana;

/**
 * Guarda el historial de sets, el registro de la sesion
 */
public class SesionService {

	private final RutinaService rutinaService; 
	private SesionEntrenamiento sesionActiva;
	private  ColaSets<SetPlaneado> colaPendientes; //Se renueva en cada sesion
	private PilaAcciones<AccionSet> pilaDeshacer;  //Se renueva en cada sesion
	private final List<SetRegistro> historial;   //Lista de todas las sesiones
	private final List<SesionEntrenamiento> sesiones; //Lista que permite navegar entre sesiones
	private int siguienteIdSesion;
	private int siguienteIdSet;
	

	
	public SesionService(RutinaService rutinaService) {
		if (rutinaService==null) {
			throw new ObjetoNuloException("El RutinaService no puede ser nulo");
		}

		this.pilaDeshacer =new PilaAcciones<>();
		this.rutinaService = rutinaService;
		this.historial = new ArrayList<>();
		this.colaPendientes = new ColaSets<>();
		this.sesiones = new ArrayList<>();
		this.siguienteIdSesion=1;
		this.siguienteIdSet=1;
	
	}
	
	public List<SetRegistro> getHistorial() {
		return Collections.unmodifiableList(historial);
	}

	/** R6: inicia el entrenamiento con la rutina que el usuario tiene para hoy. */
	public SesionEntrenamiento iniciarSesion(Usuario usuario) {
		return iniciarSesion (usuario, LocalDate.now());
	}
	
	public SesionEntrenamiento iniciarSesion(Usuario usuario, LocalDate fecha) {
		if (usuario==null) {
			throw new UsuarioInvalidoException("Debe indicar un usuario");
		}
		if (fecha==null)
		{	throw new FechaInvalidaException("La fecha no puede ser nula");}
		if (sesionActiva!= null&& sesionActiva.getEstado()== EstadoSesion.EN_CURSO) {
			throw new SesionEnCursoException("Ya hay una sesion activa, debe finalizarla antes de iniciar otra");
			
		}
		
		DiaSemana dia = mapearDiaSemana(fecha.getDayOfWeek());
		Optional<Rutina> rutina = rutinaService.buscarRutinaPorDia(usuario, dia);
		
		if (!rutina.isPresent()) {
			throw new SinRutinaParaHoyException("No hay rutina planificada para hoy ("+dia+")");}
		
		List<RutinaEjercicio> ejercicios = rutinaService.obtenerEjercicios(rutina.get());
		if (ejercicios.isEmpty()) {
			throw new RutinaInvalidaException("La rutina de hoy no tiene ejercicios");
		}
		
		ColaSets<SetPlaneado> nuevaCola = new ColaSets<>();
		for (RutinaEjercicio rutE : ejercicios) {
			for (int numero =1; numero<= rutE.getSetsObjetivo();numero++) {
				nuevaCola.encolar(new SetPlaneado(numero, rutE.getEjercicio(), rutE.getObjetivo()));
			}
		}
		SesionEntrenamiento sesion = new SesionEntrenamiento(siguienteIdSesion, fecha, usuario, rutina.get());
		siguienteIdSesion++;
		sesiones.add(sesion);
		sesionActiva=sesion;
		colaPendientes = nuevaCola;
		pilaDeshacer= new PilaAcciones<>();
		return sesion;
	}
	public SetPlaneado verSetPendiente() {
		validarSesionEnCurso();
		if (colaPendientes.estaVacia()) {
			throw new EstructuraVaciaException("No quedan sets pendientes");
		}
		return colaPendientes.verTope();
	}
	public boolean haySetsPendientes() {
		return !colaPendientes.estaVacia();
		
	}
	
	/** R7: registra el set pendiente cuando el ejercicio se mide por peso. */
	
	public SetPeso registrarSetPeso(double peso, int repeticiones, int rpe, int rir) {
		SetPlaneado planeado = verSetPendiente();
		validarTipo(planeado, TipoEjercicio.PESO);
		SetPeso set = new SetPeso(siguienteIdSet, sesionActiva, planeado.getEjercicio(),
				planeado.getNumeroSet(), rpe, rir, peso, repeticiones);
		confirmarRegistro(set, planeado);
		return set;
	}
	/** R7: registra el set pendiente cuando el ejercicio se mide por tiempo */
	public SetTiempo registrarSetTiempo(int duracionSegundos, int rpe, int rir) {
			SetPlaneado planeado= verSetPendiente();
			validarTipo(planeado, TipoEjercicio.TIEMPO);
			
			SetTiempo set = new SetTiempo(siguienteIdSet, sesionActiva, planeado.getEjercicio(),
					planeado.getNumeroSet(), rpe,rir, duracionSegundos);
			confirmarRegistro(set, planeado);
			return set;
	}
	
	/**
	 * 
	 * R9:  Deshace el último set registrado en la sesionActiva y lo devuelve al frente de la cola
	 * 
	 */
	
	public void deshacerUltimoSet() {
        // 1. Validar que haya sesión activa y que esté EN_CURSO
		
		if(sesionActiva == null) {
			
			throw new SinSesionActivaException("No hay una sesion activa");
		}
		if(sesionActiva.getEstado() != EstadoSesion.EN_CURSO) {
		
			throw new SesionYaFinalizadaException("La sesión ya finalizó: no se puede deshacer"); 
			
		}
	
	
        // 2. Sacar la acción de la pila (pop)
		
		AccionSet accion  = pilaDeshacer.pop();
		
		
        // 3. Quitar el set del final del historial
		historial.removeLast();
		 
        // 4. Devolver el set planeado al FRENTE de la cola
		colaPendientes.encolarAlFrente(accion.getSetPlaneado());
    } 
	/** R10:finaliza la sesion activa */
	public void finalizarSesion() {
		validarSesionEnCurso();
		sesionActiva.finalizarSesion();
	}
	
	/**Historial de sessiones del usuario en orden cronologico de creacion */
	
	public List<SesionEntrenamiento> listarSesiones(Usuario usuario){
		List<SesionEntrenamiento> resultado = new ArrayList<>();
		for (SesionEntrenamiento sesion : sesiones) {
			if (sesion.getUsuario().equals(usuario)) {
				resultado.add(sesion);
			}
		}
		return resultado;
		
	}
	public SesionEntrenamiento getSesionActiva() {
		return sesionActiva;
	}
	
	/////////////////////Metodos auxiliares del service//////////////////////////////
	
	private void confirmarRegistro(SetRegistro set, SetPlaneado sPlaneado) {
		colaPendientes.desencolar();
		historial.add(set);
		pilaDeshacer.push(new AccionSet(set, sPlaneado));
		siguienteIdSet++;
		
	}
	private void validarSesionEnCurso() {
		if (sesionActiva==null) {
			throw new SinSesionActivaException("No hay sesion activa");
		}
		if (sesionActiva.getEstado()!= EstadoSesion.EN_CURSO) {
			throw new SesionYaFinalizadaException("Ya fue finalizada la sesion");
		}
	}
	
	private void validarTipo(SetPlaneado sPlaneado, TipoEjercicio esperado) {
		if (sPlaneado.getEjercicio().getTipo()!= esperado) {
			throw new EjercicioInvalidoException("El set pendiente es de tipo " + sPlaneado.getEjercicio().getTipo());
					
		}
	}
	private DiaSemana mapearDiaSemana(DayOfWeek dia) {
		switch (dia) {
		case MONDAY: return DiaSemana.LUNES;
		case TUESDAY:return DiaSemana.MARTES;
		case WEDNESDAY: return DiaSemana.MIERCOLES;
		case THURSDAY: return DiaSemana.JUEVES;
		case FRIDAY: return DiaSemana.VIERNES;
		case SATURDAY: return DiaSemana.SABADO;
		default : return DiaSemana.DOMINGO;
		}
		
	}

}
