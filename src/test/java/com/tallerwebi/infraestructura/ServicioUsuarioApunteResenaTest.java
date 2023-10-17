package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioMateriaImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResenaImpl;
import com.tallerwebi.presentacion.DatosMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioUsuarioApunteResenaMock = mock(RepositorioUsuarioApunteResena.class);
        servicioUsuarioApunteResena = new ServicioUsuarioApunteResenaImpl(repositorioUsuarioApunteResenaMock);
    }

    @Test
    public void obtieneUnaListaDeResenasPorElIdDelApunte() {
        Long idApunte = 1L;

        // Crear una lista de mocks de Resena
        List<Resena> listaResenasMock = new ArrayList<>();

        // Crear y agregar varios mocks de Resena a la lista
        Resena resenaMock1 = mock(Resena.class);
        listaResenasMock.add(resenaMock1);

        Resena resenaMock2 = mock(Resena.class);
        listaResenasMock.add(resenaMock2);

        when(servicioUsuarioApunteResena.obtenerLista(idApunte)).thenReturn(listaResenasMock);

        // Ejecución de la prueba
        List<Resena> result= servicioUsuarioApunteResena.obtenerLista(idApunte);

        // Verificación
        assertEquals(listaResenasMock, result);
        // Verifica que se llamó al método del repositorio
        verify(repositorioUsuarioApunteResenaMock).obtenerResenasPorIdApunte(idApunte);

    }

    @Test
    public void queNoPuedaDarMasDeUnaResenaAUnMismoApunte(){

        // Crea objetos mock de Apunte, Usuario y Servicio
        Apunte apunteMock = mock(Apunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        Resena resenaMock= mock(Resena.class);

        // Configura las interacciones de los objetos mock
        when(apunteMock.getId()).thenReturn(1L);
        when(usuarioMock.getId()).thenReturn(10L);
        when(repositorioUsuarioApunteResenaMock.existeResenaConApunteYUsuario(10L, 1L)).thenReturn(true); // Supongamos que ya existe una reseña

        boolean result = servicioUsuarioApunteResena.registrar(usuarioMock,apunteMock,resenaMock);

        assertFalse(result);

    }



}





