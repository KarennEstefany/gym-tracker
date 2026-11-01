package com.gymtracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.gymtracker.exceptions.ContrasenaInvalidaException;
import com.gymtracker.exceptions.DocumentoInvalidoException;
import com.gymtracker.exceptions.EdadEstaturaInvalidaException;
import com.gymtracker.exceptions.NombreNullException;
import com.gymtracker.exceptions.ObjetivoInvalidoException;
import com.gymtracker.exceptions.ObjetoNuloException;
import com.gymtracker.exceptions.UsuarioDuplicadoException;
import com.gymtracker.exceptions.UsuarioNoEncontradoException;
import com.gymtracker.model.Objetivo;
import com.gymtracker.model.Usuario;

public class UsuarioServiceTest {

	private static final String CLAVE = "Clave1234";
	private UsuarioService usuarioService;
	
	
	
	
	@BeforeEach
	void setUp() {
		usuarioService= new UsuarioService();
		
	}
	
	private Usuario registrarUsuario(String documento) {
		return usuarioService.registrarUsuario(documento, "Maria", 18, 1.7, Objetivo.RESISTENCIA, CLAVE);
	}
	
	//////////////////////////Prueba de estado inicial////////////////////
	/// 
	@Test
	void serviceNuevo() {
		assertEquals(0, usuarioService.darCantidad());
		assertFalse(usuarioService.existe("100"));
	}
	////////////////////////Pruebas de registrar usuario//////////////////////
	@Test
	void devuelveElUsuarioConSusDatos() {
		Usuario usuario = usuarioService.registrarUsuario("200", "Maria", 18, 1.7, Objetivo.RESISTENCIA, CLAVE);
		assertEquals("200", usuario.getDocumento());
		assertEquals("Maria", usuario.getNombre());
		assertEquals(18, usuario.getEdad());
		assertEquals(1.7, usuario.getEstatura(), 0.001);
		assertEquals(Objetivo.RESISTENCIA, usuario.getObjetivo());
		assertEquals(1, usuarioService.darCantidad());
		
	}
	
	@Test
	void idSecuelciales() {
		assertEquals(1, registrarUsuario("100").getId());
		assertEquals(2, registrarUsuario("200").getId());
		assertEquals(3, registrarUsuario("300").getId());
		assertEquals(4, registrarUsuario("400").getId());
	}
	@Test
	void documentoRepetido() {
		registrarUsuario("100");
		
		assertThrows(UsuarioDuplicadoException.class, ()-> registrarUsuario("100"));
		assertEquals(1, usuarioService.darCantidad());
	}
	@Test
	void registrarDuplicadoNoReemplaza() {
		Usuario original = usuarioService.registrarUsuario("200", "Maria", 18, 1.7, Objetivo.RESISTENCIA, CLAVE);
		
		assertThrows(UsuarioDuplicadoException.class, ()-> usuarioService.registrarUsuario("200", "Ana", 29, 1.8, Objetivo.HIPERTROFIA, "Clave32221"));
		assertSame(original, usuarioService.buscarPorDocumento("200"));
		assertEquals("Maria", usuarioService.buscarPorDocumento("200").getNombre());
	}
	@Test
	void noSeConsumeIdSiFalla() {
		registrarUsuario("100");
		assertThrows(UsuarioDuplicadoException.class, ()-> registrarUsuario("100"));
		
		Usuario nuevo = registrarUsuario("200");
		assertEquals(2, nuevo.getId());
		
	}
	@Test
	void datosInvalidosNoRegistra() {
		assertThrows(EdadEstaturaInvalidaException.class, ()-> usuarioService.registrarUsuario("100", "Sofia", -2, 1.7, Objetivo.PERDIDA_DE_GRASA, CLAVE));
		assertFalse(usuarioService.existe("100"));
		assertEquals(0, usuarioService.darCantidad());
		Usuario uvalido = registrarUsuario("100");
		assertEquals(1, uvalido.getId());
	}
	
	@Test
	void registrarNombreNulo() {
		assertThrows(NombreNullException.class, ()-> usuarioService.registrarUsuario("100", null, 2, 1.7, Objetivo.PERDIDA_DE_GRASA, CLAVE));
		assertFalse(usuarioService.existe("100"));
	}
	@Test
	void registrarObjetivoNulo() {
		assertThrows(ObjetivoInvalidoException.class, ()-> usuarioService.registrarUsuario("100", "Juan",2, 1.7, null, CLAVE));
		assertFalse(usuarioService.existe("100"));
	}
	
