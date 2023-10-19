package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResenaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteTest {

    private ServicioUsuarioApunteImpl servicioUsuarioApunte;

    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
    }

    @Test
    public void obtieneUnaListaDeApuntesPorElIdDelUsuario() {
        Long usuarioId = 1L;

        List<UsuarioApunte> usuarioApuntes = new ArrayList<>();

        usuarioApuntes.add(new UsuarioApunte(new Usuario(1L),new Apunte("Apunte 1",1L)));
        usuarioApuntes.add(new UsuarioApunte(new Usuario(1L),new Apunte("Apunte 2", 2L)));

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(usuarioId)).thenReturn(usuarioApuntes);

        // Ejecutar el método bajo prueba
        List<Apunte> resultado = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuarioId);

        // Verificar el resultado
        assertEquals(2, resultado.size());
    }
}





