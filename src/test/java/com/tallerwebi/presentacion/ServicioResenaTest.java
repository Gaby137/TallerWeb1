package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.servicio.ServicioResenaImpl;
import com.tallerwebi.dominio.entidad.Resena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioResenaTest {

    private ServicioResenaImpl servicioResena;
    private RepositorioResena repositorioResenaMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioResenaMock = mock(RepositorioResena.class);
        servicioResena = new ServicioResenaImpl(repositorioResenaMock);
    }

    @Test
    public void testGuardarResena() {
        // Configuración de datos de ejemplo
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Configuración del comportamiento del repositorio simulado
        doNothing().when(repositorioResenaMock).guardar(any(Resena.class));

        // Ejecución de la prueba
        servicioResena.guardar(resena);

        // Verificación
        verify(repositorioResenaMock, times(1)).guardar(resena);
    }

    @Test
    public void testBorrarResena() {
        // Configuración de una reseña de ejemplo
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Configuración del comportamiento del repositorio simulado
        doNothing().when(repositorioResenaMock).borrar(anyLong());

        // Ejecución de la prueba
        servicioResena.borrar(resena.getId());  // Usar directamente el ID de la reseña

        // Verificación
        verify(repositorioResenaMock, times(1)).borrar(resena.getId()); // Verifica que se llamó al método del repositorio
    }
    @Test
    public void testListarResenas() {
        // Configuración de datos de ejemplo
        Resena resena1 = new Resena();
        resena1.setDescripcion("Buena reseña 1");
        resena1.setCantidadDeEstrellas(5);

        Resena resena2 = new Resena();
        resena2.setDescripcion("Buena reseña 2");
        resena2.setCantidadDeEstrellas(4);

        // Configuración del comportamiento del repositorio simulado
        when(repositorioResenaMock.listar(0L)).thenReturn(List.of(resena1, resena2));

        // Ejecución de la prueba
        List<Resena> resenas = servicioResena.listar(0L);

        // Verificación
        assertNotNull(resenas, "La lista de reseñas no debe ser nula");
        assertEquals(2, resenas.size(), "La lista debe contener 2 reseñas");
        assertTrue(resenas.contains(resena1), "La lista debe contener la primera reseña");
        assertTrue(resenas.contains(resena2), "La lista debe contener la segunda reseña");
        verify(repositorioResenaMock, times(1)).listar(0L); // Verifica que se llamó al método del repositorio
    }

}
