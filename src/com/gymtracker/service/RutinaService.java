package com.gymtracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gymtracker.exceptions.DiaRutinaDuplicadoException;
import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.RutinaInvalidaException;
import com.gymtracker.exceptions.UsuarioInvalidoException;
import com.gymtracker.model.DiaSemana;
import com.gymtracker.model.Ejercicio;
import com.gymtracker.model.Rutina;
import com.gymtracker.model.RutinaEjercicio;
import com.gymtracker.model.Usuario;

/**
 * 
 */
/**
 * 
 */
public class RutinaService {

	 private List<Rutina> rutinas;
	 private List<RutinaEjercicio> ejerciciosDeRutinas;
	 private int siguienteIdRutina;
	 private int siguienteIdRutinaEjercicio;
	 
	 
	 public RutinaService() {
		super();
		this.rutinas = new ArrayList<>();
		this.ejerciciosDeRutinas = new ArrayList();
		this.siguienteIdRutina = 1;
		this.siguienteIdRutinaEjercicio = 1;
	 }
	 
	   
	 
	 /**
	  * 
	  * R3: crea una rutina; un usuario solo puede tener una por día. Devuelve la rutina creada 
	 * @param usuario
	 * @param nombre
	 * @param dia
	 * @return nueva	 */
	public Rutina crearRutina(Usuario usuario, String nombre, DiaSemana dia) {
		 
		 validarUsuario(usuario);
		 validarDiaLibre(usuario, dia, null);
		 
		 Rutina nueva = new Rutina(siguienteIdRutina, nombre, usuario, dia);
		 siguienteIdRutina ++;  // después de crear, para no dejar huecos si el constructor falla
		 rutinas.add(nueva);
		 return nueva;
		 
	 }
	 
	
	
	 /**
	 * 
	 * R4: cambia el día de una rutina, sin perder sus ejercicios.
	 * @param rutina
	 * @param nuevoDia
	 */
	public void editarRutina(Rutina rutina, DiaSemana nuevoDia) {
		 
		 validarRutina(rutina);
		 validarDiaLibre(rutina.getUsuario(), nuevoDia, rutina);
		 rutina.setDiaSemana(nuevoDia);
	 }

	
    /**
     * R5: agrega un ejercicio al final de la rutina; el orden se asigna solo.
     * @param rutina
     * @param ejercicio
     * @param sets
     * @param objetivo	
     * @return
     */
    public RutinaEjercicio agregarEjercicio(Rutina rutina, Ejercicio ejercicio, int sets, int objetivo) {
        validarRutina(rutina);
        if (ejercicio == null) {
            throw new EjercicioInvalidoException("Debe indicar un ejercicio");
        }
        int orden = obtenerEjercicios(rutina).size() + 1;

        RutinaEjercicio nuevo = new RutinaEjercicio(siguienteIdRutinaEjercicio, rutina, ejercicio, orden, sets, objetivo);
        siguienteIdRutinaEjercicio++;
        ejerciciosDeRutinas.add(nuevo);
        return nuevo;
    }


    /**
     * 
     * R6 y R6b: la rutina del usuario para ese día, si existe (si no, es día de descanso).
     * @param usuario
     * @param dia
     * @return
     */
    public Optional<Rutina> buscarRutinaPorDia(Usuario usuario, DiaSemana dia) {
        validarUsuario(usuario);
        
        // 1. Recorremos la lista tradicionalmente con un ciclo for-each
        for (Rutina r : rutinas) {
            // 2. Evaluamos la condición manualmente para cada rutina
            if (r.getUsuario().equals(usuario) && r.getDiaSemana() == dia) {
                // 3. Si la encontramos, la envolvemos en un Optional y la retornamos de inmediato
                return Optional.of(r);
            }
        }
        
        // 4. Si el ciclo termina y no encontró nada, devolvemos un Optional vacío (día de descanso)
        return Optional.empty();
    }

    
    
    /**
     * 
     * @param usuario
     * @return resultado 
     */
    public List<Rutina> listarRutinas(Usuario usuario) {
        validarUsuario(usuario);
        List<Rutina> resultado = new ArrayList<>();
        for (Rutina r : rutinas) {
            if (r.getUsuario().equals(usuario)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    
    /** 
     *  Ejercicios de la rutina en el orden en que se agregaron. 
     * @param rutina
     * @return resultado
     */
    public List<RutinaEjercicio> obtenerEjercicios(Rutina rutina) {
        validarRutina(rutina);
        List<RutinaEjercicio> resultado = new ArrayList<>();
        for (RutinaEjercicio re : ejerciciosDeRutinas) {
            if (re.getRutina().getId() == rutina.getId()) {
                resultado.add(re);
            }
        }
        return resultado;
    }
	 
	 
	 /** Validaciones Internas */

		/**
		 * @param usuario
		 */
		private void validarUsuario(Usuario usuario) {

			if(usuario == null) {
				
				throw new UsuarioInvalidoException("Debe indicar un usuario");
			}
		}
		
		/**
		 * @param rutina
		 */
		private void validarRutina(Rutina rutina) {
			
			if(rutina == null) {
				
				throw new RutinaInvalidaException("Debe indicar una rutina");
			}
		}
		
		/**
		 * @param usuario
		 * @param dia
		 * @param excluir
		 */
		private void validarDiaLibre(Usuario usuario, DiaSemana dia, Rutina excluir) {
			
			for(Rutina r: rutinas) {
				
				Boolean mismoUsuario = r.getUsuario().equals(usuario);
				Boolean mismoDia = r.getDiaSemana()==dia;
				Boolean esLaMisma = (excluir != null && r.getId() == excluir.getId());
				if(mismoUsuario && mismoDia && !esLaMisma) {
					
					throw new DiaRutinaDuplicadoException("Ya existe una rutina para este dia de la semana");
					
				}
			}
			
		}
	 
}
