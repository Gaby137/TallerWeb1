package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Puntaje;
import com.tallerwebi.dominio.iRepositorio.RepositorioPuntaje;
import com.tallerwebi.dominio.servicio.ServicioPuntajeImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServicioPuntajeTest {

    private ServicioPuntajeImpl servicioPuntaje;
    private RepositorioPuntaje repositorioPuntajeMock;

    @BeforeEach
    public void init() {
        repositorioPuntajeMock = mock(RepositorioPuntaje.class);
        servicioPuntaje = new ServicioPuntajeImpl(repositorioPuntajeMock);
    }

    @Test
    public void testRegistrarExitoso() {
        DatosPuntaje datosPuntaje = new DatosPuntaje("usuario123", 50);

        boolean resultado = servicioPuntaje.registrar(datosPuntaje);

        assertTrue(resultado);
        verify(repositorioPuntajeMock).registrarPuntaje(any(Puntaje.class));
    }

    @Test
    public void testRegistrarFallo() {
        DatosPuntaje datosPuntaje = new DatosPuntaje(null, 0);

        boolean resultado = servicioPuntaje.registrar(datosPuntaje);

        assertFalse(resultado);
    }

    @Test
    public void testObtenerPorId() {
        Long idEjemplo = 1L;

        Puntaje puntajeMock = new Puntaje(50, new Date(), new Date());
        when(repositorioPuntajeMock.obtenerPuntaje(idEjemplo)).thenReturn(puntajeMock);

        Puntaje resultado = servicioPuntaje.obtenerPorId(idEjemplo);

        assertNotNull(resultado);
        assertEquals(puntajeMock, resultado);
    }

    @Test
    public void testActualizar() {
        Puntaje puntajeEjemplo = new Puntaje(50, new Date(), new Date());

        servicioPuntaje.actualizar(puntajeEjemplo);

        verify(repositorioPuntajeMock).modificarPuntaje(puntajeEjemplo);
    }

    @Test
    public void testEliminar() {
        Puntaje puntajeEjemplo = new Puntaje(50, new Date(), new Date());

        servicioPuntaje.eliminar(puntajeEjemplo);

        verify(repositorioPuntajeMock).eliminarPuntaje(puntajeEjemplo);
    }
}
