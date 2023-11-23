package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        controladorResena = new ControladorResena(servicioResena, servicioUsuario, servicioApunte, servicioUsuarioApunte, servicioUsuarioApunteResena);
        sessionMock = mock(HttpSession.class);
        resultMock = mock(BindingResult.class);
        redirectAttributesMock = mock(RedirectAttributes.class);

    }

    @Test
    void irAFormularioAltaDeberiaDevolverVistaConResenaVacia() {
        Usuario usuarioMock = mock(Usuario.class);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuarioMock);
        ModelAndView modelAndView = controladorResena.irAFormularioAlta(sessionMock);
        // validacion
        assertEquals("formulario-alta-resena", modelAndView.getViewName());
        Resena resena = (Resena) modelAndView.getModelMap().get("resena");
        assertEquals(null, resena.getId());
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
    void guardarResenaDeberiaGuardarResenaYAgregarPuntos() {
        // Preparación
        Long APUNTE_ID = 1L;
        Resena resena = new Resena();
        resena.setDescripcion("lalalal");
        resena.setCantidadDeEstrellas(4);
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();  // Asegúrate de ajustar esto según tus necesidades
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);
        resena.setUsuarioResenaApunte(usuarioApunteResena);

        // Configuración del servicioResena para evitar excepciones

        when(servicioUsuarioApunteResena.registrarResena(any(Usuario.class), any(Apunte.class),any(Resena.class))).thenReturn(true);

        // Configuración del servicioUsuario para evitar excepciones
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);


        // Ejecución
        ModelAndView modelAndView = controladorResena.guardarResena(resena, resultMock, sessionMock);

        // Verificación

        // Verifica que la vista sea la esperada (listarResenas)
        assertEquals("redirect:/detalleApunte/1", modelAndView.getViewName());

    }

    @Test
    public void queNoDejeEliminarResenaSiNoEsTuya(){
        Usuario usuario = new Usuario(1L);
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
        Resena resena = new Resena();
        resena.setId(1L);


        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(1L);
        when(servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuarioYApunte(1L, 1L)).thenReturn(resena);

        controladorResena.borrar(1L, sessionMock, redirectAttributesMock);

        verify(servicioResena, times(1)).borrar(1L);
    }

}






