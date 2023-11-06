package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioApunteImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public class ServicioApunteTest {
    private ServicioApunteImpl servicioApunte;
    private RepositorioApunte repositorioApunteMock;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
 
        repositorioApunteMock = mock(RepositorioApunte.class);
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioApunte = new ServicioApunteImpl(repositorioApunteMock, repositorioUsuarioApunteMock);


    }

    @Test
    public void testObtenerApuntePoId() {
        // Configuración de un id de ejemplo
        Long idEjemplo = 1L;

        // Configuración del comportamiento del repositorio simulado
        Apunte apunteMock = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20,  new Date(), new Date());
        apunteMock.setId(idEjemplo);
        when(repositorioApunteMock.obtenerApunte(idEjemplo)).thenReturn(apunteMock);

        // Ejecución de la prueba
        Apunte resultado = servicioApunte.obtenerPorId(idEjemplo);

        // Verificación
        assertNotNull(resultado);
        assertEquals(apunteMock, resultado);
    }

    @Test
    public void testActualizarApunteFuncionamientoABML() {
        // Configuración de un apunte de ejemplo
        Apunte apunteEjemplo = new Apunte("archivo.pdf", "Apunte de prueba", "Descripción de prueba", 20, new Date(), new Date());

        // Ejecución de la prueba
        servicioApunte.actualizar(apunteEjemplo);

        // Verificación
        verify(repositorioApunteMock).modificarApunte(apunteEjemplo); // Verifica que se llamó al método del repositorio
    }

    @Test
    public void testEliminarApunteFuncionamientoABML() {
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
