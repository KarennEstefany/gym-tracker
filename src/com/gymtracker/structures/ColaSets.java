package com.gymtracker.structures;


import com.gymtracker.exceptions.EstructuraVaciaException;
import com.gymtracker.exceptions.ObjetoNuloException;


public class ColaSets<T> {
	private ListaEnlazada<T> elementos = new ListaEnlazada();
	
	public boolean estaVacia() {
		if (elementos.estaVacia()) {
			return true;
		}
		return false;
	} 
	public int darTamanio() {
		return elementos.darTamanio();
	}
	public void encolar(T elemento) {
		elementos.agregarAlFinal(elemento);
	}
	
	public void encolarAlFrente(T elemento) {
		elementos.agregarAlInicio(elemento);
	}
	public T desencolar() {
		if (estaVacia()) {
			throw new EstructuraVaciaException("La cola esta vacia, no se puede desencolar");
		}
		return elementos.eliminarPrimero();
		
	}
	public void verTope() {
		if (estaVacia()) {
			throw new EstructuraVaciaException("La cola esta vacia, no se puede ver el tope");
		}
		elementos.obtenerPrimero();
	}
	
}
