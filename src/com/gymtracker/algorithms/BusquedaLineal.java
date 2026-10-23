package com.gymtracker.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.gymtracker.exceptions.ObjetoNuloException;

public class BusquedaLineal {
	public static <T> T buscar(List<T> lista, Predicate <? super T> criterio ) {
		if (lista == null || criterio ==null) {
			throw new ObjetoNuloException("La lista y el criterio no pueden ser nulos");
		}
		for( T elemento: lista) {
			if(criterio.test(elemento)== true) {
				return elemento;
				
			}
			
		}
		return null;
		
}
	public static <T> int buscarIndice(List<T> lista, Predicate <? super T> criterio ) {
		if (lista == null || criterio ==null) {
			throw new ObjetoNuloException("La lista y el criterio no pueden ser nulos");
		}
		int contador=0; 
		for( T elemento: lista) {
			if(criterio.test(elemento)==true) { 
				return contador;}
			contador ++;
		}
		return -1;
		
	}
	public static <T> List<T> filtrar(List<T> lista, Predicate <? super T> criterio ) {
		if (lista == null || criterio ==null) {
			throw new ObjetoNuloException("La lista y el criterio no pueden ser nulos");
		}
		List<T> array = new ArrayList<T>();
		for( T elemento: lista) {
			if(criterio.test(elemento)== true) {
				array.add(elemento);
			}
		}
		return array;
	}

}
