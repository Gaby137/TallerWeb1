package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.Resena;
import com.tallerwebi.dominio.ServicioResena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ControladorResenaTest {

    private ServicioResena servicioResena;
    private ControladorResena controladorResena;

    @BeforeEach
    public void init() {
        servicioResena = mock(ServicioResena.class);
        controladorResena = new ControladorResena(servicioResena);
    }

    @Test
    void irAFormularioAltaDeberiaDevolverVistaConResenaVacia() {
        ModelAndView modelAndView = controladorResena.irAFormularioAlta();

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
}