	@Test
	void registrarEstaturaCero() {
		assertThrows(EdadEstaturaInvalidaException.class, ()-> usuarioService.registrarUsuario("100", "Juan",2, 0, Objetivo.FUERZA, CLAVE));
		assertFalse(usuarioService.existe("100"));
	}
	@Test
	void registrarConDocumentoVacio() {
		//Verificar en usuario
		assertThrows(DocumentoInvalidoException.class, ()-> usuarioService.registrarUsuario("", "Juan",2, 1.7, Objetivo.HIPERTROFIA, CLAVE));
		assertEquals(0, usuarioService.darCantidad());
		
	}
	@Test
	void registrarConContrasenaVacio() {
		//Verificar en usuario
		assertThrows(ContrasenaInvalidaException.class, ()-> usuarioService.registrarUsuario("100", "Juan",2, 1.7, Objetivo.RESISTENCIA, null));
		assertFalse(usuarioService.existe("100"));
		
	}
	//////////////////Pruebas de busqueda/////////////////////////
	@Test
	void buscarPorDocumento() {
		Usuario usuario = registrarUsuario("200");
		assertSame(usuario, usuarioService.buscarPorDocumento("200"));
	}
	@Test
	void buscarDocumentoInexistente() {
		registrarUsuario("200");
		assertThrows(UsuarioNoEncontradoException.class, ()-> usuarioService.buscarPorDocumento("300"));
	}
	@Test
	void buscarEnServiceVacio() {
		assertThrows(UsuarioNoEncontradoException.class, ()-> usuarioService.buscarPorDocumento("300"));
	}
	@Test
	void buscarDocumentoNulo() {
		assertThrows(ObjetoNuloException.class, ()-> usuarioService.buscarPorDocumento(null));
	}
	@Test
	void distingueEntreVariosUsuarios() {
		Usuario u1 =usuarioService.registrarUsuario("100", "Juan",2, 1.7, Objetivo.RESISTENCIA, CLAVE);
		Usuario u2 =usuarioService.registrarUsuario("200", "Ana",10, 1.8, Objetivo.FUERZA, CLAVE);
		Usuario u3 =usuarioService.registrarUsuario("300", "Michael",20, 1.74, Objetivo.RESISTENCIA, CLAVE);
		
		assertSame(u1, usuarioService.buscarPorDocumento("100"));
		assertSame(u2, usuarioService.buscarPorDocumento("200"));
		assertSame(u3, usuarioService.buscarPorDocumento("300"));
		assertNotSame(usuarioService.buscarPorDocumento("100"), usuarioService.buscarPorDocumento("200"));
		
		
	}
	///////////////////Pruebas de existe//////////////////////
	@Test
	void documentosRegistrados() {
		registrarUsuario("100");
		
		assertTrue(usuarioService.existe("100"));
		assertFalse(usuarioService.existe("200"));
		
	}
	@Test
	void documentoNulo() {
		assertThrows(ObjetoNuloException.class, () -> usuarioService.existe(null));
	}
	
