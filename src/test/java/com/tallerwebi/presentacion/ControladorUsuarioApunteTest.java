package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorUsuarioApunteTest {

    private HttpServletRequest requestMock;
    private ServicioUsuario servicioUsuario;
    private ServicioUsuarioApunte servicioUsuarioApunte;
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private HttpSession sessionMock;
    private ControladorApunte controladorApunte;
    private BindingResult resultMock;
    private ServicioAdministrador servicioAdministrador;

    @BeforeEach
    public void init() {
    requestMock = mock(HttpServletRequest.class);
    sessionMock = mock(HttpSession.class);
    servicioApunte = mock(ServicioApunte.class);
    servicioUsuario = mock(ServicioUsuario.class);
    servicioUsuarioApunte = mock(ServicioUsuarioApunte.class);
    servicioUsuarioApunteResena = mock(ServicioUsuarioApunteResena.class);
    servicioAdministrador = mock(ServicioAdministrador.class);


    controladorApunte = new ControladorApunte(servicioApunte, servicioUsuarioApunte, servicioUsuarioApunteResena, servicioUsuario, servicioAdministrador);
        resultMock = mock(BindingResult.class);
    }



    @Test
    public void queAlComprarUnApunteDesdeLaVistaDeApuntesEnVentaLleveALaVistaDetalleDelApunte(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);
        UsuarioApunte usuarioApunte = new UsuarioApunte(comprador, apunte);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunte.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResena.obtenerLista(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunte.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResena.existeResena(comprador.getId(), apunte.getId())).thenReturn(true);

        when(servicioUsuarioApunte.obtenerVendedorPorApunte(apunte.getId())).thenReturn(vendedor);

        controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        when(servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte)).thenReturn(true);

        ModelAndView modelAndView = controladorApunte.comprarApunte(apunte.getId(), requestMock, sessionMock);

        verify(sessionMock, atLeastOnce()).getAttribute("usuario");

        verify(servicioApunte, atLeastOnce()).obtenerPorId(apunte.getId());

        verify(servicioUsuarioApunte, atLeastOnce()).comprarApunte(comprador, vendedor, apunte);

        assertEquals("apunte-detalle", modelAndView.getViewName());
    }
    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaVistaDeApuntesEnVenta() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Long apunteId = 1L;


        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunte.obtenerPorId(apunteId)).thenReturn(apunte);

        when(servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.comprarApunte(apunteId, requestMock, sessionMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("Error al realizar la compra", modelMap.get("error"));

        assertEquals("apuntesEnVenta", modelAndView.getViewName());
    }

    }
