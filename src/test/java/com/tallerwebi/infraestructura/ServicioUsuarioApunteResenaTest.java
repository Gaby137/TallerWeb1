package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;
    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioApunte servicioApunteMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioApunteResenaMock = mock(RepositorioUsuarioApunteResena.class);
        servicioUsuarioApunteMock = mock(ServicioUsuarioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioApunteMock = mock(ServicioApunte.class);

        servicioUsuarioApunteResena = new ServicioUsuarioApunteResenaImpl(
                repositorioUsuarioApunteResenaMock,
                servicioUsuarioApunteMock,
                servicioUsuarioMock,
                servicioApunteMock
        );
    }

    @Test
    public void obtieneUnaListaDeResenasPorElIdDelApunte() {
        Long idApunte = 1L;

        List<Resena> listaResenasMock = new ArrayList<>();

        Resena resenaMock1 = mock(Resena.class);
        listaResenasMock.add(resenaMock1);

        Resena resenaMock2 = mock(Resena.class);
        listaResenasMock.add(resenaMock2);

        when(servicioUsuarioApunteResena.obtenerLista(idApunte)).thenReturn(listaResenasMock);

        List<Resena> result = servicioUsuarioApunteResena.obtenerLista(idApunte);

        assertEquals(listaResenasMock, result);

        verify(repositorioUsuarioApunteResenaMock).obtenerResenasPorIdApunte(idApunte);

    }

    @Test
    public void queNoPuedaDarMasDeUnaResenaAUnMismoApunte() {
        Apunte apunteMock = mock(Apunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        Resena resenaMock = mock(Resena.class);

        when(apunteMock.getId()).thenReturn(1L);
        when(usuarioMock.getId()).thenReturn(10L);
        when(repositorioUsuarioApunteResenaMock.existeResenaConApunteYUsuario(10L, 1L)).thenReturn(true); // Supongamos que ya existe una reseña

        boolean result = servicioUsuarioApunteResena.registrar(usuarioMock, apunteMock, resenaMock);

        assertFalse(result);

    }

    @Test
    public void dar100PuntosAlUsuarioPorBuenasResenasCuandoElPromedioDeEstrellasDeLasResenasDeUnApunteDeMasDeCuatroYMedio() {
        Long apunteId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPuntos(100);
        Apunte apunte = new Apunte();
        apunte.setId(apunteId);
        Resena resena1 = new Resena();
        resena1.setCantidadDeEstrellas(5);
        Resena resena2 = new Resena();
        resena2.setCantidadDeEstrellas(4);
        Resena resena3 = new Resena();
        resena3.setCantidadDeEstrellas(5);
        Resena resena4 = new Resena();
        resena4.setCantidadDeEstrellas(5);
        Resena resena5 = new Resena();
        resena5.setCantidadDeEstrellas(5);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(resena1, resena2, resena3, resena4, resena5));
        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunteId)).thenReturn(usuario);
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertTrue(resultado);
        assertEquals(200, usuario.getPuntos());
    }

    @Test
    public void noDarPuntosAlUsuarioCuandoElPromedioDeEstrellasDeLasResenasEsMenorDeCuatroYMedio() {
        Long apunteId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPuntos(100);
        Apunte apunte = new Apunte();
        apunte.setId(apunteId);

        Resena resena1 = new Resena();
        resena1.setCantidadDeEstrellas(3);
        Resena resena2 = new Resena();
        resena2.setCantidadDeEstrellas(4);
        Resena resena3 = new Resena();
        resena3.setCantidadDeEstrellas(3);
        Resena resena4 = new Resena();
        resena4.setCantidadDeEstrellas(4);
        Resena resena5 = new Resena();
        resena5.setCantidadDeEstrellas(4);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(resena1, resena2, resena3, resena4, resena5));
        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunteId)).thenReturn(usuario);
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertFalse(resultado);
        assertEquals(100, usuario.getPuntos());
    }

    @Test
    public void noDarCienPuntosAlUsuarioCuandoElMetodoYaSeEjecutoParaEseApunte() {
        Long apunteId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPuntos(100);
        Apunte apunte = new Apunte();
        apunte.setId(apunteId);
        Resena resena1 = new Resena();
        resena1.setCantidadDeEstrellas(5);
        Resena resena2 = new Resena();
        resena2.setCantidadDeEstrellas(4);
        Resena resena3 = new Resena();
        resena3.setCantidadDeEstrellas(5);
        Resena resena4 = new Resena();
        resena4.setCantidadDeEstrellas(5);
        Resena resena5 = new Resena();
        resena5.setCantidadDeEstrellas(5);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(resena1, resena2, resena3, resena4, resena5));
        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunteId)).thenReturn(usuario);
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertTrue(resultado);
        assertEquals(200, usuario.getPuntos());

        Resena resena6 = new Resena();
        resena6.setCantidadDeEstrellas(5);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(resena1, resena2, resena3, resena4, resena5, resena6));
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado2 = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertFalse(resultado2);
        assertEquals(200, usuario.getPuntos());

    }

    @Test
    public void calcularPromedioPuntajeResenasDeUnApunteSinResenas() {
        Long apunteId = 1L;
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(new ArrayList<>());

        double resultado = servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunteId);

        assertEquals(0.0, resultado);
    }

    @Test
    public void calcularPromedioPuntajeResenasDeUnApunteConResenas() {
        Long apunteId = 1L;
        Resena resena1 = new Resena();
        resena1.setCantidadDeEstrellas(5);
        Resena resena2 = new Resena();
        resena2.setCantidadDeEstrellas(4);
        Resena resena3 = new Resena();
        resena3.setCantidadDeEstrellas(3);
        List<Resena> resenas = Arrays.asList(resena1, resena2, resena3);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(resenas);

        double resultado = servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunteId);

        assertEquals(4.0, resultado);
    }

    @Test
    public void calcularPromedioPuntajeResenasPorUsuarioSinApuntes() {
        Long usuarioId = 1L;
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuarioId)).thenReturn(Collections.emptyList());

        double resultado = servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuarioId);

        assertEquals(0.0, resultado);
    }

    @Test
    public void obtener1ApunteComprado() {
        Usuario usuario = new Usuario();
        List<UsuarioApunte> apuntes = new ArrayList<>();

        UsuarioApunte usuarioApunteComprado = new UsuarioApunte();
        usuarioApunteComprado.setTipoDeAcceso(TipoDeAcceso.LEER);
        apuntes.add(usuarioApunteComprado);

        UsuarioApunte usuarioApunteCreado2 = new UsuarioApunte();
        usuarioApunteCreado2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        apuntes.add(usuarioApunteCreado2);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId())).thenReturn(apuntes);

        List<UsuarioApunte> apuntesComprados = servicioUsuarioApunteResena.obtenerApuntesComprados(usuario);

        assertEquals(apuntesComprados.size(), 1);
    }

    @Test
    public void obtener1ApunteCreado() {
        Usuario usuario = new Usuario();
        List<UsuarioApunte> apuntes = new ArrayList<>();

        UsuarioApunte usuarioApunteCreado = new UsuarioApunte();
        usuarioApunteCreado.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        apuntes.add(usuarioApunteCreado);

        UsuarioApunte usuarioApunteCreado2 = new UsuarioApunte();
        usuarioApunteCreado2.setTipoDeAcceso(TipoDeAcceso.LEER);
        apuntes.add(usuarioApunteCreado2);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId())).thenReturn(apuntes);

        List<UsuarioApunte> apuntesCreados = servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        assertEquals(apuntesCreados.size(), 1);
    }

    @Test
    public void calcularPromedioPuntajeResenasPorLasResenasDeLosApuntesDelUsuario() {
        Long usuarioId = 1L;
        Apunte apunte1 = new Apunte();
        apunte1.setId(1L);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);

        UsuarioApunte usuarioApunte1 = new UsuarioApunte();
        usuarioApunte1.setApunte(apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        UsuarioApunte usuarioApunte2 = new UsuarioApunte();
        usuarioApunte2.setApunte(apunte2);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        List<UsuarioApunte> usuarioApuntes = Arrays.asList(usuarioApunte1, usuarioApunte2);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuarioId)).thenReturn(usuarioApuntes);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L)).thenReturn(List.of(new Resena(5), new Resena(3)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L)).thenReturn(List.of(new Resena(1), new Resena(3)));

        double resultado = servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuarioId);

        assertEquals(3.0, resultado);
    }
    @Test
    public void obtenerApuntesDestacadosYQueSoloTraiga6Apuntes() {
        Apunte apunte1 = new Apunte();
        apunte1.setId(1L);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);
        Apunte apunte3 = new Apunte();
        apunte3.setId(3L);
        Apunte apunte4 = new Apunte();
        apunte4.setId(4L);
        Apunte apunte5 = new Apunte();
        apunte5.setId(5L);
        Apunte apunte6 = new Apunte();
        apunte6.setId(6L);
        Apunte apunte7 = new Apunte();
        apunte6.setId(7L);

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(anyLong())).thenReturn(Arrays.asList(
                apunte1,
                apunte2,
                apunte3,
                apunte4,
                apunte5,
                apunte6,
                apunte7
        ));


        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(anyLong()))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));

        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte1.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte2.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte3.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte4.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte5.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte6.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte7.getId());

        List<Apunte> apuntesDestacados = servicioUsuarioApunteResena.obtenerMejoresApuntes(anyLong());

        Apunte[] apuntesEsperados = {apunte1, apunte2, apunte3, apunte4, apunte5, apunte6};
        assertEquals(apuntesDestacados.size(), 6);
        assertArrayEquals(apuntesEsperados, apuntesDestacados.toArray());
    }
    @Test
    public void obtenerUsuariosDestacadosYQueSoloTraiga6Usuarios() {
        Long usuarioId = 1L;

        Usuario usuario1 = new Usuario(1L);
        Usuario usuario2 = new Usuario(2L);
        Usuario usuario3 = new Usuario(3L);
        Usuario usuario4 = new Usuario(4L);
        Usuario usuario5 = new Usuario(5L);
        Usuario usuario6 = new Usuario(6L);
        Usuario usuario7 = new Usuario(7L);
        Usuario usuario8 = new Usuario(8L);

        Apunte apunte1 = new Apunte();
        apunte1.setId(1L);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);
        Apunte apunte3 = new Apunte();
        apunte3.setId(3L);
        Apunte apunte4 = new Apunte();
        apunte4.setId(4L);
        Apunte apunte5 = new Apunte();
        apunte5.setId(5L);
        Apunte apunte6 = new Apunte();
        apunte6.setId(6L);
        Apunte apunte7 = new Apunte();
        apunte6.setId(7L);

        UsuarioApunte usuarioApunte2 = new UsuarioApunte(usuario2, apunte1);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte(usuario3, apunte2);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4 = new UsuarioApunte(usuario4, apunte3);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5 = new UsuarioApunte(usuario5, apunte4);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte6 = new UsuarioApunte(usuario6, apunte5);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7 = new UsuarioApunte(usuario7, apunte6);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8 = new UsuarioApunte(usuario8, apunte7);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                usuario2,
                usuario3,
                usuario4,
                usuario5,
                usuario6,
                usuario7,
                usuario8
        ));

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(anyLong()))
                .thenReturn(Arrays.asList(usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5, usuarioApunte6, usuarioApunte7, usuarioApunte8));

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(anyLong()))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(anyLong())).thenReturn(Arrays.asList(usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5, usuarioApunte6, usuarioApunte7));

        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte2.getApunte().getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte3.getApunte().getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte4.getApunte().getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte5.getApunte().getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte6.getApunte().getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(usuarioApunte7.getApunte().getId());

        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario2.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario3.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario4.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario5.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario6.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuario7.getId());

        List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuarioId);

        Usuario[] usuariosEsperados = {usuario2, usuario3, usuario4, usuario5, usuario6, usuario7};
        assertEquals(usuariosDestacados.size(), 6);
        assertArrayEquals(usuariosEsperados, usuariosDestacados.toArray());
    }

}