	///////////////////////Pruebas de ingresar con las credenciales/////////
	@Test
	void credencialesCorrectas() {
		Usuario usuario = registrarUsuario("100");
		assertSame(usuario, usuarioService.ingresar("100", CLAVE));
	}
	@Test
	void contrasenaIncorrecta() {
		registrarUsuario("100");
		assertThrows(ContrasenaInvalidaException.class, () -> usuarioService.ingresar("100", "Clavemaka"));
	}
	@Test
	void documentoInexistente() {
		registrarUsuario("100");
		assertThrows(ContrasenaInvalidaException.class, () -> usuarioService.ingresar("999", CLAVE));
	}
	@Test
	void noRevelaElDocumento() {
		registrarUsuario("100");
		ContrasenaInvalidaException porClave = assertThrows(ContrasenaInvalidaException.class,
				() -> usuarioService.ingresar("100", "incorrecta"));
		ContrasenaInvalidaException porDocumento = assertThrows(ContrasenaInvalidaException.class,
				() -> usuarioService.ingresar("999", CLAVE));
 
		assertEquals(porClave.getMessage(), porDocumento.getMessage());
	}
	@Test
	void contrasenaConMayusculasMinusculas() {
		registrarUsuario("100");
		assertThrows(ContrasenaInvalidaException.class, () -> usuarioService.ingresar("100", "CLAVE123"));
	}
	@Test
	void contrasenaDeOtroUsuario() {
		usuarioService.registrarUsuario("100", "Michael",20, 1.74, Objetivo.RESISTENCIA, "Clave123Michael");
		usuarioService.registrarUsuario("200", "Isabel",19, 1.64, Objetivo.FUERZA, "Clave123Isabel");
	
		assertThrows(ContrasenaInvalidaException.class, () -> usuarioService.ingresar("100", "Clave123Isabel"));
		assertEquals("Michael", usuarioService.ingresar("100", "Clave123Michael").getNombre());
	}
	@Test
	void ingresarDocumentoNulo() {
		assertThrows(ObjetoNuloException.class, ()-> usuarioService.ingresar(null, CLAVE));
	}
	////////////////////Rehashing a traves del service (muchos usuarios)//////////////////////
	@Test
	void registrarMuchosUsuarios() {
		for (int i=0; i <100;i++) {
			registrarUsuario("DOC" +i);
		}
		
		assertEquals(100, usuarioService.darCantidad());
		for(int i =0; i<100; i++) {
			assertEquals(i+1, usuarioService.buscarPorDocumento("DOC" +i).getId());
			
		}
		assertSame(usuarioService.buscarPorDocumento("DOC50"), usuarioService.ingresar("DOC50", CLAVE));
	}
	
	

	
	@Nested 
	class ConListaInicial {
		
		
		private UsuarioService usuarioService;
		private List<Usuario> listaInicial;
		
		@BeforeEach
		void setUp2() {
			usuarioService= new UsuarioService();
			
			listaInicial = new ArrayList<>();
			listaInicial.add(new Usuario(5, "123", "Ana", 23,1.65, Objetivo.HIPERTROFIA, "Clave123"));
			usuarioService = new UsuarioService(listaInicial);
			
		}
		
	
	
		// Agrega
		@Test 
		void buscarPorDocumento_LoEncuentra() {
			
			Usuario u1 = new Usuario(1, "123", "Ana", 23, 1.65, Objetivo.HIPERTROFIA , "Clave123");
			
			
			List<Usuario> listaInicial = new ArrayList<>();
			listaInicial.add(u1);
			
			
			UsuarioService servicio = new UsuarioService(listaInicial);
			
			Usuario encontrado = servicio.buscarPorDocumento("123");
			assertNotNull(encontrado);
			assertEquals("Ana", encontrado.getNombre());
			
			
		}
		
		
		
		
		//Agrega
		@Test
		void constructor_reconstruyeElIndiceDeLaLista() {
			
			assertEquals("Ana", usuarioService.buscarPorDocumento("123").getNombre());
		}
		
		
		
		//Agrega
		@Test
		void registrarUsuario_siguienteIDElMasAltoMasUno() {
			
			Usuario nuevo = usuarioService.registrarUsuario("321", "Juan", 23, 1.75, Objetivo.FUERZA, "Clave123");
			
			assertEquals(6, nuevo.getId());
		}
		
		//Agrega
		@Test
		void registrarUsuario_loAgregaALaListaQueSeGuarda() {
			
			usuarioService.registrarUsuario("321", "Juan", 23, 1.75, Objetivo.FUERZA, "Clave123");
			
			assertEquals(2, listaInicial.size());
		}
		
		//Agrega
		
		@Test 
		void registrarUsuario_documentoRepetido_noAgregaNiGastaId() {
			assertThrows(UsuarioDuplicadoException.class, () -> usuarioService.registrarUsuario("123", "Otra", 30, 1.75,Objetivo.FUERZA , "Clave123"));
			assertEquals(1, listaInicial.size());
			
			Usuario nuevo = usuarioService.registrarUsuario("321", "Juan", 23, 1.75, Objetivo.FUERZA, "Clave123");
			
			assertEquals(6,nuevo.getId());
			
		}
		
		@Test
		void constructorSinParametros_empiezaVacioConIdUno() {
			
			UsuarioService vacio = new UsuarioService();
			Usuario primero = vacio.registrarUsuario("999", "Luis", 30, 1.80, Objetivo.FUERZA, "Clave123");
			assertEquals(1, primero.getId());
		}
		
	}
	
}
		
		
	
	
	


	


