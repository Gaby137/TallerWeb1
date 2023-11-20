package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.excepcion.ArchivoInexistenteException;
import com.tallerwebi.dominio.servicio.ServicioApunte;

import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControladorApunteTest {
    private ControladorApunte controladorApunte;
    private Apunte apunteMock;
    private ServicioApunte servicioApunteMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResenaMock;
    private ControladorLogin controladorLogin;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private BindingResult resultMock;
    private MockMultipartFile pdf;

    @BeforeEach
    public void init() {
        apunteMock = mock(Apunte.class);
        when(apunteMock.getId()).thenReturn(1L);
        when(apunteMock.getNombre()).thenReturn("Apunte 1");
        pdf = mock(MockMultipartFile.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioApunteResenaMock = mock(ServicioUsuarioApunteResena.class);
        servicioApunteMock = mock(ServicioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        controladorLogin = mock(ControladorLogin.class);

        controladorApunte = new ControladorApunte(servicioApunteMock, servicioUsuarioApunteMock, servicioUsuarioApunteResenaMock, servicioUsuarioMock, controladorLogin);
        resultMock = mock(BindingResult.class);
    }

    @Test
    public void testPublicarExitoso() throws ArchivoInexistenteException {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);

        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, resultMock, sessionMock);

        // Verificación
        assertEquals("redirect:/misApuntes", modelAndView.getViewName());
    }

    @Test
    public void testPublicarFallo() throws ArchivoInexistenteException {
        // Configuración de objetos simulados

        DatosApunte datosApunteMock = mock(DatosApunte.class);
        datosApunteMock.setNombre(null);
        Usuario usuarioMock = mock(Usuario.class);
        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        when(pdf.isEmpty()).thenReturn(true);

        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);
        
        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, resultMock, sessionMock);
        
        assertEquals("altaApunte", modelAndView.getViewName());
    }



}
