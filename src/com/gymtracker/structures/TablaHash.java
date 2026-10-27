package com.gymtracker.structures;

import java.util.ArrayList;
import java.util.List;

import com.gymtracker.exceptions.CapacidadInvalidaException;
import com.gymtracker.exceptions.ObjetoNuloException;

/**
 * Tabla hash genérica con manejo de colisiones por encadenamiento.
 *
 * Cada casillero guarda una lista
 * enlazada de nodos {@link NodoHash} con las claves que cayeron en esa posición.
 * Cuando la tabla se llena más del 75 % (factor de carga) duplica su capacidad y
 * redistribuye los nodos (rehashing), para que las cadenas se mantengan cortas.
 *
 * No admite claves ni valores nulos. Usa {@code hashCode()} y {@code equals()}
 * de la clave, por lo que la clase usada como clave debe implementarlos bien
 * (String e Integer ya lo hacen).
 * 
 * 
 *
 * @param <K> tipo de la clave (por ejemplo, el código del ejercicio)
 * @param <V> tipo del valor (por ejemplo, el Ejercicio)
 */
public class TablaHash<K, V> {
	private static final int CAPACIDAD_INICIAL=16;
	private static final double FACTOR_CARGA=0.75;
	private NodoHash<K, V> [] tabla;
	private int tamanio;
	
	public TablaHash(){
		this(CAPACIDAD_INICIAL);
	}
	@SuppressWarnings("unchecked")
	public TablaHash(int capacidad){
		if(capacidad <= 0) {
			throw new CapacidadInvalidaException("La capacidad de la tabla debe ser mayor a 0");
		}
		this.tabla= new NodoHash[capacidad];
		this.tamanio=0;
	}
	
	public int darTamanio() {
		return tamanio;
	}
	public int darCapacidad() {
		return tabla.length;
	}
	public boolean estaVacia() {
		return tamanio==0;
	}
	/**
	 * Inserta el par en la tabla. Si ya existe esa clave, solo reemplaza el valor
	 * @param clave
	 * @param valor
	 */
	public void insertar(K clave, V valor) {
		verificarClave(clave);
		if(valor ==null) {
			throw new ObjetoNuloException("El valor no puede ser nulo");
		}
		NodoHash<K,V> nodoExistente = buscarNodo(clave);
		
		if (nodoExistente!= null) {
			nodoExistente.setValor(valor);
			return;
		}
		
		int indice=indiceHash(clave, tabla.length);
		NodoHash<K,V> nuevoNodo = new NodoHash<>(clave, valor, tabla[indice]);// se crea un nuevo nodo con la clave y valor que nos pasaron
		tabla[indice]=nuevoNodo;
		tamanio++;
		
		if( (double) tamanio / tabla.length> FACTOR_CARGA) {
			redimensionarTabla();
		}
		
		
	}
	/**
	 * Buscar el valor asociado a la clave
	 * @param clave
	 * @return retorna null si el nodo es nulo, retorna su valor si existe
	 */
	public V buscarValor(K clave) {
		verificarClave(clave);
		NodoHash<K,V> nodo = buscarNodo(clave);
		return nodo ==null ? null : nodo.getValor();
	}
	/**
	 * Indica si la clave buscada existe en la tabla
	 * @param clave
	 * @return
	 */
	public boolean contieneClave(K clave) {
		verificarClave(clave);
		return buscarNodo(clave) != null;
	}
	/**
	 * Elimina la clave y retorna el valor eliminado
	 * @param clave
	 * @return retorna null si la clave no existia
	 */
	public V eliminar(K clave) {
		verificarClave(clave);
		int indice = indiceHash(clave, tabla.length);
		NodoHash<K, V> anterior = null;
		NodoHash<K, V> actual = tabla[ indice];
		
		while(actual!= null) {
			if (actual.getClave().equals(clave)) {
				if(anterior==null) {
					tabla[indice]=actual.getNodoSiguiente();
				}
				else {
					anterior.setNodoSiguiente(actual.getNodoSiguiente());
				}
				actual.setNodoSiguiente(null);
				tamanio--;
				return actual.getValor();
			}
			anterior=actual;
			actual=actual.getNodoSiguiente();
		}
		return null;
	}
	/**
	 * Devuelve todos los valores, no tiene orden
	 * @return
	 */
	public List<V> listarValores(){
		List<V> devolver = new ArrayList<>();
		for (NodoHash<K, V> nodo : tabla) {
			for (NodoHash<K,V> actual = nodo; actual!= null; actual=actual.getNodoSiguiente()) {
				devolver.add(actual.getValor());
			}
		}
		return devolver;
		
	}
	/**
	 * Devuelve todas las claves, sin orden garantizado
	 * @return
	 */
	public List<K> listarClaves(){
		List<K> devolver = new ArrayList<>();
		for (NodoHash<K, V> nodo: tabla) {
			for (NodoHash<K, V> actual = nodo; actual!= null; actual=actual.getNodoSiguiente()) {
				devolver.add(actual.getClave());
			}
		}
		return devolver;
	}
	
	
	//////////////////////////Metodos auxiliares////////////////////////
	/**
	 * Crea del indice hash para asignar una posicion valida dentro del arreglo
	 * Se utiliza 0x7fffffff para borrar el signo y que el resultado no sea negativo
	 * @param clave
	 * @param capacidad del arreglo
	 * @return
	 */
	private int indiceHash(K clave, int capacidad) {
		return (clave.hashCode() & 0x7fffffff) %capacidad;
	}
	/**
	 * Busca el nodo recorriendo la cadena del casillero donde sebe estar
	 * @param clave
	 * @return
	 */
	private NodoHash<K,V> buscarNodo(K clave){
		NodoHash<K,V> nodoActual= tabla[indiceHash(clave, tabla.length)];
		while(nodoActual !=null) {
			if(nodoActual.getClave().equals(clave)) {
				return nodoActual;
			}
			nodoActual=nodoActual.getNodoSiguiente();
		}
		return null;
	}
	/**
	 * Redimensiona el arreglo, duplica su capacidad y reubica a los nodos en su nueva posicion
	 * Solo se usa al llevar al 75% de capacidad
	 */
	@SuppressWarnings("unchecked")
	private void redimensionarTabla() {
		int nuevaCapacidad = tabla.length * 2;
		NodoHash<K,V>[] nuevaTabla = new NodoHash[nuevaCapacidad];
		
		for(NodoHash<K,V> nodoCabeza : tabla) {
			NodoHash<K,V> actual=nodoCabeza;
			while(actual != null) {
				NodoHash<K,V> nodoSiguiente = actual.getNodoSiguiente(); //Guardar el nodo antes de hacer el cambio de dimensiones
				int i=indiceHash(actual.getClave(), nuevaCapacidad); //Cambian los indices al cambiar el tamanio del arreglo
				actual.setNodoSiguiente(nuevaTabla[i]);
				nuevaTabla[i] = actual;
				actual=nodoSiguiente;
			}
		}
		tabla= nuevaTabla;
		
	}
	/**
	 * Verifica que la clave no sea nula
	 * @param clave
	 */
	private void verificarClave(K clave) {
		if (clave == null) {
			throw new ObjetoNuloException("Ingrese una clave no nula");
		}
	}
}

