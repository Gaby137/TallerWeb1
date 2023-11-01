package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.servicio.ServicioLogin;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import static org.hamcrest.Matchers.hasSize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosRegistro registroMock;
	private BindingResult resultMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;
    private ServicioUsuario servicioUsuarioMock;
	private ServicioUsuarioApunte servicioUsuarioApunte;
	private ServicioUsuarioApunteResena servicioUsuarioApunteResena;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		registroMock = mock(DatosRegistro.class);
		resultMock = mock(BindingResult.class);
		mock(MultipartFile.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
		servicioUsuarioApunte = mock(ServicioUsuarioApunte.class);
		servicioUsuarioApunteResena = mock(ServicioUsuarioApunteResena.class);
		controladorLogin = new ControladorLogin(servicioLoginMock, servicioUsuarioApunteResena, servicioUsuarioApunte, servicioUsuarioMock);

	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}

	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome(){
		// preparacion
		Usuario usuarioEncontradoMock = mock(Usuario.class);
		when(usuarioEncontradoMock.getRol()).thenReturn(Rol.valueOf("ADMIN"));

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente, IOException {

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(registroMock, resultMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(registroMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, IOException {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(registroMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(registroMock, resultMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente, IOException {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(registroMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(registroMock, resultMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

	@Test
	public void homeDeberiaMostrarElApunteConBuenaCalificacionSolamente() {
		Usuario usuarioMock = mock(Usuario.class);
		when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);

		List<Apunte> apuntes = new ArrayList<>();
		Apunte apunte1 = new Apunte();
		apunte1.setId(1L);
		Apunte apunte2 = new Apunte();
		apunte2.setId(2L);
		apuntes.add(apunte1);
		apuntes.add(apunte2);

		when(servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuarioMock.getId())).thenReturn(apuntes);

		when(servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(1L)).thenReturn(4.5);
		when(servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(2L)).thenReturn(2.0);

		ModelAndView modelAndView = controladorLogin.home(sessionMock);

		assertThat(modelAndView.getViewName(), equalToIgnoringCase("home"));
		List<Apunte> apunte = (List<Apunte>) modelAndView.getModel().get("apuntes");
		assertThat(apunte, hasSize(1));
	}
}
