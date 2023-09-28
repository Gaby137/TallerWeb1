package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Apunte;
import com.tallerwebi.dominio.RepositorioApunte;
import com.tallerwebi.dominio.ServicioApunteImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class ServicioApunteTest {
    private ServicioApunteImpl servicioApunte;
    private RepositorioApunte repositorioApunteMock;

    @Autowired
    private WebApplicationContext wac;


    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        MockitoAnnotations.initMocks(this);
        repositorioApunteMock = mock(RepositorioApunte.class);
        servicioApunte = new ServicioApunteImpl(repositorioApunteMock);
    }

    @Test
    public void testRegistrarExitoso() {
        // Configuración de datos de ejemplo
        DatosApunte datosApunte = new DatosApunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba");

        // Ejecución de la prueba
        boolean resultado = servicioApunte.registrar(datosApunte);

        // Verificación
        assertTrue(resultado);
        // Verifica que se llamó al método del repositorio
        verify(repositorioApunteMock).registrarApunte(any(Apunte.class));
    }

    @Test
    public void testRegistrarFallo() {
        // Configuración de datos de ejemplo con información faltante
        DatosApunte datosApunte = new DatosApunte(null, "", null);

        // Ejecución de la prueba
        boolean resultado = servicioApunte.registrar(datosApunte);

        // Verificación
        assertFalse(resultado);

    }

    @Test
    public void testObtenerPorId() {
        // Configuración de un id de ejemplo
        Long idEjemplo = 1L;

        // Configuración del comportamiento del repositorio simulado
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", new Date(), new Date());
        when(repositorioApunteMock.obtenerApunte(idEjemplo)).thenReturn(apunteMock);

        // Ejecución de la prueba
        Apunte resultado = servicioApunte.obtenerPorId(idEjemplo);

        // Verificación
        assertNotNull(resultado);
        assertEquals(apunteMock, resultado);
    }

    @Test
    public void testActualizar() {
        // Configuración de un apunte de ejemplo
        Apunte apunteEjemplo = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", new Date(), new Date());

        // Ejecución de la prueba
        servicioApunte.actualizar(apunteEjemplo);

        // Verificación
        verify(repositorioApunteMock).modificarApunte(apunteEjemplo); // Verifica que se llamó al método del repositorio
    }

    @Test
    public void testEliminar() {
        Long idApunte = 1L;
        Apunte apunteEjemplo = new Apunte();

        // Configurar el comportamiento del repositorio para devolver el apunte de ejemplo
        when(repositorioApunteMock.obtenerApunte(idApunte)).thenReturn(apunteEjemplo);

        // Llamada al método que queremos probar
        servicioApunte.eliminar(idApunte);

        // Verificar que se llamó al método del repositorio para eliminar el apunte
        verify(repositorioApunteMock).eliminarApunte(apunteEjemplo);
    }


}
