package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.DatosMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;
    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioApunte servicioApunteMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioApunteResenaMock = mock(RepositorioUsuarioApunteResena.class);
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioUsuarioApunteMock = mock(ServicioUsuarioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioApunteMock = mock(ServicioApunte.class);

        servicioUsuarioApunteResena = new ServicioUsuarioApunteResenaImpl(
                repositorioUsuarioApunteResenaMock,
                repositorioUsuarioApunteMock,
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

        List<Resena> result= servicioUsuarioApunteResena.obtenerLista(idApunte);

        assertEquals(listaResenasMock, result);

        verify(repositorioUsuarioApunteResenaMock).obtenerResenasPorIdApunte(idApunte);

    }

    @Test
    public void queNoPuedaDarMasDeUnaResenaAUnMismoApunte(){
        Apunte apunteMock = mock(Apunte.class);
        Usuario usuarioMock = mock(Usuario.class);
        Resena resenaMock= mock(Resena.class);

        when(apunteMock.getId()).thenReturn(1L);
        when(usuarioMock.getId()).thenReturn(10L);
        when(repositorioUsuarioApunteResenaMock.existeResenaConApunteYUsuario(10L, 1L)).thenReturn(true); // Supongamos que ya existe una reseña

        boolean result = servicioUsuarioApunteResena.registrar(usuarioMock,apunteMock,resenaMock);

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
    public void obtener2ApuntesConPromedioSuperiorA4EstrellasY1ConMenos() {
        Long usuarioId = 1L;
        Apunte apunte1 = new Apunte();
        apunte1.setId(1L);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);
        Apunte apunte3 = new Apunte();
        apunte3.setId(3L);

        List<Apunte> todosLosApuntes = List.of(apunte1, apunte2, apunte3);

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(usuarioId)).thenReturn(todosLosApuntes);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L)).thenReturn(List.of(new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L)).thenReturn(List.of(new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(3L)).thenReturn(List.of(new Resena(3)));

        List<Apunte> mejoresApuntes = servicioUsuarioApunteResena.obtenerMejoresApuntes(usuarioId);

        assertEquals(2, mejoresApuntes.size());
    }

    @Test
    public void calcularPromedioPuntajeResenasPorUsuarioSinApuntes() {
        Long usuarioId = 1L;
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuarioId)).thenReturn(Collections.emptyList());

        double resultado = servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(usuarioId);

        assertEquals(0.0, resultado);
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
    public void obtenerUsuariosDestacadosEnOrden() {
        Long usuarioId = 1L;

        Usuario usuario2 = new Usuario(2L);
        Usuario usuario3 = new Usuario(3L);
        Usuario usuario4 = new Usuario(4L);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                usuario2,
                usuario3,
                usuario4
        ));

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(anyLong()))
                .thenReturn(List.of(new Resena(5)))
                .thenReturn(List.of(new Resena(4)))
                .thenReturn(List.of(new Resena(3)));

        List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuarioId);

        Usuario[] usuariosEsperados = {usuario2, usuario3, usuario4};
        assertArrayEquals(usuariosEsperados, usuariosDestacados.toArray());
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
    public void testObtenerApuntesCreadosYVerSiPuedeComprar() {
        Usuario usuario = new Usuario();
        Usuario usuarioActual = new Usuario();

        // Configura el usuario y el usuario actual según tus necesidades
        usuario.setId(1L);
        usuarioActual.setId(2L);

        // Configura los apuntes creados por el usuario
        UsuarioApunte apunteCreado1 = new UsuarioApunte();
        apunteCreado1.setUsuario(usuario);
        apunteCreado1.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        UsuarioApunte apunteCreado2 = new UsuarioApunte();
        apunteCreado2.setUsuario(usuario);
        apunteCreado2.setTipoDeAcceso(TipoDeAcceso.LEER);

        List<UsuarioApunte> apuntesCreados = Arrays.asList(apunteCreado1, apunteCreado2);

        when(servicioUsuarioApunteResena.obtenerApuntesCreados(usuario)).thenReturn(apuntesCreados);

        // Simula la lógica para obtener el tipo de acceso
        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(usuarioActual.getId(), apunteCreado1.getApunte().getId())).thenReturn(TipoDeAcceso.EDITAR);
        when(servicioUsuarioApunteMock.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(usuarioActual.getId(), apunteCreado2.getApunte().getId())).thenReturn(TipoDeAcceso.LEER);

        List<UsuarioApunte> apuntesObtenidos = servicioUsuarioApunteResena.obtenerApuntesCreadosYVerSiPuedeComprar(usuario, usuarioActual);

        assertEquals(2, apuntesObtenidos.size());
        assertTrue(apuntesObtenidos.get(0).getApunte().isSePuedeComprar());
        assertFalse(apuntesObtenidos.get(1).getApunte().isSePuedeComprar());
    }

}






