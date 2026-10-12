package com.gymtracker.structures;

import com.gymtracker.exceptions.PilaVaciaException;

public class PilaAcciones<T>{
	
	
	private Nodo<T> tope;
	private int tamanio;
	
	
	public PilaAcciones() {
		super();
		this.tope = null;
		this.tamanio = 0;
	}
	
	
	public void push(T dato) {
		
		
		Nodo<T> nuevoNodo = new Nodo<>(dato);
		
		nuevoNodo.setSiguiente(tope);
		tope  = nuevoNodo;
		tamanio ++;
	}
	
	public T pop() {
		
		if(estaVacia()) {
			
			throw new PilaVaciaException("La pila está vacía.");
			
		}
		
		T dato = tope.getDato();
		tope = tope.getSiguiente();
		tamanio --;
		return dato;
	}
	
	
	public T verTope() {
		
		if(estaVacia()) {
			
			throw new PilaVaciaException("La pila está vacía.");
			
		}
		
		return tope.getDato();
	}
	
	
	public int darTamanio() {
		
		return tamanio;
	}
	
	public boolean estaVacia() {
		
		return tope == null;
	}
	
	
	
	

}
