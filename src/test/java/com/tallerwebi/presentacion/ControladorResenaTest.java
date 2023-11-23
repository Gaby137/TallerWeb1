package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;

import static junit.framework.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ControladorResenaTest {

    private HttpServletRequest requestMock;
    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private ControladorResena controladorResena;
    private HttpSession sessionMock;
    private BindingResult resultMock;
    private RedirectAttributes redirectAttributesMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        servicioResena = mock(ServicioResena.class);
        servicioUsuario = mock(ServicioUsuario.class);
        servicioApunte = mock(ServicioApunte.class);
        servicioUsuarioApunte = mock(ServicioUsuarioApunte.class);
        servicioUsuarioApunteResena = mock(ServicioUsuarioApunteResena.class);
        sessionMock = mock(HttpSession.class);
        resultMock = mock(BindingResult.class);
        controladorResena = new ControladorResena(servicioResena, servicioUsuario, servicioApunte, servicioUsuarioApunte, servicioUsuarioApunteResena, sessionMock);
        redirectAttributesMock = mock(RedirectAttributes.class);

    }
    
    @Test
    void borrarResenaDeberiaLlamarMetodoBorrarDelServicioYRedireccionarAVistaApunteDetalle() {
        // Preparación
        Usuario usuario = new Usuario(1L);
        Apunte apunte = new Apunte(1L);
        Resena resena = new Resena();
        resena.setId(1L);

        // Configuración del servicioResena para evitar excepciones
        doNothing().when(servicioResena).borrar(1L);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(1L);
        when(servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuarioYApunte(usuario.getId(), 1L)).thenReturn(resena);

        // Ejecución
        ModelAndView modelAndView = controladorResena.borrar(1L, sessionMock, redirectAttributesMock);

        // Verificación de que el servicioResena.borrar se llamó una vez con el idResenaABorrar
        verify(servicioResena, times(1)).borrar(eq(1L));

        // Verificación de la vista y modelo
        assertEquals("redirect:/detalleApunte/1", modelAndView.getViewName());
    }
    @Test
    void queAlEnviarFormularioDeResenaConParametrosCorrectosSeGuardeYDevuelvaStatusOK() {
        Long APUNTE_ID = 1L;
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);


        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);

        ResponseEntity response = controladorResena.guardarResena("Parametro de descripcion correcto", "5");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void queAlEnviarFormularioDeResenaSinDescripcionDevuelvaMensajeYStatusCorrecto() {
        Long APUNTE_ID = 1L;
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);

        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);

        ResponseEntity response = controladorResena.guardarResena("", "5");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campo Comentario no puede estar vacio", response.getBody());

    }

    @Test
    void queAlEnviarFormularioDeResenaSinValoracionDevuelvaMensajeYStatusCorrecto() {
        Long APUNTE_ID = 1L;
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);

        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);

        ResponseEntity response = controladorResena.guardarResena("Deslalal", "");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campo Valoración no puede estar vacio", response.getBody());
    }

    @Test
    void queAlEnviarFormularioDeResenaConFormatoDeValoracionIncorrectoDevuelvaMensajeYStatusCorrecto() {
        Long APUNTE_ID = 1L;
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);

        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);

        ResponseEntity response = controladorResena.guardarResena("Deslalal", "t7t8");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campo Valoración debe ser un número", response.getBody());

    }

    @Test
    void queAlEnviarFormularioDeResenaConValorDeValoracionIncorrectoDevuelvaMensajeYStatusCorrecto() {
        Long APUNTE_ID = 1L;
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);

        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);

        ResponseEntity response = controladorResena.guardarResena("Deslalal", "0");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campo Valoración tiene que ser un numero entre 1 y 5", response.getBody());

    }

    @Test
    public void queNoDejeEliminarResenaSiNoEsTuya(){
        Usuario usuario = new Usuario(1L);
        Apunte apunte = new Apunte(1L);
        Resena resena = new Resena();
        resena.setId(1L);


        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(1L);
        when(servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuarioYApunte(1L, 1L)).thenReturn(null);

        controladorResena.borrar(1L, sessionMock, redirectAttributesMock);

        verify(servicioResena, never()).borrar(1L);
    }

    @Test
    public void queDejeEliminarResenaSiEsTuya(){
        Usuario usuario = new Usuario(1L);
        Apunte apunte = new Apunte(1L);
        Resena resena = new Resena();
        resena.setId(1L);


        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(1L);
        when(servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuarioYApunte(1L, 1L)).thenReturn(resena);

        controladorResena.borrar(1L, sessionMock, redirectAttributesMock);

        verify(servicioResena, times(1)).borrar(1L);
    }

}






