package com.gymtracker.service;

import java.util.ArrayList;
import java.util.List;

import com.gymtracker.exceptions.SesionYaFinalizadaException;
import com.gymtracker.exceptions.SinSesionActivaException;
import com.gymtracker.model.EstadoSesion;
import com.gymtracker.model.SesionEntrenamiento;
import com.gymtracker.model.SetPlaneado;
import com.gymtracker.model.SetRegistro;
import com.gymtracker.structures.ColaSets;
import com.gymtracker.structures.PilaAcciones;

public class SesionService {
	
	
	private SesionEntrenamiento sesionActiva;
	private final ColaSets<SetPlaneado> colaPendientes;
	private final PilaAcciones<AccionSet> pilaDeshacer;
	private final List<SetRegistro> historial;
	
	
	public SesionService() {

		
		this.pilaDeshacer =new PilaAcciones<>();
		this.historial = new ArrayList<>();
		this.colaPendientes = new ColaSets<>();
	
	}
	/**
	 *
	 * Deshace el último set registrado en la sesionActiva 
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
		//colaPendientes.encolarAlFrente(accion.getSetPlaneado());
    } 
	
	

}
