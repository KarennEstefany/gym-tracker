package com.gymtracker.structures;

import com.gymtracker.exceptions.ColaVaciaException;

public class ColaSets<T>{
	
	private Nodo<T> frente;
	private Nodo<T> fin;
	private int tamanio;
	
	
	public ColaSets() {
	
		this.frente =null;
		this.fin = null;
		this.tamanio = 0;
	}
	
	
	
	public void encolar(T dato) {
		
		
		Nodo<T> nuevoNodo = new Nodo<>(dato);
		
		if(estaVacia()) {
			
			frente = nuevoNodo;
			fin = nuevoNodo;
		} else {
			
			fin.setSiguiente(nuevoNodo);
			fin = nuevoNodo;	
		}
		
		tamanio++;
	}
	
	
	public T desencolar() {
		
		if(estaVacia()) {
			
			throw new ColaVaciaException("La cola está vacía: no hay sets pendientes");
		}
		
		T valor = frente.getDato();
		
		if(frente.getSiguiente()== null) {
			
			fin = null;
		}
		
		frente = frente.getSiguiente();
	
		tamanio --;
		 return valor;
	}
	
	
	public T verTope() {
		
		if(estaVacia()) {
			
			throw new ColaVaciaException("La cola está vacía: no hay sets pendientes");
		}
		
		return frente.getDato();
	}
	public boolean estaVacia() {
		
		return frente == null;
	}
	
	
	public int darTamanio() {
		
		return tamanio;
	}
	
	
	
	
	  	

}
