package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteTest {

    private ServicioUsuarioApunteImpl servicioUsuarioApunte;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private ServicioUsuario servicioUsuarioMock;

    private List<UsuarioApunte> usuarioApuntesMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock, servicioUsuarioMock);
        usuarioApuntesMock = new ArrayList<>();
    }
    @Test
    public void alGuardarUnApunteFijarseSiElUsuarioLoTiene() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        usuarioApuntesMock.add(new UsuarioApunte(usuario, apunte));

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(usuario.getId())).thenReturn(usuarioApuntesMock);

        List<Apunte> resultado = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());

        assertTrue(resultado.contains(apunte));
    }

    @Test
    public void alTener1ApunteCompradoY1ApunteDeOtroUsuarioVendiendoseQueSoloSeMuestreElApunteEnVenta() {
        Usuario usuarioPropio = new Usuario();
        Usuario usuarioOtro = new Usuario();

        usuarioPropio.setId(1L);
        usuarioOtro.setId(2L);

        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();

        UsuarioApunte usuarioApuntePropio = new UsuarioApunte(usuarioPropio, apunte1);
        UsuarioApunte usuarioApunteOtro = new UsuarioApunte(usuarioOtro, apunte2);

        List<UsuarioApunte> apuntesDeOtrosUsuarios = new ArrayList<>();
        apuntesDeOtrosUsuarios.add(usuarioApunteOtro);

        List<UsuarioApunte> apuntesCompradosPorUsuario = new ArrayList<>();
        apuntesCompradosPorUsuario.add(usuarioApuntePropio);

        when(repositorioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(usuarioPropio.getId())).thenReturn(apuntesDeOtrosUsuarios);
        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(usuarioPropio.getId())).thenReturn(apuntesCompradosPorUsuario);

        List<Apunte> resultado = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuarioPropio.getId());

        assertEquals(1, resultado.size());
        assertEquals(apunte2, resultado.get(0));
    }

    @Test
    public void queElUsuarioPuedaComprarUnApunteYSeLeRestenLosPuntosQueCuesta() {
        Usuario usuario=new Usuario();
        Apunte apunte=new Apunte();
        usuario.setPuntos(100);
        apunte.setPrecio(50);

        when(servicioUsuarioMock.actualizar(usuario)).thenReturn(true);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(usuario, apunte);

        assertTrue(resultadoCompra);
        assertEquals(50, usuario.getPuntos());
    }

    @Test
    public void queElUsuarioNoPuedaComprarApunteSiNoTieneLosPuntosNecesarios() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        usuario.setPuntos(30);
        apunte.setPrecio(50);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(usuario, apunte);

        assertFalse(resultadoCompra);
        assertEquals(30, usuario.getPuntos());
    }

    @Test
    public void queNoSePuedaComprarSiUsuarioEsNull() {
        Apunte apunte = new Apunte();
        apunte.setPrecio(50);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(null, apunte);

        assertFalse(resultadoCompra);
    }

}




