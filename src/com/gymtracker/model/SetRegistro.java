package com.gymtracker.model;


import com.gymtracker.exceptions.EjercicioInvalidoException;
import com.gymtracker.exceptions.NumeroSetInvalidoException;
import com.gymtracker.exceptions.RirInvalidoException;
import com.gymtracker.exceptions.RpeInvalidoException;
import com.gymtracker.exceptions.SesionInvalidaException;

public abstract class SetRegistro {
	
	 private final int id;
	 private final SesionEntrenamiento sesion;
	 private final Ejercicio ejercicio;
	 private int numeroSet;
	 private int rpe;
	 private int rir;
	 
	 
	 


	 public SetRegistro(int id, SesionEntrenamiento sesion, Ejercicio ejercicio, int numeroSet, int rpe, int rir) {
		
		 
		 if(sesion == null) {
			 
			 throw new SesionInvalidaException("El set debe estar asociado a una excepción");
		 }
		 
		 if(ejercicio == null) {
			 throw new EjercicioInvalidoException("El set debe estar asociado a un ejercicio");
		 }
		this.id = id;
		this.sesion = sesion;
		this.ejercicio = ejercicio;
		setNumeroSet(numeroSet);
		setRpe(rpe);
		setRir(rir);  // Se valida también cuando se crea el objeto 
		
	 }
	 

	 /**
 	 * @return the id
 	 */
	 public int getId() {
		 return id;
	 }

	 
	 /**
 	 * @return the sesionId
 	 */
	
	 public SesionEntrenamiento getSesion() {
		 return sesion;
	 }

	 /**
 	 * @return the numeroSet
 	 */
	 public int getNumeroSet() {
		 return numeroSet;
	 }


	 /**
 	 * @param numeroSet the numeroSet to set
 	 */
	 public void setNumeroSet(int numeroSet) {
		 
		 if (numeroSet <= 0) {
			    throw new NumeroSetInvalidoException("El número de set debe ser mayor a cero");
			}
		
		 this.numeroSet = numeroSet;
		 
	 }

	 

	 /**
 	 * @return the rpe
 	 */
	 public int getRpe() {
		 return rpe;
	 }


	 /**
 	 * @param rpe the rpe to set
 	 */
	 public void setRpe(int rpe) {
		 if (rpe < 1 || rpe > 10) {
	            throw new RpeInvalidoException("RPE debe estar entre 1 y 10");
	        }
		 this.rpe = rpe;
	 }


	 /**
 	 * @return the rir
 	 */
	 
	 //Se asume RIR en escala de 0 a 5 según la convención estándar de entrenamiento de fuerza. 
	 public int getRir() {
		 
		 if(rir<0 || rir > 5 ) {
			 throw new RirInvalidoException("El rango estándar está entre 0 y 5 ");
		 }
		 
		 return rir;
	 }


	 /**
 	 * @param rir the rir to set
 	 */
	 public void setRir(int rir) {
		 this.rir = rir;
	 }





	 /**
 	 * @return the ejercicioId
 	 */
	 public Ejercicio getEjercicio() {
		 return ejercicio;
	 }
	 
	 //Método que deben implementar las dos clases que heredan de esta,
	 //dependiendo de si se calcula por tiempo o peso 
	 public abstract double calcularVolumen();
	 
	 
	 
	 
	 
	 
	 

}
