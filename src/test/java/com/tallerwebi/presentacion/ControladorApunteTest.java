package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioApunte;

import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResenaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        String pathArchivo = "archivo.pdf";
        String nombre = "Apunte 1";
        String descripcion = "Descripcion del Apunte";
        new DatosApunte(pathArchivo, nombre, descripcion);

        apunteMock = mock(Apunte.class);
        when(apunteMock.getId()).thenReturn(1L);
        when(apunteMock.getNombre()).thenReturn("Apunte 1");

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioApunteMock = mock(ServicioApunte.class);
        controladorApunte = new ControladorApunte(servicioApunteMock, servicioUsuarioApunteMock, servicioUsuarioApunteResenaMock);
    }

    @Test
    public void testPublicarExitoso() {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        when(servicioApunteMock.registrar(datosApunteMock, usuarioMock)).thenReturn(true);

        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, sessionMock);

        // Verificación
        assertEquals("altaApunte", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
    }

    @Test
    public void testPublicarFallo() {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        when(servicioApunteMock.registrar(datosApunteMock, usuarioMock)).thenReturn(false);

        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, sessionMock);

        // Verificación
        assertEquals("altaApunte", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Por favor complete todos los campos", modelAndView.getModel().get("error"));
    }

   /* @Test
    void testDeberiaDevolverDetalleDeApunteConSuListadoDeResenas() {
        // preparación
        Long APUNTE_ID = 1L;

        // Apunte apunteMock = mock(Apunte.class);
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuarioMock = mock(Usuario.class);
        Resena resenaMock1 = mock(Resena.class);
        Resena resenaMock2 = mock(Resena.class);
        List<UsuarioApunteResena>  usuarioApunteResenaListMock = new ArrayList<>();

        UsuarioApunteResena usuarioApunteResenaMock1 = new UsuarioApunteResena(usuarioMock, resenaMock1, apunteMock);
        UsuarioApunteResena usuarioApunteResenaMock2 = new UsuarioApunteResena(usuarioMock, resenaMock2, apunteMock);

        usuarioApunteResenaListMock.add(usuarioApunteResenaMock1);
        usuarioApunteResenaListMock.add(usuarioApunteResenaMock2);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioApunteMock.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);
        when(servicioApunteMock.getListadoDeResenasConSusUsuariosPorIdApunte(APUNTE_ID)).thenReturn(usuarioApunteResenaListMock);

        // ejecución
        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(APUNTE_ID, requestMock);

        // validación
        Apunte apunteEnModelo = (Apunte) modelAndView.getModelMap().get("apunte");
        List<UsuarioApunteResena> resenasEnModelo = (List<UsuarioApunteResena>) modelAndView.getModelMap().get("usuarioApunteResenas");

        verify(servicioApunteMock).getListadoDeResenasConSusUsuariosPorIdApunte(APUNTE_ID);
        assertEquals("detalleApunte", modelAndView.getViewName());
        assertFalse(apunteEnModelo == null);
        assertEquals(apunteMock, apunteEnModelo);
        assertFalse(resenasEnModelo == null);
        assertEquals(2, resenasEnModelo.size());
    }*/

}
