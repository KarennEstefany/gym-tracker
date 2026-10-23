package com.gymtracker.algorithms;

import java.util.Comparator;

import com.gymtracker.model.Ejercicio;

public class ComparadoresEjercicio {
	private static final Comparator<Ejercicio> porNombre = (a, b) -> a.getNombre().compareTo(b.getNombre());
	private static final Comparator<Ejercicio> porGrupoMuscular = (a, b) -> a.getGrupoMuscular().compareTo(b.getGrupoMuscular());
}
