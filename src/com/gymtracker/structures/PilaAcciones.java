package com.gymtracker.structures;

import com.gymtracker.exceptions.PilaVaciaException;

public class PilaAcciones<T>{
	
	
	private final ListaEnlazada<T> lista;
	
	
	public PilaAcciones() {
		
		this.lista = new ListaEnlazada<>();
	}
	
	
	public void push(T dato) {
		
		
		lista.agregarAlInicio(dato);
	}
	
	
	
	public T pop() {
		
		 if (estaVacia()) {
	            throw new PilaVaciaException("La pila está vacía: no hay acciones para deshacer");
	        }
		
		return lista.eliminarPrimero();
		
	}
	
	
	public T verTope() {
		 
		if(estaVacia()) {
			
			throw new PilaVaciaException("La pila está vacía.");
			
		}
		
		return lista.obtenerPrimero();
	}
	
	
	public int darTamanio() {
		
		return lista.darTamanio();
	}
	
	public boolean estaVacia() {
		
		return lista.estaVacia();
	}
	
	
	
	

}
