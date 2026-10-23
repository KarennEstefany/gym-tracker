package com.gymtracker.algorithms;

import java.util.Comparator;
import java.util.List;

import com.gymtracker.model.Ejercicio;

public class OrdenBurbuja {
	
	public static <T> void ordenar(List<T> lista, Comparator <? super T> comparador ) {
		for (int i = 0; i<lista.size()-1; i ++) {
			for(int j=0; j<lista.size()-i-1;j ++) {
				// El elemento mayor burbujea hasta el final 
				if ( comparador.compare(lista.get(j), lista.get(j + 1)) > 0) {
					T temp= lista.get(j);
					lista.set(j, lista.get(j+1));
					lista.set(j+1, temp);
				}
			}
		}
	}
}
