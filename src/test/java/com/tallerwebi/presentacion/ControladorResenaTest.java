package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioApunte;
import com.tallerwebi.dominio.servicio.ServicioResena;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ControladorResenaTest {

    private HttpServletRequest requestMock;
    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResena;
    private ControladorResena controladorResena;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        requestMock = mock(HttpServletRequest.class);
        servicioResena = mock(ServicioResena.class);
        servicioUsuario = mock(ServicioUsuario.class);
        servicioApunte = mock(ServicioApunte.class);
        servicioUsuarioApunteResena = mock(ServicioUsuarioApunteResena.class);
        controladorResena = new ControladorResena(servicioResena, servicioUsuario, servicioApunte, servicioUsuarioApunteResena);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    void irAFormularioAltaDeberiaDevolverVistaConResenaVacia() {
        ModelAndView modelAndView = controladorResena.irAFormularioAlta(sessionMock);

        // validacion
        assertEquals("formulario-alta-resena", modelAndView.getViewName());
        Resena resena = (Resena) modelAndView.getModelMap().get("resena");
        assertEquals(null, resena.getId());
    }

    @Test
    void borrarResenaDeberiaLlamarMetodoBorrarDelServicio() {
        // Preparación
        Long idResenaABorrar = 1L;

        // Configuración del servicioResena para evitar excepciones
        doNothing().when(servicioResena).borrar(idResenaABorrar);
        when(sessionMock.getAttribute("idApunte")).thenReturn(1L);

        // Ejecución
        ModelAndView modelAndView = controladorResena.borrar(idResenaABorrar, sessionMock);

        // Verificación de que el servicioResena.borrar se llamó una vez con el idResenaABorrar
        verify(servicioResena, times(1)).borrar(idResenaABorrar);

        // Verificación de la vista y modelo
        assertEquals("redirect:/detalleApunte/{id}", modelAndView.getViewName());
        ModelMap modelMap = modelAndView.getModelMap();
        assertTrue(modelMap.containsKey("mensaje"));
        assertEquals("Reseña borrada exitosamente", modelMap.get("mensaje"));
        assertFalse(modelMap.containsKey("error"));
    }
    @Test
    void guardarResenaDeberiaGuardarResenaYAgregarPuntos() {
        // Preparación
        Long APUNTE_ID = 1L;
        Resena resena = new Resena();
        resena.setDescripcion("lalalal");
        resena.setCantidadDeEstrellas(4);
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", new Date(), new Date());
        apunteMock.setId(APUNTE_ID);
        Usuario usuario = new Usuario();  // Asegúrate de ajustar esto según tus necesidades
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);
        resena.setUsuarioResenaApunte(usuarioApunteResena);

        // Configuración del servicioResena para evitar excepciones
        doNothing().when(servicioResena).guardar(resena);
        doNothing().when(servicioUsuarioApunteResena).registrar(usuarioApunteResena);

        // Configuración del servicioUsuario para evitar excepciones
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);
        when(sessionMock.getAttribute("usuario")).thenReturn(usuario);
        when(sessionMock.getAttribute("idApunte")).thenReturn(APUNTE_ID);
        when(servicioApunte.obtenerPorId(APUNTE_ID)).thenReturn(apunteMock);

        // Ejecución
        ModelAndView modelAndView = controladorResena.guardarResena(resena, sessionMock);

        // Verificación
        // Verifica que se haya llamado al método guardar del servicioResena con la reseña
        verify(servicioResena, times(1)).guardar(resena);

        // Verifica que se haya llamado al método actualizar del servicioUsuario con el usuario
        verify(servicioUsuario, times(1)).actualizar(usuario);

        // Verifica que la vista sea la esperada (listarResenas)
        assertEquals("redirect:/detalleApunte/{id}", modelAndView.getViewName());
        assertEquals(APUNTE_ID, modelAndView.getModelMap().getAttribute("id"));

    }
}



