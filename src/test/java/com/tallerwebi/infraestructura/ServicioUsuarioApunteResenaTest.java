package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import com.tallerwebi.dominio.servicio.*;
import com.tallerwebi.presentacion.DatosApunte;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertFalse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;
    private RepositorioApunte repositorioApunteMock;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioApunte servicioApunteMock;

    @BeforeEach
    public void init() {
        repositorioUsuarioApunteResenaMock = mock(RepositorioUsuarioApunteResena.class);
        repositorioApunteMock = mock(RepositorioApunte.class);
        repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
        servicioUsuarioApunteMock = mock(ServicioUsuarioApunte.class);
        servicioUsuarioMock = mock(ServicioUsuario.class);
        servicioApunteMock = mock(ServicioApunte.class);

        servicioUsuarioApunteResena = new ServicioUsuarioApunteResenaImpl(
                repositorioUsuarioApunteResenaMock,
                repositorioApunteMock,
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

        boolean result = servicioUsuarioApunteResena.registrarResena(usuarioMock, apunteMock, resenaMock);

        assertFalse(result);

    }

    @Test
    public void unUsuarioPuedeSubirUnApunteExitosamente() {
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);

        when(datosApunteMock.getPathArchivo()).thenReturn("asasas.pdf");
        when(datosApunteMock.getNombre()).thenReturn("apunte1");
        when(datosApunteMock.getDescripcion()).thenReturn("descripcion de apunte");
        when(datosApunteMock.getPrecio()).thenReturn(100);

        doNothing().when(repositorioApunteMock).registrarApunte(any(Apunte.class));
        doNothing().when(repositorioUsuarioApunteMock).registrar(any(UsuarioApunte.class));

        servicioUsuarioApunteResena.registrarApunte(datosApunteMock, usuarioMock);

        verify(repositorioApunteMock).registrarApunte(any(Apunte.class));
        verify(repositorioUsuarioApunteMock).registrar(any(UsuarioApunte.class));
    }
    @Test
    public void SiUnApunteSeSubeVacioDebeDarError() {
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);

        when(datosApunteMock.getPathArchivo()).thenReturn("");
        when(datosApunteMock.getNombre()).thenReturn("");
        when(datosApunteMock.getDescripcion()).thenReturn("");
        when(datosApunteMock.getPrecio()).thenReturn(100);

        doNothing().when(repositorioApunteMock).registrarApunte(any(Apunte.class));
        doNothing().when(repositorioUsuarioApunteMock).registrar(any(UsuarioApunte.class));

        servicioUsuarioApunteResena.registrarApunte(datosApunteMock, usuarioMock);
        // Verificación
        // assertFalse(resultado);

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
    public void obtenerApuntesDestacadosEnOrden() {
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
        apunte7.setId(7L);

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(anyLong())).thenReturn(Arrays.asList(
                apunte1,
                apunte2,
                apunte3,
                apunte4,
                apunte5,
                apunte6,
                apunte7
        ));


        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L))
                .thenReturn(List.of(new Resena(4), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(3L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(4L))
                .thenReturn(List.of(new Resena(5), new Resena(3), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(5L))
                .thenReturn(List.of(new Resena(4), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(6L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(7L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));

        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte1.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte2.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte3.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte4.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte5.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte6.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(apunte7.getId());

        List<Apunte> apuntesDestacados = servicioUsuarioApunteResena.obtenerMejoresApuntes(anyLong());

        Apunte[] apuntesEsperados = {apunte3, apunte6, apunte2, apunte5, apunte4, apunte1};
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
        apunte7.setId(7L);


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
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);

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

    @Test
    public void obtenerUsuariosDestacadosEnOrden() {
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
        apunte7.setId(7L);


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
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                usuario2,
                usuario3,
                usuario4,
                usuario5,
                usuario6,
                usuario7,
                usuario8
        ));

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(2L))
                .thenReturn(List.of(usuarioApunte2));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(3L))
                .thenReturn(List.of(usuarioApunte3));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(4L))
                .thenReturn(List.of(usuarioApunte4));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(5L))
                .thenReturn(List.of(usuarioApunte5));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(6L))
                .thenReturn(List.of(usuarioApunte6));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(7L))
                .thenReturn(List.of(usuarioApunte7));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(8L))
                .thenReturn(List.of(usuarioApunte8));

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(3L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(4L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(5L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(6L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(7L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));


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

        Usuario[] usuariosEsperados = {usuario3, usuario5, usuario6, usuario2, usuario4, usuario7};
        assertArrayEquals(usuariosEsperados, usuariosDestacados.toArray());
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
    public void darle50PuntosAlUsuarioPorHaberSubido10Resenas() {
        Usuario usuario = new Usuario();

        Resena resena1 = new Resena();
        Resena resena2 = new Resena();
        Resena resena3 = new Resena();
        Resena resena4 = new Resena();
        Resena resena5 = new Resena();


        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(resena1, resena2, resena3, resena4, resena5));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Resena resena6 = new Resena();
        Resena resena7 = new Resena();
        Resena resena8 = new Resena();
        Resena resena9 = new Resena();
        Resena resena10 = new Resena();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(resena6, resena7, resena8, resena9, resena10));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Assert.assertEquals(50, usuario.getPuntos());
    }

    @Test
    public void darle50PuntosAlUsuarioPorHaberSubido10Apuntes() {
        Usuario usuario = new Usuario();

        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        Apunte apunte3 = new Apunte();
        Apunte apunte4 = new Apunte();
        Apunte apunte5 = new Apunte();

        UsuarioApunte usuarioApunte1 = new UsuarioApunte();
        usuarioApunte1.setApunte(apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte2 = new UsuarioApunte();
        usuarioApunte2.setApunte(apunte2);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte();
        usuarioApunte3.setApunte(apunte3);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4 = new UsuarioApunte();
        usuarioApunte4.setApunte(apunte4);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5 = new UsuarioApunte();
        usuarioApunte5.setApunte(apunte5);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.EDITAR);


        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(usuarioApunte1, usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Apunte apunte6 = new Apunte();
        Apunte apunte7 = new Apunte();
        Apunte apunte8 = new Apunte();
        Apunte apunte9 = new Apunte();
        Apunte apunte10 = new Apunte();

        UsuarioApunte usuarioApunte6 = new UsuarioApunte();
        usuarioApunte6.setApunte(apunte6);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7 = new UsuarioApunte();
        usuarioApunte7.setApunte(apunte7);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8 = new UsuarioApunte();
        usuarioApunte8.setApunte(apunte8);
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte9 = new UsuarioApunte();
        usuarioApunte9.setApunte(apunte9);
        usuarioApunte9.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte10 = new UsuarioApunte();
        usuarioApunte10.setApunte(apunte10);
        usuarioApunte10.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(usuarioApunte6, usuarioApunte7, usuarioApunte8, usuarioApunte9, usuarioApunte10));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Assert.assertEquals(50, usuario.getPuntos());
    }

    @Test
    public void saberSiUnApuntePuedeSerCompradoPorUnUsuarioEnCasoDeNoTenerloCompradoYVisceversa(){
        Apunte apunte1 = new Apunte(1L);
        Apunte apunte4 = new Apunte(2L);
        Apunte apunte5 = new Apunte(3L);
        Apunte apunte6 = new Apunte(4L);


        Usuario usuarioActual=new Usuario(1L);
        Usuario usuarioVendedor=new Usuario(2L);

        //Actual
        UsuarioApunte usuarioApunte1 = new UsuarioApunte();
        usuarioApunte1.setApunte(apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.LEER);
        UsuarioApunte usuarioApunte2 = new UsuarioApunte();
        usuarioApunte2.setApunte(apunte4);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.LEER);

        //Vendedor
        UsuarioApunte usuarioApunte6 = new UsuarioApunte();
        usuarioApunte6.setApunte(apunte1);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7 = new UsuarioApunte();
        usuarioApunte7.setApunte(apunte4);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8 = new UsuarioApunte();
        usuarioApunte8.setApunte(apunte5);
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte9 = new UsuarioApunte();
        usuarioApunte9.setApunte(apunte6);
        usuarioApunte9.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(2L))
                .thenReturn(List.of(usuarioApunte6, usuarioApunte7, usuarioApunte8, usuarioApunte9));

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(1L))
                .thenReturn(List.of(usuarioApunte1, usuarioApunte2));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuarioVendedor);
        servicioUsuarioApunteResena.obtenerApuntesComprados(usuarioActual);

        servicioUsuarioApunteResena.obtenerApuntesCreadosYVerSiPuedeComprar(usuarioVendedor, usuarioActual);

        Assert.assertFalse(apunte1.isSePuedeComprar());
        Assert.assertFalse(apunte4.isSePuedeComprar());
        Assert.assertTrue(apunte5.isSePuedeComprar());
        Assert.assertTrue(apunte6.isSePuedeComprar());
    }
}






