package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.Rol;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class ControladorAdministradorTest {
    private ServicioAdministrador servicioAdministradorMock;
    private ControladorAdministrador controladorAdministrador;
    private HttpSession session;
    private BindingResult resultMock;

    @BeforeEach
    public void init() {
        servicioAdministradorMock = mock(ServicioAdministrador.class);
        controladorAdministrador = new ControladorAdministrador(servicioAdministradorMock);

        session = mock(HttpSession.class);
        resultMock = mock(BindingResult.class);
    }

    @Test
    void testIngresarALaVistaFormCarreraSiendoAdmin() {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.ADMIN);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelAndView = controladorAdministrador.carrera(session);

        assertEquals("altaCarrera", modelAndView.getViewName());
    }

    @Test
    void testIngresarALaVistaFormCarreraSinSerAdmin() {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.USUARIO);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelAndView = controladorAdministrador.carrera(session);

        assertEquals("redirect:/home", modelAndView.getViewName());
    }

    @Test
    void testIngresarALaVistaFormCarreraSinLogearse() {
        when(session.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorAdministrador.carrera(session);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    void testRegistrarUnaCarrera() {
        DatosCarrera datosCarrera = new DatosCarrera();

        Usuario usuario = new Usuario();
        usuario.setRol(Rol.ADMIN);
        session.setAttribute("usuario", usuario);

        when(resultMock.hasErrors()).thenReturn(false);
        doNothing().when(servicioAdministradorMock).registrarCarrera(datosCarrera);

        ModelAndView modelAndView = controladorAdministrador.subirCarrera(datosCarrera, resultMock, session);

        assertEquals("redirect:/home", modelAndView.getViewName());

        verify(servicioAdministradorMock, times(1)).registrarCarrera(datosCarrera);
    }

    @Test
    void testIngresarALaVistaFormMateriaSiendoAdmin() {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.ADMIN);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelAndView = controladorAdministrador.materia(session);

        assertEquals("altaMateria", modelAndView.getViewName());
    }

    @Test
    void testIngresarALaVistaFormMateriaSinSerAdmin() {
        Usuario usuario = new Usuario();
        usuario.setRol(Rol.USUARIO);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        ModelAndView modelAndView = controladorAdministrador.materia(session);

        assertEquals("redirect:/home", modelAndView.getViewName());
    }

    @Test
    void testIngresarALaVistaFormMateriaSinLogearse() {
        when(session.getAttribute("usuario")).thenReturn(null);

        ModelAndView modelAndView = controladorAdministrador.materia(session);

        assertEquals("redirect:/login", modelAndView.getViewName());
    }

    @Test
    void testRegistrarUnaMateria() {
        DatosMateria datosMateria = new DatosMateria();

        Usuario usuario = new Usuario();
        usuario.setRol(Rol.ADMIN);
        session.setAttribute("usuario", usuario);

        when(resultMock.hasErrors()).thenReturn(false);
        doNothing().when(servicioAdministradorMock).registrarMateria(datosMateria);

        ModelAndView modelAndView = controladorAdministrador.subirMateria(datosMateria, resultMock, session);

        assertEquals("redirect:/home", modelAndView.getViewName());

    }

    @Test
    void testObtenerMateriasPorCarrera() {
        Long idCarrera = 1L;

        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setRol(Rol.ADMIN);
        when(session.getAttribute("usuario")).thenReturn(usuarioAdmin);

        ResponseEntity<List<Materia>> response = controladorAdministrador.obtenerMateriasPorCarrera(idCarrera);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        verify(servicioAdministradorMock, times(1)).obtenerMateriasPorCarrera(idCarrera);
    }

}
