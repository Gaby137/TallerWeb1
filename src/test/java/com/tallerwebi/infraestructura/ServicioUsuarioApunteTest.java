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
import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;

import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteTest {

    private ServicioUsuarioApunteImpl servicioUsuarioApunte;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private Usuario usuarioMock;
    private Apunte apunteMock;
    private List<UsuarioApunte> usuarioApuntesMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock, servicioUsuarioMock);
        usuarioMock = mock(Usuario.class);
        apunteMock = mock(Apunte.class);
        usuarioApuntesMock = new ArrayList<>();
        servicioUsuarioMock = mock(ServicioUsuario.class);
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
    @Test
    public void alGuardarUnApunteFijarseSiElUsuarioLoTiene() {
        Long userId = 1L;
        Apunte apunte = new Apunte();
        usuarioApuntesMock.add(new UsuarioApunte(null, apunte));

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(userId)).thenReturn(usuarioApuntesMock);

        List<Apunte> resultado = servicioUsuarioApunte.obtenerApuntesPorUsuario(userId);

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

        apunte1.setId(1L);
        apunte2.setId(2L);

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
        verify(repositorioUsuarioApunteMock, times(1)).registrar(any(UsuarioApunte.class));
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
        verify(repositorioUsuarioApunteMock, never()).registrar(any(UsuarioApunte.class));
    }

    @Test
    public void queNoSePuedaComprarSiUsuarioEsNull() {
        Apunte apunte = new Apunte();
        apunte.setPrecio(50);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(null, apunte);

        assertFalse(resultadoCompra);
    }

}




