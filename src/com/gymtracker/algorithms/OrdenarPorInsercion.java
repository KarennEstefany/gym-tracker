package com.gymtracker.algorithms;

import java.util.Comparator;
import java.util.List;

/**
 * Ordena una lista en el mismo lugar mediante el algoritmo de ordenamiento
 * por inserción.
 * <p>
 * Recorre la lista de izquierda a derecha manteniendo una parte inicial ya
 * ordenada. En cada paso toma el siguiente elemento y lo desliza hacia la
 * izquierda, corriendo los mayores una posición, hasta ubicarlo en su lugar.
 * <p>
 * Es estable (los elementos iguales conservan su orden relativo), adaptable
 * (O(n) si la lista ya está ordenada o casi ordenada) y no usa memoria extra.
 * En el peor caso y en el caso promedio es O(n^2). Está pensado para usarse con
 * un ArrayList, donde get y set cuestan O(1).
 *
 * @param <T>        tipo de los elementos de la lista
 * @param lista      lista que se ordena; se modifica directamente
 * @param comparador define qué significa "menor" para los elementos de la lista
 */
public class OrdenarPorInsercion{

	public static <T> void ordenar(List<T> lista, Comparator<? super T> comparador) {

		// Se empieza en el segundo elemento (i = 1), el de la
		// posición 0, ya forma una parte ordenada.
		for (int i = 1; i < lista.size(); i++) {

			// Se guarda temporalmente el elemento que se va a ubicar
			T actual = lista.get(i);

			// j recorre la parte ya ordenada de derecha a izquierda,
			// empezando por el elemento inmediatamente anterior a actual.
			int j = i - 1;

			// Mientras j siga dentro de la lista (j >= 0) y el elemento de la
			// izquierda sea mayor que actual, hay que seguir buscando su lugar.
			// Se usa "> 0" estricto para que los elementos iguales no se
			// crucen y el algoritmo siga siendo estable.
			while (j >= 0 && comparador.compare(lista.get(j), actual) > 0) {

				// El elemento mayor se corre una posición a la derecha.
				lista.set(j + 1, lista.get(j));
				j--;
			}

			// Al terminar el while, el hueco quedó en j + 1, ahí va actual.
			lista.set(j + 1, actual);
		}
	}
}