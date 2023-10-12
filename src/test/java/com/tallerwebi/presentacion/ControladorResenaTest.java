package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioResena;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ControladorResenaTest {

    private ServicioResena servicioResena;
    private ServicioUsuario servicioUsuario;
    private ControladorResena controladorResena;
    private HttpSession sessionMock;
    private ControladorUsuario controladorUsuario;

    @BeforeEach
    public void init() {
        servicioResena = mock(ServicioResena.class);
        servicioUsuario=mock(ServicioUsuario.class);
        controladorResena = new ControladorResena(servicioResena, servicioUsuario);
        controladorUsuario=new ControladorUsuario(servicioUsuario);
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
    void listarResenasDeberiaDevolverVistaConListaDeResenas() {
        // preparación
        ModelMap modelMap = new ModelMap();
        List<Resena> resenas = new ArrayList<>();
        resenas.add(new Resena());
        resenas.add(new Resena());
        when(servicioResena.listar()).thenReturn(resenas);

        // ejecución
        ModelAndView modelAndView = controladorResena.listarResenas();

        // validación
        assertEquals("apunte-detalle", modelAndView.getViewName());
        List<Resena> resenasEnModelo = (List<Resena>) modelAndView.getModelMap().get("resenas");
        assertEquals(2, resenasEnModelo.size());
    }
    @Test
    void borrarResenaDeberiaLlamarMetodoBorrarDelServicio() {
        // Preparación
        Long idResenaABorrar = 1L;

        // Configuración del servicioResena para evitar excepciones
        doNothing().when(servicioResena).borrar(idResenaABorrar);

        // Ejecución
        ModelAndView modelAndView = controladorResena.borrar(idResenaABorrar);

        // Verificación de que el servicioResena.borrar se llamó una vez con el idResenaABorrar
        verify(servicioResena, times(1)).borrar(idResenaABorrar);

        // Verificación de la vista y modelo
        assertEquals("redirect:/apunte-detalle", modelAndView.getViewName());
        ModelMap modelMap = modelAndView.getModelMap();
        assertTrue(modelMap.containsKey("mensaje"));
        assertEquals("Reseña borrada exitosamente", modelMap.get("mensaje"));
        assertFalse(modelMap.containsKey("error"));
    }
    @Test
    void guardarResenaDeberiaGuardarResenaYAgregarPuntos() {
        // Preparación
        Resena resena = new Resena();
        Usuario usuario = new Usuario();
        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena();
        usuarioApunteResena.setUsuario(usuario);
        resena.setUsuarioResenaApunte(usuarioApunteResena);

        // Configuración del servicioResena para evitar excepciones
        doNothing().when(servicioResena).guardar(resena);

        // Configuración del servicioUsuario para evitar excepciones
        when(servicioUsuario.actualizar(any(Usuario.class))).thenReturn(true);

        // Ejecución
        ModelAndView modelAndView = controladorResena.guardarResena(resena);

        // Verificación
        // Verifica que se haya llamado al método guardar del servicioResena con la reseña
        verify(servicioResena, times(1)).guardar(resena);

        // Verifica que se haya llamado al método actualizar del servicioUsuario con el usuario
        verify(servicioUsuario, times(1)).actualizar(usuario);

        // Verifica que la vista sea la esperada (listarResenas)
        assertEquals("apunte-detalle", modelAndView.getViewName());

    }
}



