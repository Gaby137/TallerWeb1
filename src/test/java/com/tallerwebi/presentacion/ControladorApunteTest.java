package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.excepcion.ArchivoInexistenteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.*;

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
    private Apunte apunteMock;
    private RedirectAttributes redirectAttributesMock;
    private ServicioAdministrador servicioAdministrador;
    private ControladorLogin controladorLogin;
    private MockMultipartFile pdf;


    @BeforeEach
    public void init() {
        apunteMock = mock(Apunte.class);
        when(apunteMock.getId()).thenReturn(1L);
        when(apunteMock.getNombre()).thenReturn("Apunte 1");
        pdf = mock(MockMultipartFile.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioApunteMock = mock(ServicioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioUsuarioApunteMock = mock(ServicioUsuarioApunte.class);
        servicioUsuarioApunteResenaMock = mock(ServicioUsuarioApunteResena.class);
        servicioAdministrador = mock(ServicioAdministrador.class);
        controladorLogin = mock(ControladorLogin.class);

        controladorApunte = new ControladorApunte(servicioApunteMock, servicioUsuarioApunteMock, servicioUsuarioApunteResenaMock, servicioUsuarioMock, servicioAdministrador, controladorLogin);
        resultMock = mock(BindingResult.class);
        redirectAttributesMock = mock(RedirectAttributes.class);
    }

    @Test
    public void testPublicarExitoso() throws ArchivoInexistenteException {

        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);

        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock, resultMock, sessionMock);

        assertEquals("redirect:/misApuntes", modelAndView.getViewName());
    }

    @Test
    public void testPublicarFallo() throws ArchivoInexistenteException {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);

        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        when(pdf.isEmpty()).thenReturn(true);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        doNothing().when(servicioUsuarioApunteResenaMock).registrarApunte(datosApunteMock, usuarioMock);
        
        // Ejecución de la prueba
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
        Usuario vendedor = new Usuario(1L);
        Apunte apunte = new Apunte(1L);

        when(servicioUsuarioMock.obtenerPorId(1L)).thenReturn(vendedor);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(1L)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(1L)).thenReturn(vendedor);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        controladorApunte.verPerfilUsuario(1L, sessionMock);

        ModelAndView modelAndView = controladorApunte.comprarApuntePorPerfil(1L, requestMock, sessionMock);

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
        Apunte apunte = new Apunte(1L);


        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(servicioApunteMock.obtenerPorId(1L)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        when(controladorLogin.home(sessionMock)).thenReturn(new ModelAndView("home"));

        ModelAndView modelAndView = controladorApunte.comprarApunteEnElHome(1L, requestMock, sessionMock, redirectAttributesMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("Error al realizar la compra", modelMap.get("error"));

        assertEquals("home", modelAndView.getViewName());
    }

    @Test
    public void queAlComprarUnApunteDesdeElApunteDetalleLleveALaVistaDetalleDelApunte(){
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

        ModelAndView modelAndView = controladorApunte.comprarApunteEnDetalleApunte(apunte.getId(), requestMock, sessionMock);

        verify(sessionMock, atLeastOnce()).getAttribute("usuario");

        verify(servicioApunteMock, atLeastOnce()).obtenerPorId(apunte.getId());

        verify(servicioUsuarioApunteMock, atLeastOnce()).comprarApunte(comprador, vendedor, apunte);

        assertEquals("apunte-detalle", modelAndView.getViewName());
    }
    @Test
    public void queAlComprarUnApunteConErrorAparezcaMensajeDeErrorEnLaDetalleApunte() {
        Usuario comprador = new Usuario();
        Usuario vendedor = new Usuario();
        Apunte apunte = new Apunte(1L);

        when(sessionMock.getAttribute("usuario")).thenReturn(comprador);

        when(requestMock.getSession()).thenReturn(sessionMock);

        when(servicioApunteMock.obtenerPorId(1L)).thenReturn(apunte);

        when(servicioUsuarioApunteMock.comprarApunte(comprador, vendedor, apunte)).thenReturn(false);

        controladorApunte.getDetalleApunteConListadoDeSusResenas(1L, requestMock, sessionMock);

        ModelAndView modelAndView = controladorApunte.comprarApunteEnDetalleApunte(1L, requestMock, sessionMock);

        ModelMap modelMap = modelAndView.getModelMap();

        assertEquals("Error al realizar la compra", modelMap.get("error"));

        assertEquals("apunte-detalle", modelAndView.getViewName());
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

    @Test
    public void queNoDejeEliminarApunteSiNoEsTuyo(){
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        apunte.setId(1L);
        usuario.setId(1L);

        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioUsuarioApunteMock.existeRelacionUsuarioApunteEditar(1L, 1L)).thenReturn(false);
        doNothing().when(servicioUsuarioApunteMock).eliminarApunte(1L);

        controladorApunte.eliminar(1L, sessionMock);

        verify(servicioUsuarioApunteMock, never()).eliminarApunte(1L);
    }

    @Test
    public void queDejeEliminarApunteSiEsTuyo(){
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        apunte.setId(2L);
        usuario.setId(1L);

        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(servicioUsuarioApunteMock.existeRelacionUsuarioApunteEditar(1L, 2L)).thenReturn(true);
        doNothing().when(servicioUsuarioApunteMock).eliminarApunte(2L);

        controladorApunte.eliminar(2L, sessionMock);

        verify(servicioUsuarioApunteMock, times(1)).eliminarApunte(2L);
    }




}
