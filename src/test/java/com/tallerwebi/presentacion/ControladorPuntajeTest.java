  
package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Apunte;
import com.tallerwebi.dominio.ServicioApunte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorPuntajeTest {
    
    private ControladorApunte controladorApunte; // Cambio de ControladorApunte a ControladorPuntaje
    private DatosApunte datosApunte;
    private Apunte apunteMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioApunte servicioApunteMock;

    @BeforeEach
    public void init(){
        String pathArchivo = "archivo.pdf";
        String nombre = "Apunte 1";
        String descripcion = "Descripcion del Apunte";
        datosApunte = new DatosApunte(pathArchivo, nombre, descripcion);

        apunteMock = mock(Apunte.class);
        when(apunteMock.getId()).thenReturn(1L);
        when(apunteMock.getNombre()).thenReturn("Apunte 1");

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioApunteMock = mock(ServicioApunte.class);
        controladorApunte = new ControladorApunte(servicioApunteMock);
    }

    @Test
    public void testPublicarExitoso() {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        when(servicioApunteMock.registrar(datosApunteMock)).thenReturn(true);

        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock);

        // Verificación
        assertEquals("apuntePublicadoExito", modelAndView.getViewName());
        assertFalse(modelAndView.getModel().containsKey("error"));
    }

    @Test
    public void testPublicarFallo() {
        // Configuración de objetos simulados
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        when(servicioApunteMock.registrar(datosApunteMock)).thenReturn(false);

        // Ejecución de la prueba
        ModelAndView modelAndView = controladorApunte.publicar(datosApunteMock);

        // Verificación
        assertEquals("subirApunte", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Por favor complete todos los campos", modelAndView.getModel().get("error"));
    }
 
}
