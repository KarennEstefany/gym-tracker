package com.gymtracker.structures;

import java.util.Objects;

import com.gymtracker.exceptions.EstructuraVaciaException;

public class ListaEnlazada<T> {
	private int tamanio;
	private NodoLista<T> cabeza;
	private NodoLista<T> cola;
	/**
	 * @param tamanio
	 * @param cabeza
	 * @param cola
	 */
	public ListaEnlazada() {
		this.tamanio = 0;
		this.cabeza = null;
		this.cola = null;
	}
	
	public int darTamanio() {
		return tamanio;
	}
	public boolean estaVacia() {
		if (cabeza == null && tamanio == 0) {
			return true;
		}
		return false;
	}
	
	public void agregarAlInicio (T objeto) {
		NodoLista<T> nuevoNodo = new NodoLista<T>(objeto);
		
		nuevoNodo.setNodoSiguiente(cabeza);
		cabeza=nuevoNodo;
		tamanio++;
		if (cola ==null) {
			cola=nuevoNodo;
		}
		
	}
	
	public void agregarAlFinal (T objeto) {
		NodoLista<T> nuevoNodo = new NodoLista<T>(objeto);
		if(estaVacia()) {
			cabeza = nuevoNodo;
		}
		else {
		cola.setNodoSiguiente(nuevoNodo);
		}
		cola=nuevoNodo;
		tamanio++;
		
		}
	public T obtenerPrimero () {
		if (estaVacia()) {
			throw new EstructuraVaciaException("No hay objetos para obtener");
		}
		return cabeza.getDato();
	}
	public T eliminarPrimero () {
		if (estaVacia()) {
			throw new EstructuraVaciaException("No hay objetos para eliminar");
		}
		else {
			NodoLista<T> puntero=cabeza;
			cabeza=puntero.getNodoSiguiente();
			puntero.setNodoSiguiente(null);
			 if (cabeza == null){
					cola=null;}
			tamanio--;
			return puntero.getDato();
			
		}
	}
	public T buscar(T objeto) {
		NodoLista<T> puntero = cabeza;
		while (puntero !=null) {
			if (Objects.equals(puntero.getDato(),objeto)) {
				return puntero.getDato();
			}
			else {
				puntero=puntero.getNodoSiguiente();
			}
		}
		return null;
	}
	public T eliminar(T objeto) {
		if (estaVacia()) {
	        return null;
	    }
	    if (Objects.equals(cabeza.getDato(), objeto)) {
	        return eliminarPrimero();
	    }
	    NodoLista<T> anterior = cabeza;
	    NodoLista<T> actual = cabeza.getNodoSiguiente();
	    while (actual != null) {
	        if (Objects.equals(actual.getDato(), objeto)) {
	            anterior.setNodoSiguiente(actual.getNodoSiguiente());
	            if (actual == cola) {
	                cola = anterior;
	            }
	            actual.setNodoSiguiente(null);
	            tamanio--;
	            return actual.getDato();
	        }
	        anterior = actual;
	        actual = actual.getNodoSiguiente();
	    }
	    return null;
		}
	

}
