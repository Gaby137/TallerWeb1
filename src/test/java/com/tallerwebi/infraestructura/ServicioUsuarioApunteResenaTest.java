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

        // Mock de la clase Apunte
        Apunte apunteMock = mock(Apunte.class);
        when(apunteMock.getId()).thenReturn(1L);
        when(apunteMock.getNombre()).thenReturn("Apunte 1");

        // Mock de la clase Usuario
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getId()).thenReturn(10L);
        when(usuarioMock.getNombre()).thenReturn("Usuario 10");

        // Mock de la clase Resena
        Resena resenaMock = mock(Resena.class);
        when(resenaMock.getId()).thenReturn(1L);
        when(resenaMock.getDescripcion()).thenReturn("Reseña 1");
        when(resenaMock.getCantidadDeEstrellas()).thenReturn(5);
        // Configura otras propiedades y métodos según tus necesidades

        // Mock de la clase UsuarioApunteResena
        UsuarioApunteResena usuarioApunteResenaMock = mock(UsuarioApunteResena.class);
        when(usuarioApunteResenaMock.getId()).thenReturn(1L);
        when(usuarioApunteResenaMock.getUsuario()).thenReturn(usuarioMock);
        when(usuarioApunteResenaMock.getResena()).thenReturn(resenaMock);
        when(usuarioApunteResenaMock.getApunte()).thenReturn(apunteMock);

        Resena resenaNueva = new Resena();
        resenaNueva.setDescripcion("Buena nueva reseña");
        resenaNueva.setCantidadDeEstrellas(5);

        boolean result = servicioUsuarioApunteResena.registrar(usuarioMock,apunteMock,resenaNueva);

        assertFalse(result);

    }



}





