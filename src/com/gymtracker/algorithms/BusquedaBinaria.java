package com.gymtracker.algorithms;

import java.util.List;

/**
 * Clase de utilidad que proporciona algoritmos de búsqueda.
 * 
 * @param <T> el tipo de elementos gestionados por este algoritmo, 
 *            los cuales deben ser comparables entre sí.
 *
 */

public class BusquedaBinaria<T> {
	
	/**
     * Realiza una búsqueda binaria en una lista ordenada para encontrar el elemento objetivo especificado.
     * 
     * @param <T> el tipo de elementos en la lista, que implementa {@link Comparable}
     * @param lista la {@link List} ordenada de elementos en la cual se va a buscar
     * @param buscado el elemento objetivo que se desea encontrar
     * @return el índice del elemento objetivo si se encuentra; de lo contrario, {@code -1}
     * @throws NullPointerException si la lista especificada o el objetivo son nulos
     */
	public static <T extends Comparable<T>> int  busquedaBinaria(List<T> lista, T buscado) {
		
		int inicio = 0;
		int fin  = lista.size()-1;
		
		
		while(inicio <= fin) {
			
			int medio = inicio + (fin-inicio)/2;
			
			if(lista.get(medio).compareTo(buscado)==0) {
				return medio;
				
			}else if(buscado.compareTo(lista.get(medio))>0) {
				inicio = medio + 1; 
				
			}else {
				
				fin = medio -1; 
			}
		}
		return -1;
		
		
		
	}

}
