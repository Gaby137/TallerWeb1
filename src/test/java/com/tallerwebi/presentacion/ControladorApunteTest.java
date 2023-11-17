package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

public class ControladorApunteTest {
    private HttpServletRequest requestMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioApunte servicioApunteMock;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResenaMock;
    private HttpSession sessionMock;
    private ControladorApunte controladorApunte;
    private BindingResult resultMock;
    private RedirectAttributes redirectAttributesMock;
    private ServicioAdministrador servicioAdministrador;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioApunteMock = mock(ServicioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioUsuarioApunteMock = mock(ServicioUsuarioApunte.class);
        servicioUsuarioApunteResenaMock = mock(ServicioUsuarioApunteResena.class);
        servicioAdministrador = mock(ServicioAdministrador.class);

        controladorApunte = new ControladorApunte(servicioApunteMock, servicioUsuarioApunteMock, servicioUsuarioApunteResenaMock, servicioUsuarioMock, servicioAdministrador);
        resultMock = mock(BindingResult.class);
        redirectAttributesMock = mock(RedirectAttributes.class);
    }

    @Test
    public void testPublicarExitoso() {
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);

        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, resultMock, sessionMock);

        assertEquals("redirect:/misApuntes", modelAndView.getViewName());
    }

    @Test
    public void testPublicarFallo() {
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);

        when(resultMock.hasErrors()).thenReturn(true);
        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);

        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, resultMock, sessionMock);

        assertEquals("altaApunte", modelAndView.getViewName());
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

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(true);

        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunte.getId())).thenReturn(vendedor);

        controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(true);

        ModelAndView modelAndView = controladorApunte.comprarApunte(apunte.getId(), requestMock, sessionMock);

        verify(sessionMock, atLeastOnce()).getAttribute("usuario");

        verify(servicioApunteMock, atLeastOnce()).obtenerPorId(apunte.getId());

        verify(servicioUsuarioApunteMock, atLeastOnce()).comprarApunte(comprador, vendedor, apunte);

        assertEquals("apunte-detalle", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaVistaDeApuntesEnVenta() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Long apunteId = 1L;


        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.comprarApunte(apunteId, requestMock, sessionMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("apuntesEnVenta", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteDesdeLaVistaDelPerfilDelVendedorLleveALaVistaDetalleDelApunte(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);
        UsuarioApunte usuarioApunte = new UsuarioApunte(comprador, apunte);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(true);

        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunte.getId())).thenReturn(vendedor);

        controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(true);

        ModelAndView modelAndView = controladorApunte.comprarApuntePorPerfil(apunte.getId(), requestMock, sessionMock);

        verify(sessionMock, atLeastOnce()).getAttribute("usuario");

        verify(servicioApunteMock, atLeastOnce()).obtenerPorId(apunte.getId());

        verify(servicioUsuarioApunteMock, atLeastOnce()).comprarApunte(comprador, vendedor, apunte);

        assertEquals("apunte-detalle", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaVistaDelPerfilDelVendedor() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Long apunteId = 1L;


        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.comprarApuntePorPerfil(apunteId, requestMock, sessionMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("Error al realizar la compra", modelMap.get("error"));

        assertEquals("perfilUsuario", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteDesdeElHomeLleveALaVistaDetalleDelApunte(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);
        UsuarioApunte usuarioApunte = new UsuarioApunte(comprador, apunte);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(true);

        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunte.getId())).thenReturn(vendedor);

        controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(true);

        ModelAndView modelAndView = controladorApunte.comprarApunteEnElHome(apunte.getId(), requestMock, sessionMock, redirectAttributesMock);

        verify(sessionMock, atLeastOnce()).getAttribute("usuario");

        verify(servicioApunteMock, atLeastOnce()).obtenerPorId(apunte.getId());

        verify(servicioUsuarioApunteMock, atLeastOnce()).comprarApunte(comprador, vendedor, apunte);

        assertEquals("apunte-detalle", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaVistaDelHome() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Long apunteId = 1L;


        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.comprarApunteEnElHome(apunteId, requestMock, sessionMock, redirectAttributesMock);

        assertEquals("redirect:/home", modelAndView.getViewName());
    }

    @Test
    public void queAlTenerUnaResenaHechaElBooleanDeTrue(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(true);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean hayResena = (Boolean) modelAndView.getModelMap().get("hayResena");

        assertTrue(hayResena);
    }

    @Test
    public void queAlNoTenerUnaResenaHechaElBooleanDeFalse(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean hayResena = (Boolean) modelAndView.getModelMap().get("hayResena");

        assertFalse(hayResena);
    }

    @Test
    public void queElTipoDeAccesoDeFalseSiElTipoDeAccesoEsEditar(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.EDITAR);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean tipoDeAcceso = (Boolean) modelAndView.getModelMap().get("tipoDeAcceso");

        assertFalse(tipoDeAcceso);

    }

    @Test
    public void queElTipoDeAccesoDeTrueSiElTipoDeAccesoEsLeer(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean tipoDeAcceso = (Boolean) modelAndView.getModelMap().get("tipoDeAcceso");

        assertTrue(tipoDeAcceso);

    }
    @Test
    public void queElPdfCompradoDeTrueSiElTipoDeAccesoEsLeer(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.LEER);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean pdfComprado = (Boolean) modelAndView.getModelMap().get("pdfComprado");

        assertTrue(pdfComprado);

    }

    @Test
    public void queElPdfCompradoDeTrueSiElTipoDeAccesoEsEditar(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(TipoDeAcceso.EDITAR);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean pdfComprado = (Boolean) modelAndView.getModelMap().get("pdfComprado");

        assertTrue(pdfComprado);

    }

    @Test
    public void queElPdfCompradoDeFalseSiElTipoDeAccesoEsNull(){
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();
        apunte.setId(1L);
        comprador.setId(1L);
        vendedor.setId(2L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioUsuarioApunteResenaMock.obtenerListaDeResenasPorIdApunte(apunte.getId())).thenReturn(List.of(resena));

        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(comprador.getId(), apunte.getId())).thenReturn(null);

        when(servicioUsuarioApunteResenaMock.existeResena(comprador.getId(), apunte.getId())).thenReturn(false);

        ModelAndView modelAndView = controladorApunte.getDetalleApunteConListadoDeSusResenas(apunte.getId(), requestMock, sessionMock);

        Boolean pdfComprado = (Boolean) modelAndView.getModelMap().get("pdfComprado");

        assertFalse(pdfComprado);

    }




}
