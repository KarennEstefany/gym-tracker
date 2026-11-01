package com.gymtracker.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gymtracker.exceptions.PersistenciaException;

public class Persistencia {
	
	private Persistencia() {
		
		//esta es una clase de utilidad, por lo tanto, no se instancia
	}
	
	public static void guardar(DatosApp datos, String ruta) {
		
		File archivo = new File(ruta);
		File carpeta = archivo.getParentFile();
		if(carpeta != null) {
			
			carpeta.mkdirs(); //Si la carpeta no existe, la crea
		}
		try (FileOutputStream destino = new FileOutputStream(archivo); ObjectOutputStream salida = new ObjectOutputStream(destino)){
			
			salida.writeObject(datos);
			
		} catch(IOException e) {
			
			throw new PersistenciaException("No se pudo guardar: " + e.getMessage());
		} 
		
		
	}
	
	public static DatosApp cargar(String ruta) {
		
		File archivo = new File(ruta);
		if(!archivo.exists()) {
			return new DatosApp(); //primera ejecución: no hay nada guardado 
		}
		
		try ( FileInputStream fuente =  new FileInputStream(archivo); ObjectInputStream entrada = new ObjectInputStream(fuente)){
			return (DatosApp) entrada.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new PersistenciaException("No se pudo cargar el archivo" + e.getMessage());
		} 
	}
	
	

}
