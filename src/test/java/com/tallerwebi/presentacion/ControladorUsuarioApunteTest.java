package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorUsuarioApunteTest {

    private HttpServletRequest requestMock;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private HttpSession sessionMock;
    private ControladorApunte controladorApunte;

    @BeforeEach
    public void init() {
    requestMock = mock(HttpServletRequest.class);
    servicioApunte = mock(ServicioApunte.class);
    servicioUsuario = mock(ServicioUsuario.class);
    servicioUsuarioApunte = mock(ServicioUsuarioApunte.class);
    servicioUsuarioApunteResena = mock(ServicioUsuarioApunteResena.class);
    controladorApunte = new ControladorApunte(servicioApunte, servicioUsuarioApunte, servicioUsuarioApunteResena, servicioUsuario);
    sessionMock = mock(HttpSession.class);
    }


    @Test
    public void queAlComprarUnApunteCorrectamenteAparezcaMensajeEnLaVista(){
            Usuario comprador = new Usuario();
            Usuario vendedor = new Usuario();
            Apunte apunte = new Apunte();


            when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

            when(servicioApunte.obtenerPorId(apunte.getId())).thenReturn(apunte);

            when(servicioUsuarioApunte.obtenerVendedorPorApunte(vendedor.getId())).thenReturn(vendedor);

            when(servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte)).thenReturn(true);

            ModelAndView modelAndView = controladorApunte.comprarApunte(apunte.getId(), sessionMock);

            ModelMap modelMap = modelAndView.getModelMap();
            assertEquals("Compra exitosa", modelMap.get("mensaje"));

            assertEquals("apuntesEnVenta", modelAndView.getViewName());
        }
    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaVista() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Long apunteId = 1L;

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunte.obtenerPorId(apunteId)).thenReturn(apunte);

        when(servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.comprarApunte(apunteId, sessionMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("Error al realizar la compra", modelMap.get("error"));

        assertEquals("apuntesEnVenta", modelAndView.getViewName());
    }

    }
