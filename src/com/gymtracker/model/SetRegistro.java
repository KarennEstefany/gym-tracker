package com.gymtracker.model;


import com.gymtracker.exceptions.NumeroSetInvalidoException;

public class SetRegistro {
	
	 private final int id;
	 private final int sesionId;
	 private final int ejercicioId;
	 private int numeroSet;
	 private double peso;
	 private int repeticiones;
	 private int rpe;
	 private int rir;
	 
	 
	 public SetRegistro(int id, int sesionId, int ejercicioId, int numeroSet, double peso, int repeticiones, int rpe,
			int rir) {
		super();
		this.id = id;
		this.sesionId = sesionId;
		this.ejercicioId = ejercicioId;
		this.numeroSet = numeroSet;
		this.peso = peso;
		this.repeticiones = repeticiones;
		this.rpe = rpe;
		this.rir = rir;
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
 	 * @return the peso
 	 */
	 public double getPeso() {
		 return peso;
	 }


	 /**
 	 * @param peso the peso to set
 	 */
	 public void setPeso(double peso) {
		 this.peso = peso;
	 }


	 /**
 	 * @return the repeticiones
 	 */
	 public int getRepeticiones() {
		 return repeticiones;
	 }


	 /**
 	 * @param repeticiones the repeticiones to set
 	 */
	 public void setRepeticiones(int repeticiones) {
		 this.repeticiones = repeticiones;
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
		 this.rpe = rpe;
	 }


	 /**
 	 * @return the rir
 	 */
	 public int getRir() {
		 return rir;
	 }


	 /**
 	 * @param rir the rir to set
 	 */
	 public void setRir(int rir) {
		 this.rir = rir;
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
	 public int getSesionId() {
		 return sesionId;
	 }


	 /**
 	 * @return the ejercicioId
 	 */
	 public int getEjercicioId() {
		 return ejercicioId;
	 }
	 
	 
	 
	 
	 
	 
	 
	 

}
