package com.gymtracker.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;

/**
 * Pruebas unitarias de {@link OrdenadorInsercion}.
 * <p>
 * Cubre listas según su estado inicial (ordenada, inversa, casi ordenada),
 * casos límite (vacía, un elemento, repetidos), el uso de distintos
 * comparadores y la estabilidad del algoritmo.
 */

class OrdenarPorInsercionTest{

	
	// Casos que atrapan los errores de índice 


	//Verifica que una lista completamente desordenada  quede ordenada de menor a mayor.
	@Test
	void ordenar_listaDesordenada_quedaAscendente() {
		
		List<Integer> lista = new ArrayList<>(List.of(3,2,1));
					
		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());
		
		assertEquals(List.of(1,2,3), lista);
	}
	
	
	// Comprueba que si el número más pequeño está al final de la lista, 
	//el algoritmo lo mueva correctamente hasta la primera posición.
	
	@Test
	void ordenar_menorAlFinal_quedaEnLaPrimeraPosicion() {
		
		List<Integer> lista = new ArrayList<>(List.of(2,3,1));
		
		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());
		
		assertEquals(List.of(1,2,3), lista);
	}
	
	//Valida que si hay un elemento fuera de lugar casi al final,
	//el algoritmo lo coloque en su posición correcta.
	@Test
	void ordenar_ultimoElementoTambienSeUbica(){
		
		List<Integer> lista = new ArrayList<>(List.of(1,2,4,3));
		
		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());
		
		assertEquals(List.of(1,2,3,4), lista);
	
	}

	/**
	 * Verifica que una lista que ya viene en orden ascendente no cambie.
	 * <p>
	 * Es el mejor caso del algoritmo: el while nunca entra y cada elemento
	 * se compara una sola vez, lo que da complejidad O(n).
	 */

	@Test
	void ordenar_listaYaOrdenada_quedaIgual() {
		List<Integer> lista = new ArrayList<>(List.of(1, 2, 3, 4));

		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());

		assertEquals(List.of(1, 2, 3, 4), lista);
	}
	
	
	/**
	 * Verifica que una lista con un solo par de elementos fuera de lugar
	 * quede ordenada.
	 * <p>
	 * Representa el caso típico del proyecto (sesiones y mediciones que se
	 * agregan en orden cronológico), donde el algoritmo se comporta casi en O(n).
	 */
	
	@Test
	void ordenar_listaCasiOrdenada_quedaAscendente() {
		List<Integer> lista = new ArrayList<>(List.of(1, 2, 4, 3, 5));

		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());

		assertEquals(List.of(1, 2, 3, 4, 5), lista);
	}
	
	/**
	 * Verifica que una lista en orden descendente quede en orden ascendente.
	 * <p>
	 * Es el peor caso del algoritmo: cada elemento se desplaza hasta el inicio
	 * de la lista, lo que da complejidad O(n^2).
	 */
	
	@Test
	void ordenar_listaEnOrdenInverso_quedaAscendente() {
		List<Integer> lista = new ArrayList<>(List.of(5, 4, 3, 2, 1));

		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());

		assertEquals(List.of(1, 2, 3, 4, 5), lista);
	}



		
	//Casos límite 
	
	//Si la lista solo tiene un número, el algoritmo no debe fallar ni alterarla.
	@Test
	void ordenar_unSoloElemento_quedaIgual() {
		
		List<Integer> lista = new ArrayList<>(List.of(7));
		
		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());
		
		assertEquals(List.of(7), lista);
	}
	//Si se le pasa una lista vacía, el algoritmo la maneja sin errores y confirma que sigue vacía.
	@Test
	void ordenar_listaVacia_noLanzaErrorYSigueVacia(){
		List<Integer> lista = new ArrayList<>();
		
		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());
		assertTrue(lista.isEmpty());
		
	}
	
	
	@Test
	void ordenar_elementosRepetidos_quedanJuntosSinPerderNinguno() {
		List<Integer> lista = new ArrayList<>(List.of(3, 1, 3, 2, 1));

		OrdenarPorInsercion.ordenar(lista, Comparator.naturalOrder());

		assertEquals(List.of(1, 1, 2, 3, 3), lista);
	}

	
	// Comparator y genericidad

	
	
	//Valida que el algoritmo no solo sirve para números, sino que también funciona correctamente
		//ordenando otro tipo de datos, por ejemplo, String 
	
	@Test
	void ordenar_esGenerico_funcionaConOtrosTipos() {
		
		List<String> lista = new ArrayList<>(List.of("manzana", "piña", "uva", "fresa"));
		
		OrdenarPorInsercion.ordenar(lista,Comparator.naturalOrder());
		
		assertEquals(List.of("fresa", "manzana", "piña", "uva"), lista);
		
	}
	
	@Test
	void ordenar_conComparadorInverso_quedaDescendente() {
		List<Integer> lista = new ArrayList<>(List.of(2, 5, 1, 4));

		OrdenarPorInsercion.ordenar(lista, Comparator.reverseOrder());

		assertEquals(List.of(5, 4, 2, 1), lista);
	}
	
	


	//Casos del proyecto 
	
	//Verifica que el algoritmo sea capaz de ordenar objetos complejos, en este caso usuarios, 
	//basándose en un atributo específico, en este caso, su número de documento de forma ascendente
	@Test 
	void ordenar_UsuarioPorDocumento() {
		
		Usuario ana = new Usuario(1, "300", "Ana", 35, 1.65, Objetivo.HIPERTROFIA);
		Usuario maria = new Usuario(2, "100", "María", 25, 1.70, Objetivo.FUERZA);
		Usuario marta = new Usuario(3, "200", "Marta", 25, 1.67, Objetivo.PERDIDA_DE_GRASA);
		
		List<Usuario> lista = new ArrayList<>(List.of(ana, maria, marta));
		OrdenarPorInsercion.ordenar(lista, Comparator.comparing(Usuario::getDocumento));
		
		assertEquals(List.of(maria,marta,ana), lista);
	}
	
	//Comprueba que al ordenar por edad, si dos usuarios tienen la misma edad (25 años), 
	//el algoritmo respeta y mantiene su orden original relativo, esto verifica la estabilidad del algoritmo 
	
	@Test 
	void ordenar_usuariosPorEdad_conservaOrdenDeEdadesIguales() {
		
		Usuario ana = new Usuario(1, "300", "Ana", 25, 1.65, Objetivo.HIPERTROFIA);
		Usuario maria = new Usuario(2, "100", "María", 25, 1.70, Objetivo.FUERZA);
		Usuario marta = new Usuario(3, "200", "Marta", 35, 1.67, Objetivo.PERDIDA_DE_GRASA);
		
		List<Usuario> lista = new ArrayList<>(List.of(ana, maria, marta));
		OrdenarPorInsercion.ordenar(lista, Comparator.comparingInt(Usuario::getEdad));
		
		assertEquals(List.of(ana,maria,marta), lista);
	}
	

	

}
