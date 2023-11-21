package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuarioApunteResenaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;

import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteTest {

    private ServicioUsuarioApunteImpl servicioUsuarioApunte;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioApunte servicioApunteMock;
    private ServicioUsuarioApunteResena servicioUsuarioApunteResenaMock;

    @BeforeEach
    public void init() {
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioApunteMock = mock(ServicioApunte.class);
        servicioUsuarioApunteResenaMock = mock(ServicioUsuarioApunteResena.class);
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);
        servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock, servicioUsuarioMock, servicioApunteMock);  // Pasar el servicioUsuarioMock como argumento
    }

    @Test
    public void obtieneUnaListaDeApuntesPorElIdDelUsuario() {
        Long usuarioId = 1L;

        List<UsuarioApunte> usuarioApuntes = new ArrayList<>();

        usuarioApuntes.add(new UsuarioApunte(new Usuario(1L), new Apunte("Apunte 1", 1L)));
        usuarioApuntes.add(new UsuarioApunte(new Usuario(1L), new Apunte("Apunte 2", 2L)));

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(usuarioId)).thenReturn(usuarioApuntes);

        List<UsuarioApunte> resultado = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuarioId);

        assertEquals(2, resultado.size());
    }

    @Test
    public void alQuererBuscarUnaRelacionUsuarioApuntePorElIdDelUsuarioQueDevuelvaDichaRelacion() {
        Apunte apunte = new Apunte();
        Usuario usuario = new Usuario();
        UsuarioApunte usuarioApunte = new UsuarioApunte(usuario, apunte);

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(usuario.getId())).thenReturn(Arrays.asList(usuarioApunte));

        List<UsuarioApunte> resultado = servicioUsuarioApunte.obtenerApuntesPorUsuario(usuario.getId());

        assertTrue(resultado.contains(usuarioApunte));
    }

    @Test
    public void alTener1ApunteCompradoY1ApunteDeOtroUsuarioVendiendoseQueSoloSeMuestreElApunteEnVenta() {
        Usuario usuarioPropio = new Usuario();
        Usuario usuarioVendedor = new Usuario();

        Apunte apunteEnVenta = new Apunte();
        Apunte apunteComprado = new Apunte();

        UsuarioApunte usuarioApunteEnVenta = new UsuarioApunte(usuarioVendedor, apunteEnVenta);

        UsuarioApunte usuarioApunteComprado = new UsuarioApunte(usuarioPropio, apunteComprado);

        usuarioApunteEnVenta.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        List<UsuarioApunte> listaUsuarioApunte = new ArrayList<>();
        listaUsuarioApunte.add(usuarioApunteEnVenta);
        listaUsuarioApunte.add(usuarioApunteComprado);

        when(repositorioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(usuarioPropio.getId())).thenReturn(listaUsuarioApunte);

        List<Apunte> resultado = servicioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuarioPropio.getId());

        assertEquals(1, resultado.size());
        assertEquals(apunteEnVenta, resultado.get(0));
    }

    @Test
    public void queElUsuarioPuedaComprarUnApunteYSeLeRestenLosPuntosQueCuestaYAlVendedorSeLeSumen() {
        Usuario comprador = new Usuario();
        comprador.setPuntos(100);

        Usuario vendedor = new Usuario();
        vendedor.setPuntos(100);

        Apunte apunte = new Apunte();
        apunte.setPrecio(50);

        boolean compraExitosa = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        assertTrue(compraExitosa);

        assertEquals(50, comprador.getPuntos());
        assertEquals(150, vendedor.getPuntos());
    }


    @Test
    public void queElUsuarioNoPuedaComprarApunteSiNoTieneLosPuntosNecesarios() {
        Usuario comprador = new Usuario();
        Apunte apunte = new Apunte();
        Usuario vendedor = new Usuario();
        comprador.setPuntos(30);
        apunte.setPrecio(50);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(comprador, vendedor, apunte);

        assertFalse(resultadoCompra);
        assertEquals(30, comprador.getPuntos());
        verify(repositorioUsuarioApunteMock, never()).registrar(any(UsuarioApunte.class));
    }

    @Test
    public void queNoSePuedaComprarSiUsuarioEsNull() {
        Apunte apunte = new Apunte();
        apunte.setPrecio(50);

        boolean resultadoCompra = servicioUsuarioApunte.comprarApunte(null, null, apunte);

        assertFalse(resultadoCompra);
    }

    @Test
    public void queAlBorrarUnApunteSiNadieLoTieneCompradoQueBorreLaRelacionDirectamente() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        apunte.setId(1L);
        UsuarioApunte usuarioApunte = new UsuarioApunte(usuario, apunte);
        usuarioApunte.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        when(repositorioUsuarioApunteMock.obtenerRelacionesUsuarioApuntePorIdDeApunte(apunte.getId()))
                .thenReturn(List.of(usuarioApunte));
        doNothing().when(repositorioUsuarioApunteMock).eliminarRelacionUsuarioApuntePorId(usuarioApunte.getId());
        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        servicioUsuarioApunte.eliminarApunte(apunte.getId());

        verify(repositorioUsuarioApunteMock, times(1)).eliminarRelacionUsuarioApuntePorId(usuarioApunte.getId());
    }

    @Test
    public void queAlBorrarUnApunteQueYaFueCompradoNoBorreLaRelacionYSoloDesactiveElApunte() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        apunte.setId(1L);
        apunte.setActivo(true);
        UsuarioApunte usuarioApunte = new UsuarioApunte(usuario, apunte);
        usuarioApunte.setTipoDeAcceso(TipoDeAcceso.LEER);

        when(repositorioUsuarioApunteMock.obtenerRelacionesUsuarioApuntePorIdDeApunte(apunte.getId()))
                .thenReturn(List.of(usuarioApunte));
        doNothing().when(repositorioUsuarioApunteMock).eliminarRelacionUsuarioApuntePorId(usuarioApunte.getId());
        when(servicioApunteMock.obtenerPorId(apunte.getId())).thenReturn(apunte);

        servicioUsuarioApunte.eliminarApunte(apunte.getId());

        verify(repositorioUsuarioApunteMock, times(0)).eliminarRelacionUsuarioApuntePorId(usuarioApunte.getId());
        assertFalse(apunte.isActivo());;
    }

    @Test
    public void queAparezcanTodosLosApuntesCreadosPorElUsuarioActualYPorLosDemasUsuarios(){
        Usuario usuario1 = new Usuario(1L);
        Usuario usuario2 = new Usuario(2L);
        Apunte apunte1 = new Apunte(1L);
        Apunte apunte2 = new Apunte(2L);
        Apunte apunte3 = new Apunte(3L);
        Apunte apunte4 = new Apunte(4L);

        UsuarioApunte usuarioApunte1 = new UsuarioApunte(usuario1, apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte2 = new UsuarioApunte(usuario1, apunte2);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte(usuario2, apunte3);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4 = new UsuarioApunte(usuario2, apunte4);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5 = new UsuarioApunte(usuario2, apunte1);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.LEER);

        when(repositorioUsuarioApunteMock.obtenerApuntesPorIdUsuario(1L)).thenReturn(List.of(usuarioApunte1, usuarioApunte2));
        when(repositorioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(1L)).thenReturn(List.of(usuarioApunte3, usuarioApunte4, usuarioApunte5));

        List<Apunte> apuntesEsperados = servicioUsuarioApunte.obtenerTodosLosApuntes(1L);

        assertEquals(4, apuntesEsperados.size());
}

}




