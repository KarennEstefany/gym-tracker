package com.gymtracker.structures;

public class NodoLista<T> {
	private T dato; // T= Un objeto
	private NodoLista<T> nodoSiguiente;
	/**
	 * @param dato
	 * @param nodoSiguiente
	 */
	public NodoLista(T dato) {
		super();
		this.dato = dato;
		this.nodoSiguiente =  null;
	}
	public T getDato() {
		return dato;
	}
	public NodoLista<T> getNodoSiguiente() {
		return nodoSiguiente;
	}
	public void setNodoSiguiente(NodoLista<T> nodoSiguiente) {
		this.nodoSiguiente = nodoSiguiente;
	}
	

}
