package com.gymtracker.structures;

/**
 *Nodo que se usara en la tabla hash, casa nodo guarda una clave, un valor 
 *y la referencia al nodo siguiente
 * @param <K> tipo de la clave (por ejemplo, el código del ejercicio)
 * @param <V> tipo del valor (por ejemplo, el Ejercicio)
 */
public class NodoHash<K, V> {
	private final K clave; 
	private V valor;
	private NodoHash<K, V> nodoSiguiente;
	/**
	 * @param clave
	 * @param valor
	 * @param nodoSiguiente
	 */
	public NodoHash(K clave, V valor, NodoHash<K, V> nodoSiguiente) {
		super();
		this.clave = clave;
		this.valor = valor;
		this.nodoSiguiente = nodoSiguiente;
	}
	public V getValor() {
		return valor;
	}
	public void setValor(V valor) {
		this.valor = valor;
	}
	public NodoHash<K, V> getNodoSiguiente() {
		return nodoSiguiente;
	}
	public void setNodoSiguiente(NodoHash<K, V> nodoSiguiente) {
		this.nodoSiguiente = nodoSiguiente;
	}
	public K getClave() {
		return clave;
	}
	
	
}
