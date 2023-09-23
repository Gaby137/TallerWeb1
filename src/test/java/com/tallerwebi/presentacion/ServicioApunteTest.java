package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Apunte;
import com.tallerwebi.dominio.RepositorioApunte;
import com.tallerwebi.dominio.ServicioApunteImpl;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class ServicioApunteTest {
    private ServicioApunteImpl servicioApunte;
    private RepositorioApunte repositorioApunteMock;


    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
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
        // Configuración de un apunte de ejemplo
        Apunte apunteEjemplo = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", new Date(), new Date());

        // Ejecución de la prueba
        servicioApunte.eliminar(apunteEjemplo);

        // Verificación
        verify(repositorioApunteMock).eliminarApunte(apunteEjemplo); // Verifica que se llamó al método del repositorio
    }


}
