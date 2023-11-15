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
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.mock.web.MockMultipartFile;

import javax.validation.constraints.AssertFalse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;
    private RepositorioApunte repositorioApunteMock;
    private RepositorioUsuarioApunte repositorioUsuarioApunteMock;
    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;
    private ServicioUsuarioApunte servicioUsuarioApunteMock;
    private ServicioUsuario servicioUsuarioMock;
    private ServicioApunte servicioApunteMock;
    private MockMultipartFile pdf;

    @BeforeEach
    public void init() {
        pdf = mock(MockMultipartFile.class);
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

        UsuarioApunteResena usuarioApunteResenaMock= mock(UsuarioApunteResena.class);

        when(apunteMock.getId()).thenReturn(1L);
        when(usuarioMock.getId()).thenReturn(10L);
        when(usuarioApunteResenaMock.getUsuario()).thenReturn(usuarioMock);
        when(usuarioApunteResenaMock.getApunte()).thenReturn(apunteMock);
        when(usuarioApunteResenaMock.getResena()).thenReturn(resenaMock);

        List<UsuarioApunteResena> ls = new ArrayList<>();
        ls.add(new UsuarioApunteResena(usuarioMock,resenaMock,apunteMock));

        when(repositorioUsuarioApunteResenaMock.existeResenaConApunteYUsuario(10L, 1L)).thenReturn(ls);

        boolean result = servicioUsuarioApunteResena.registrarResena(usuarioMock, apunteMock, resenaMock);

        assertFalse(result);

    }

    @Test
    public void unUsuarioPuedeSubirUnApunteExitosamente() {
        DatosApunte datosApunteMock = mock(DatosApunte.class);
        Usuario usuarioMock = mock(Usuario.class);

        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        when(datosApunteMock.getPathArchivo().getOriginalFilename()).thenReturn("pdf.pdf");
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

        when(datosApunteMock.getPathArchivo()).thenReturn(pdf);
        when(datosApunteMock.getPathArchivo().getOriginalFilename()).thenReturn("pdf.pdf");
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
        apunte1.setActivo(true);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);
        apunte2.setActivo(true);

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
        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(anyLong())).thenReturn(Arrays.asList(
                listaDe7Apuntes.apunte1,
                listaDe7Apuntes.apunte2,
                listaDe7Apuntes.apunte3,
                listaDe7Apuntes.apunte4,
                listaDe7Apuntes.apunte5,
                listaDe7Apuntes.apunte6,
                listaDe7Apuntes.apunte7
        ));


        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(anyLong()))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        List<Apunte> apuntesDestacados = servicioUsuarioApunteResena.obtenerMejoresApuntes(anyLong());

        Apunte[] apuntesEsperados = {listaDe7Apuntes.apunte1, listaDe7Apuntes.apunte2, listaDe7Apuntes.apunte3, listaDe7Apuntes.apunte4, listaDe7Apuntes.apunte5, listaDe7Apuntes.apunte6};
        assertEquals(apuntesDestacados.size(), 6);
        assertArrayEquals(apuntesEsperados, apuntesDestacados.toArray());
    }

    @Test
    public void obtenerApuntesDestacadosEnOrden() {
        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(anyLong())).thenReturn(Arrays.asList(
                listaDe7Apuntes.apunte1,
                listaDe7Apuntes.apunte2,
                listaDe7Apuntes.apunte3,
                listaDe7Apuntes.apunte4,
                listaDe7Apuntes.apunte5,
                listaDe7Apuntes.apunte6,
                listaDe7Apuntes.apunte7
        ));


        resenasBuenasSimuladasPara7Apuntes();

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        List<Apunte> apuntesDestacados = servicioUsuarioApunteResena.obtenerMejoresApuntes(anyLong());

        Apunte[] apuntesEsperados = {listaDe7Apuntes.apunte3, listaDe7Apuntes.apunte6, listaDe7Apuntes.apunte2, listaDe7Apuntes.apunte5, listaDe7Apuntes.apunte4, listaDe7Apuntes.apunte1};
        assertEquals(apuntesDestacados.size(), 6);
        assertArrayEquals(apuntesEsperados, apuntesDestacados.toArray());
    }

    @Test
    public void queNoTraigaApuntesDestacadosSiNoTienenCuatroOMasDePromedio() {
        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        when(servicioUsuarioApunteMock.obtenerApuntesDeOtrosUsuarios(anyLong())).thenReturn(Arrays.asList(
                listaDe7Apuntes.apunte1,
                listaDe7Apuntes.apunte2,
                listaDe7Apuntes.apunte3,
                listaDe7Apuntes.apunte4,
                listaDe7Apuntes.apunte5,
                listaDe7Apuntes.apunte6,
                listaDe7Apuntes.apunte7
        ));

        resenasMalasSimuladasPara7Apuntes();

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        List<Apunte> apuntesDestacados = servicioUsuarioApunteResena.obtenerMejoresApuntes(anyLong());

        Apunte[] apuntesEsperados = {listaDe7Apuntes.apunte3, listaDe7Apuntes.apunte6};
        assertArrayEquals(apuntesEsperados, apuntesDestacados.toArray());
    }

    @Test
    public void obtenerUsuariosDestacadosYQueSoloTraiga6Usuarios() {
        Long usuarioId = 1L;

        ListaDe8Usuarios listaDe8Usuarios = getListaDe8Usuarios();

        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        ListaDe8UsuarioApunteConTipoDeAccesoEditar listaDe8UsuarioApunteConTipoDeAccesoEditar = getListaDe8UsuarioApunteConTipoDeAccesoEditar(listaDe8Usuarios, listaDe7Apuntes);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                listaDe8Usuarios.usuario2,
                listaDe8Usuarios.usuario3,
                listaDe8Usuarios.usuario4,
                listaDe8Usuarios.usuario5,
                listaDe8Usuarios.usuario6,
                listaDe8Usuarios.usuario7,
                listaDe8Usuarios.usuario8
        ));

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(anyLong()))
                .thenReturn(Arrays.asList(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte2, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte3, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte4, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte5, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte6, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte7, listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte8));

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(anyLong()))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        calculoDePromedioDeResenasPor6Usuarios(listaDe8Usuarios);

        List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuarioId);

        Usuario[] usuariosEsperados = {listaDe8Usuarios.usuario2, listaDe8Usuarios.usuario3, listaDe8Usuarios.usuario4, listaDe8Usuarios.usuario5, listaDe8Usuarios.usuario6, listaDe8Usuarios.usuario7};
        assertEquals(usuariosDestacados.size(), 6);
        assertArrayEquals(usuariosEsperados, usuariosDestacados.toArray());
    }
    @Test
    public void obtenerUsuariosDestacadosEnOrden() {
        Long usuarioId = 1L;

        ListaDe8Usuarios listaDe8Usuarios = getListaDe8Usuarios();

        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        ListaDe8UsuarioApunteConTipoDeAccesoEditar listaDe8UsuarioApunteConTipoDeAccesoEditar = getListaDe8UsuarioApunteConTipoDeAccesoEditar(listaDe8Usuarios, listaDe7Apuntes);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                listaDe8Usuarios.usuario2,
                listaDe8Usuarios.usuario3,
                listaDe8Usuarios.usuario4,
                listaDe8Usuarios.usuario5,
                listaDe8Usuarios.usuario6,
                listaDe8Usuarios.usuario7,
                listaDe8Usuarios.usuario8
        ));

        obtenerApuntesPara7Usuarios(listaDe8UsuarioApunteConTipoDeAccesoEditar);

        obtenerResenasBuenasPara7ApuntesV2();

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        calculoDePromedioDeResenasPor6Usuarios(listaDe8Usuarios);

        List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuarioId);

        Usuario[] usuariosEsperados = {listaDe8Usuarios.usuario3, listaDe8Usuarios.usuario5, listaDe8Usuarios.usuario6, listaDe8Usuarios.usuario2, listaDe8Usuarios.usuario4, listaDe8Usuarios.usuario7};
        assertArrayEquals(usuariosEsperados, usuariosDestacados.toArray());
    }

    @Test
    public void queNoSeMuestrenLosUsuariosDestacadosSiNoTienenCuatroOMasDePromedioDeResenas() {
        Long usuarioId = 1L;

        ListaDe8Usuarios listaDe8Usuarios = getListaDe8Usuarios();

        ListaDe7Apuntes listaDe7Apuntes = getListaDe7Apuntes();

        ListaDe8UsuarioApunteConTipoDeAccesoEditar listaDe8UsuarioApunteConTipoDeAccesoEditar = getListaDe8UsuarioApunteConTipoDeAccesoEditar(listaDe8Usuarios, listaDe7Apuntes);

        when(servicioUsuarioMock.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(usuarioId)).thenReturn(Arrays.asList(
                listaDe8Usuarios.usuario2,
                listaDe8Usuarios.usuario3,
                listaDe8Usuarios.usuario4,
                listaDe8Usuarios.usuario5,
                listaDe8Usuarios.usuario6,
                listaDe8Usuarios.usuario7,
                listaDe8Usuarios.usuario8
        ));

        obtenerApuntesPara7Usuarios(listaDe8UsuarioApunteConTipoDeAccesoEditar);

        obtenerMalasResenasPara7ApuntesV2();

        calculoDePromedioDe7Apuntes(listaDe7Apuntes);

        calculoDePromedioDeResenasPor6Usuarios(listaDe8Usuarios);

        List<Usuario> usuariosDestacados = servicioUsuarioApunteResena.obtenerUsuariosDestacados(usuarioId);

        Usuario[] usuariosEsperados = {listaDe8Usuarios.usuario3, listaDe8Usuarios.usuario4, listaDe8Usuarios.usuario7, listaDe8Usuarios.usuario8};
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

        ListaDe5ResenasBuenas listaDe5ResenasBuenas = getListaDe5ResenasBuenas();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(listaDe5ResenasBuenas.resena1, listaDe5ResenasBuenas.resena2, listaDe5ResenasBuenas.resena3, listaDe5ResenasBuenas.resena4, listaDe5ResenasBuenas.resena5));
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

        ListaDe5ResenasMalas listaDe5ResenasMalas = getListaDe5ResenasMalas();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(listaDe5ResenasMalas.resena1, listaDe5ResenasMalas.resena2, listaDe5ResenasMalas.resena3, listaDe5ResenasMalas.resena4, listaDe5ResenasMalas.resena5));
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

        ListaDe5ResenasBuenas listaDe5ResenasBuenas = getListaDe5ResenasBuenas();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(listaDe5ResenasBuenas.resena1, listaDe5ResenasBuenas.resena2, listaDe5ResenasBuenas.resena3, listaDe5ResenasBuenas.resena4, listaDe5ResenasBuenas.resena5));
        when(servicioUsuarioApunteMock.obtenerVendedorPorApunte(apunteId)).thenReturn(usuario);
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertTrue(resultado);
        assertEquals(200, usuario.getPuntos());

        Resena resena6 = new Resena();
        resena6.setCantidadDeEstrellas(5);

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(apunteId)).thenReturn(List.of(listaDe5ResenasBuenas.resena1, listaDe5ResenasBuenas.resena2, listaDe5ResenasBuenas.resena3, listaDe5ResenasBuenas.resena4, listaDe5ResenasBuenas.resena5, resena6));
        when(servicioApunteMock.obtenerPorId(apunteId)).thenReturn(apunte);

        boolean resultado2 = servicioUsuarioApunteResena.dar100PuntosAlUsuarioPorBuenasResenas(apunteId);

        assertFalse(resultado2);
        assertEquals(200, usuario.getPuntos());

    }

    @Test
    public void darle25PuntosAlUsuarioPorHaberSubido10Resenas() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());

        ListaDe10Resenas listaDe10Resenas = getListaDe10Resenas();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(listaDe10Resenas.resena1, listaDe10Resenas.resena2, listaDe10Resenas.resena3, listaDe10Resenas.resena4, listaDe10Resenas.resena5, listaDe10Resenas.resena6, listaDe10Resenas.resena7, listaDe10Resenas.resena8, listaDe10Resenas.resena9, listaDe10Resenas.resena10));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Assert.assertEquals(25, usuario.getPuntos());
    }
    @Test
    public void darle55PuntosAlUsuarioPorHaberSubido20Resenas() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());

        ListaDe20Resenas listaDe20Resenas = getListaDe20Resenas();

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(listaDe20Resenas.resena1, listaDe20Resenas.resena2, listaDe20Resenas.resena3, listaDe20Resenas.resena4, listaDe20Resenas.resena5, listaDe20Resenas.resena6, listaDe20Resenas.resena7, listaDe20Resenas.resena8, listaDe20Resenas.resena9, listaDe20Resenas.resena10));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);
        System.out.println("Banderas del Usuario: " + usuario.getFlagsDeParticipacionContinua());

        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(
                        listaDe20Resenas.resena1, listaDe20Resenas.resena2, listaDe20Resenas.resena3, listaDe20Resenas.resena4, listaDe20Resenas.resena5, listaDe20Resenas.resena6, listaDe20Resenas.resena7, listaDe20Resenas.resena8, listaDe20Resenas.resena9, listaDe20Resenas.resena10,
                        listaDe20Resenas.resena11, listaDe20Resenas.resena12, listaDe20Resenas.resena13, listaDe20Resenas.resena14, listaDe20Resenas.resena15, listaDe20Resenas.resena16, listaDe20Resenas.resena17, listaDe20Resenas.resena18, listaDe20Resenas.resena19, listaDe20Resenas.resena20
                ));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        System.out.println("Banderas del Usuario: " + usuario.getFlagsDeParticipacionContinua());
        Assert.assertEquals(55, usuario.getPuntos());
    }

    @Test
    public void darle35PuntosAlUsuarioPorHaberSubido10Apuntes() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());

        ListaDe10Apuntes listaDe10Apuntes = getListaDe10Apuntes();

        ListaDe10UsuarioApunteConSuApunte listaDe10UsuarioApunteConSuApunte = getListaDe10UsuarioApunteConSuApunte(usuario, listaDe10Apuntes);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(listaDe10UsuarioApunteConSuApunte.usuarioApunte1, listaDe10UsuarioApunteConSuApunte.usuarioApunte2, listaDe10UsuarioApunteConSuApunte.usuarioApunte3, listaDe10UsuarioApunteConSuApunte.usuarioApunte4, listaDe10UsuarioApunteConSuApunte.usuarioApunte5, listaDe10UsuarioApunteConSuApunte.usuarioApunte6, listaDe10UsuarioApunteConSuApunte.usuarioApunte7, listaDe10UsuarioApunteConSuApunte.usuarioApunte8, listaDe10UsuarioApunteConSuApunte.usuarioApunte9, listaDe10UsuarioApunteConSuApunte.usuarioApunte10));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Assert.assertEquals(35, usuario.getPuntos());
    }

    @Test
    public void darle80PuntosAlUsuarioPorHaberSubido20Apuntes() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());

        ListaDe20Apuntes listaDe20Apuntes = getListaDe20Apuntes();

        ListaDe20UsuarioApuntesConApunte listaDe20UsuarioApuntesConApunte = getListaDe20UsuarioApuntesConApunte(usuario, listaDe20Apuntes);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(listaDe20UsuarioApuntesConApunte.usuarioApunte1, listaDe20UsuarioApuntesConApunte.usuarioApunte2, listaDe20UsuarioApuntesConApunte.usuarioApunte3, listaDe20UsuarioApuntesConApunte.usuarioApunte4, listaDe20UsuarioApuntesConApunte.usuarioApunte5, listaDe20UsuarioApuntesConApunte.usuarioApunte6, listaDe20UsuarioApuntesConApunte.usuarioApunte7, listaDe20UsuarioApuntesConApunte.usuarioApunte8, listaDe20UsuarioApuntesConApunte.usuarioApunte9, listaDe20UsuarioApuntesConApunte.usuarioApunte10));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(listaDe20UsuarioApuntesConApunte.usuarioApunte1, listaDe20UsuarioApuntesConApunte.usuarioApunte2, listaDe20UsuarioApuntesConApunte.usuarioApunte3, listaDe20UsuarioApuntesConApunte.usuarioApunte4, listaDe20UsuarioApuntesConApunte.usuarioApunte5, listaDe20UsuarioApuntesConApunte.usuarioApunte6, listaDe20UsuarioApuntesConApunte.usuarioApunte7, listaDe20UsuarioApuntesConApunte.usuarioApunte8, listaDe20UsuarioApuntesConApunte.usuarioApunte9, listaDe20UsuarioApuntesConApunte.usuarioApunte10, listaDe20UsuarioApuntesConApunte.usuarioApunte11, listaDe20UsuarioApuntesConApunte.usuarioApunte12, listaDe20UsuarioApuntesConApunte.usuarioApunte13, listaDe20UsuarioApuntesConApunte.usuarioApunte14, listaDe20UsuarioApuntesConApunte.usuarioApunte15, listaDe20UsuarioApuntesConApunte.usuarioApunte16, listaDe20UsuarioApuntesConApunte.usuarioApunte17, listaDe20UsuarioApuntesConApunte.usuarioApunte18, listaDe20UsuarioApuntesConApunte.usuarioApunte19, listaDe20UsuarioApuntesConApunte.usuarioApunte20));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);

        Assert.assertEquals(80, usuario.getPuntos());
    }


    @Test
    public void noDarle35PuntosAlUsuarioPorHaberSubidoMenosDe5Apuntes() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());
        usuario.setPuntos(0);

        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        Apunte apunte3 = new Apunte();
        Apunte apunte4 = new Apunte();

        Obter4UsuarioApunteConTipoDeAccesoEditar obtener4UsuarioApunteConTipoDeAccesoEditar = getObter4UsuarioApunteConTipoDeAccesoEditar(apunte1, apunte2, apunte3, apunte4);

        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(usuario.getId()))
                .thenReturn(List.of(obtener4UsuarioApunteConTipoDeAccesoEditar.usuarioApunte1, obtener4UsuarioApunteConTipoDeAccesoEditar.usuarioApunte2, obtener4UsuarioApunteConTipoDeAccesoEditar.usuarioApunte3, obtener4UsuarioApunteConTipoDeAccesoEditar.usuarioApunte4));

        servicioUsuarioApunteResena.obtenerApuntesCreados(usuario);

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);
        Assert.assertEquals(0, usuario.getPuntos());
    }

    @Test
    public void noDarle25PuntosAlUsuarioPorHaberSubidoMenosDe5Resenas() {
        Usuario usuario = new Usuario();
        usuario.setFlagsDeParticipacionContinua(new HashSet<>());
        usuario.setPuntos(0);

        Resena resena1 = new Resena();
        Resena resena2 = new Resena();
        Resena resena3 = new Resena();
        Resena resena4 = new Resena();


        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdUsuario(usuario.getId()))
                .thenReturn(List.of(resena1, resena2, resena3, resena4));

        servicioUsuarioApunteResena.obtenerResenasPorIdDeUsuario(usuario.getId());

        servicioUsuarioApunteResena.darPuntosAlUsuarioPorParticipacionContinua(usuario);
        Assert.assertEquals(0, usuario.getPuntos());
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

    @Test
    public void queNoSePuedaDarUnaResenaSiYaDioUna(){
        Resena resenaMock1 = mock(Resena.class);
        Apunte apunteMock1 = mock(Apunte.class);
        Usuario usuarioMock1 = mock(Usuario.class);
        UsuarioApunteResena usuarioApunteResena = mock(UsuarioApunteResena.class);

        when(apunteMock1.getId()).thenReturn(1L);
        when(usuarioMock1.getId()).thenReturn(1L);
        when(usuarioApunteResena.getApunte()).thenReturn(apunteMock1);
        when(usuarioApunteResena.getUsuario()).thenReturn(usuarioMock1);
        when(usuarioApunteResena.getResena()).thenReturn(resenaMock1);

        List<UsuarioApunteResena> ls = new ArrayList<>();
        ls.add(new UsuarioApunteResena(usuarioMock1,resenaMock1,apunteMock1));

        when(repositorioUsuarioApunteResenaMock.existeResenaConApunteYUsuario(1L, 1L)).thenReturn(ls);

        boolean respuesta = servicioUsuarioApunteResena.existeResena(1L, 1L);

        assertTrue(respuesta);
        verify(repositorioUsuarioApunteResenaMock).existeResenaConApunteYUsuario(usuarioMock1.getId(), apunteMock1.getId());

    }










//METODOS PRIVADOS

    private static ListaDe10UsuarioApunteConSuApunte getListaDe10UsuarioApunteConSuApunte(Usuario usuario, ListaDe10Apuntes listaDe10Apuntes) {
        UsuarioApunte usuarioApunte1=new UsuarioApunte(usuario, listaDe10Apuntes.apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte2=new UsuarioApunte(usuario, listaDe10Apuntes.apunte2);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3=new UsuarioApunte(usuario, listaDe10Apuntes.apunte3);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4=new UsuarioApunte(usuario, listaDe10Apuntes.apunte4);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5=new UsuarioApunte(usuario, listaDe10Apuntes.apunte5);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte6=new UsuarioApunte(usuario, listaDe10Apuntes.apunte6);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7=new UsuarioApunte(usuario, listaDe10Apuntes.apunte7);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8=new UsuarioApunte(usuario, listaDe10Apuntes.apunte8);
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte9=new UsuarioApunte(usuario, listaDe10Apuntes.apunte9);
        usuarioApunte9.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte10=new UsuarioApunte(usuario, listaDe10Apuntes.apunte10);
        usuarioApunte10.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        ListaDe10UsuarioApunteConSuApunte listaDe10UsuarioApunteConSuApunte = new ListaDe10UsuarioApunteConSuApunte(usuarioApunte1, usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5, usuarioApunte6, usuarioApunte7, usuarioApunte8, usuarioApunte9, usuarioApunte10);
        return listaDe10UsuarioApunteConSuApunte;
    }

    private static class ListaDe10UsuarioApunteConSuApunte {
        public final UsuarioApunte usuarioApunte1;
        public final UsuarioApunte usuarioApunte2;
        public final UsuarioApunte usuarioApunte3;
        public final UsuarioApunte usuarioApunte4;
        public final UsuarioApunte usuarioApunte5;
        public final UsuarioApunte usuarioApunte6;
        public final UsuarioApunte usuarioApunte7;
        public final UsuarioApunte usuarioApunte8;
        public final UsuarioApunte usuarioApunte9;
        public final UsuarioApunte usuarioApunte10;

        public ListaDe10UsuarioApunteConSuApunte(UsuarioApunte usuarioApunte1, UsuarioApunte usuarioApunte2, UsuarioApunte usuarioApunte3, UsuarioApunte usuarioApunte4, UsuarioApunte usuarioApunte5, UsuarioApunte usuarioApunte6, UsuarioApunte usuarioApunte7, UsuarioApunte usuarioApunte8, UsuarioApunte usuarioApunte9, UsuarioApunte usuarioApunte10) {
            this.usuarioApunte1 = usuarioApunte1;
            this.usuarioApunte2 = usuarioApunte2;
            this.usuarioApunte3 = usuarioApunte3;
            this.usuarioApunte4 = usuarioApunte4;
            this.usuarioApunte5 = usuarioApunte5;
            this.usuarioApunte6 = usuarioApunte6;
            this.usuarioApunte7 = usuarioApunte7;
            this.usuarioApunte8 = usuarioApunte8;
            this.usuarioApunte9 = usuarioApunte9;
            this.usuarioApunte10 = usuarioApunte10;
        }
    }

    private static ListaDe10Apuntes getListaDe10Apuntes() {
        Apunte apunte1 = new Apunte();
        apunte1.setActivo(true);
        Apunte apunte2 = new Apunte();
        apunte2.setActivo(true);
        Apunte apunte3 = new Apunte();
        apunte3.setActivo(true);
        Apunte apunte4 = new Apunte();
        apunte4.setActivo(true);
        Apunte apunte5 = new Apunte();
        apunte5.setActivo(true);
        Apunte apunte6 = new Apunte();
        apunte6.setActivo(true);
        Apunte apunte7 = new Apunte();
        apunte7.setActivo(true);
        Apunte apunte8 = new Apunte();
        apunte8.setActivo(true);
        Apunte apunte9 = new Apunte();
        apunte9.setActivo(true);
        Apunte apunte10 = new Apunte();
        apunte10.setActivo(true);
        ListaDe10Apuntes listaDe10Apuntes = new ListaDe10Apuntes(apunte1, apunte2, apunte3, apunte4, apunte5, apunte6, apunte7, apunte8, apunte9, apunte10);
        return listaDe10Apuntes;
    }

    private static class ListaDe10Apuntes {
        public final Apunte apunte1;
        public final Apunte apunte2;
        public final Apunte apunte3;
        public final Apunte apunte4;
        public final Apunte apunte5;
        public final Apunte apunte6;
        public final Apunte apunte7;
        public final Apunte apunte8;
        public final Apunte apunte9;
        public final Apunte apunte10;

        public ListaDe10Apuntes(Apunte apunte1, Apunte apunte2, Apunte apunte3, Apunte apunte4, Apunte apunte5, Apunte apunte6, Apunte apunte7, Apunte apunte8, Apunte apunte9, Apunte apunte10) {
            this.apunte1 = apunte1;
            this.apunte2 = apunte2;
            this.apunte3 = apunte3;
            this.apunte4 = apunte4;
            this.apunte5 = apunte5;
            this.apunte6 = apunte6;
            this.apunte7 = apunte7;
            this.apunte8 = apunte8;
            this.apunte9 = apunte9;
            this.apunte10 = apunte10;
        }
    }

    private static ListaDe20UsuarioApuntesConApunte getListaDe20UsuarioApuntesConApunte(Usuario usuario, ListaDe20Apuntes listaDe20Apuntes) {
        UsuarioApunte usuarioApunte1=new UsuarioApunte(usuario, listaDe20Apuntes.apunte1);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte2=new UsuarioApunte(usuario, listaDe20Apuntes.apunte2);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3=new UsuarioApunte(usuario, listaDe20Apuntes.apunte3);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4=new UsuarioApunte(usuario, listaDe20Apuntes.apunte4);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5=new UsuarioApunte(usuario, listaDe20Apuntes.apunte5);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte6=new UsuarioApunte(usuario, listaDe20Apuntes.apunte6);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7=new UsuarioApunte(usuario, listaDe20Apuntes.apunte7);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8=new UsuarioApunte(usuario, listaDe20Apuntes.apunte8);
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte9=new UsuarioApunte(usuario, listaDe20Apuntes.apunte9);
        usuarioApunte9.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte10=new UsuarioApunte(usuario, listaDe20Apuntes.apunte10);
        usuarioApunte10.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte11=new UsuarioApunte(usuario, listaDe20Apuntes.apunte11);
        usuarioApunte11.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte12=new UsuarioApunte(usuario, listaDe20Apuntes.apunte12);
        usuarioApunte12.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte13=new UsuarioApunte(usuario, listaDe20Apuntes.apunte13);
        usuarioApunte13.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte14=new UsuarioApunte(usuario, listaDe20Apuntes.apunte14);
        usuarioApunte14.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte15=new UsuarioApunte(usuario, listaDe20Apuntes.apunte15);
        usuarioApunte15.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte16=new UsuarioApunte(usuario, listaDe20Apuntes.apunte16);
        usuarioApunte16.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte17=new UsuarioApunte(usuario, listaDe20Apuntes.apunte17);
        usuarioApunte17.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte18=new UsuarioApunte(usuario, listaDe20Apuntes.apunte18);
        usuarioApunte18.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte19=new UsuarioApunte(usuario, listaDe20Apuntes.apunte19);
        usuarioApunte19.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte20=new UsuarioApunte(usuario, listaDe20Apuntes.apunte20);
        usuarioApunte20.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        ListaDe20UsuarioApuntesConApunte listaDe20UsuarioApuntesConApunte = new ListaDe20UsuarioApuntesConApunte(usuarioApunte1, usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5, usuarioApunte6, usuarioApunte7, usuarioApunte8, usuarioApunte9, usuarioApunte10, usuarioApunte11, usuarioApunte12, usuarioApunte13, usuarioApunte14, usuarioApunte15, usuarioApunte16, usuarioApunte17, usuarioApunte18, usuarioApunte19, usuarioApunte20);
        return listaDe20UsuarioApuntesConApunte;
    }

    private static class ListaDe20UsuarioApuntesConApunte {
        public final UsuarioApunte usuarioApunte1;
        public final UsuarioApunte usuarioApunte2;
        public final UsuarioApunte usuarioApunte3;
        public final UsuarioApunte usuarioApunte4;
        public final UsuarioApunte usuarioApunte5;
        public final UsuarioApunte usuarioApunte6;
        public final UsuarioApunte usuarioApunte7;
        public final UsuarioApunte usuarioApunte8;
        public final UsuarioApunte usuarioApunte9;
        public final UsuarioApunte usuarioApunte10;
        public final UsuarioApunte usuarioApunte11;
        public final UsuarioApunte usuarioApunte12;
        public final UsuarioApunte usuarioApunte13;
        public final UsuarioApunte usuarioApunte14;
        public final UsuarioApunte usuarioApunte15;
        public final UsuarioApunte usuarioApunte16;
        public final UsuarioApunte usuarioApunte17;
        public final UsuarioApunte usuarioApunte18;
        public final UsuarioApunte usuarioApunte19;
        public final UsuarioApunte usuarioApunte20;

        public ListaDe20UsuarioApuntesConApunte(UsuarioApunte usuarioApunte1, UsuarioApunte usuarioApunte2, UsuarioApunte usuarioApunte3, UsuarioApunte usuarioApunte4, UsuarioApunte usuarioApunte5, UsuarioApunte usuarioApunte6, UsuarioApunte usuarioApunte7, UsuarioApunte usuarioApunte8, UsuarioApunte usuarioApunte9, UsuarioApunte usuarioApunte10, UsuarioApunte usuarioApunte11, UsuarioApunte usuarioApunte12, UsuarioApunte usuarioApunte13, UsuarioApunte usuarioApunte14, UsuarioApunte usuarioApunte15, UsuarioApunte usuarioApunte16, UsuarioApunte usuarioApunte17, UsuarioApunte usuarioApunte18, UsuarioApunte usuarioApunte19, UsuarioApunte usuarioApunte20) {
            this.usuarioApunte1 = usuarioApunte1;
            this.usuarioApunte2 = usuarioApunte2;
            this.usuarioApunte3 = usuarioApunte3;
            this.usuarioApunte4 = usuarioApunte4;
            this.usuarioApunte5 = usuarioApunte5;
            this.usuarioApunte6 = usuarioApunte6;
            this.usuarioApunte7 = usuarioApunte7;
            this.usuarioApunte8 = usuarioApunte8;
            this.usuarioApunte9 = usuarioApunte9;
            this.usuarioApunte10 = usuarioApunte10;
            this.usuarioApunte11 = usuarioApunte11;
            this.usuarioApunte12 = usuarioApunte12;
            this.usuarioApunte13 = usuarioApunte13;
            this.usuarioApunte14 = usuarioApunte14;
            this.usuarioApunte15 = usuarioApunte15;
            this.usuarioApunte16 = usuarioApunte16;
            this.usuarioApunte17 = usuarioApunte17;
            this.usuarioApunte18 = usuarioApunte18;
            this.usuarioApunte19 = usuarioApunte19;
            this.usuarioApunte20 = usuarioApunte20;
        }
    }

    private static ListaDe20Apuntes getListaDe20Apuntes() {
        Apunte apunte1 = new Apunte();
        apunte1.setActivo(true);
        Apunte apunte2 = new Apunte();
        apunte2.setActivo(true);
        Apunte apunte3 = new Apunte();
        apunte3.setActivo(true);
        Apunte apunte4 = new Apunte();
        apunte4.setActivo(true);
        Apunte apunte5 = new Apunte();
        apunte5.setActivo(true);
        Apunte apunte6 = new Apunte();
        apunte6.setActivo(true);
        Apunte apunte7 = new Apunte();
        apunte7.setActivo(true);
        Apunte apunte8 = new Apunte();
        apunte8.setActivo(true);
        Apunte apunte9 = new Apunte();
        apunte9.setActivo(true);
        Apunte apunte10 = new Apunte();
        apunte10.setActivo(true);
        Apunte apunte11 = new Apunte();
        apunte11.setActivo(true);
        Apunte apunte12 = new Apunte();
        apunte12.setActivo(true);
        Apunte apunte13 = new Apunte();
        apunte13.setActivo(true);
        Apunte apunte14 = new Apunte();
        apunte14.setActivo(true);
        Apunte apunte15 = new Apunte();
        apunte15.setActivo(true);
        Apunte apunte16 = new Apunte();
        apunte16.setActivo(true);
        Apunte apunte17 = new Apunte();
        apunte17.setActivo(true);
        Apunte apunte18 = new Apunte();
        apunte18.setActivo(true);
        Apunte apunte19 = new Apunte();
        apunte19.setActivo(true);
        Apunte apunte20 = new Apunte();
        apunte20.setActivo(true);
        ListaDe20Apuntes listaDe20Apuntes = new ListaDe20Apuntes(apunte1, apunte2, apunte3, apunte4, apunte5, apunte6, apunte7, apunte8, apunte9, apunte10, apunte11, apunte12, apunte13, apunte14, apunte15, apunte16, apunte17, apunte18, apunte19, apunte20);
        return listaDe20Apuntes;
    }

    private static class ListaDe20Apuntes {
        public final Apunte apunte1;
        public final Apunte apunte2;
        public final Apunte apunte3;
        public final Apunte apunte4;
        public final Apunte apunte5;
        public final Apunte apunte6;
        public final Apunte apunte7;
        public final Apunte apunte8;
        public final Apunte apunte9;
        public final Apunte apunte10;
        public final Apunte apunte11;
        public final Apunte apunte12;
        public final Apunte apunte13;
        public final Apunte apunte14;
        public final Apunte apunte15;
        public final Apunte apunte16;
        public final Apunte apunte17;
        public final Apunte apunte18;
        public final Apunte apunte19;
        public final Apunte apunte20;

        public ListaDe20Apuntes(Apunte apunte1, Apunte apunte2, Apunte apunte3, Apunte apunte4, Apunte apunte5, Apunte apunte6, Apunte apunte7, Apunte apunte8, Apunte apunte9, Apunte apunte10, Apunte apunte11, Apunte apunte12, Apunte apunte13, Apunte apunte14, Apunte apunte15, Apunte apunte16, Apunte apunte17, Apunte apunte18, Apunte apunte19, Apunte apunte20) {
            this.apunte1 = apunte1;
            this.apunte2 = apunte2;
            this.apunte3 = apunte3;
            this.apunte4 = apunte4;
            this.apunte5 = apunte5;
            this.apunte6 = apunte6;
            this.apunte7 = apunte7;
            this.apunte8 = apunte8;
            this.apunte9 = apunte9;
            this.apunte10 = apunte10;
            this.apunte11 = apunte11;
            this.apunte12 = apunte12;
            this.apunte13 = apunte13;
            this.apunte14 = apunte14;
            this.apunte15 = apunte15;
            this.apunte16 = apunte16;
            this.apunte17 = apunte17;
            this.apunte18 = apunte18;
            this.apunte19 = apunte19;
            this.apunte20 = apunte20;
        }
    }
    private static ListaDe20Resenas getListaDe20Resenas() {
        Resena resena1 = new Resena();
        Resena resena2 = new Resena();
        Resena resena3 = new Resena();
        Resena resena4 = new Resena();
        Resena resena5 = new Resena();
        Resena resena6 = new Resena();
        Resena resena7 = new Resena();
        Resena resena8 = new Resena();
        Resena resena9 = new Resena();
        Resena resena10 = new Resena();
        Resena resena11 = new Resena();
        Resena resena12 = new Resena();
        Resena resena13 = new Resena();
        Resena resena14 = new Resena();
        Resena resena15 = new Resena();
        Resena resena16 = new Resena();
        Resena resena17 = new Resena();
        Resena resena18 = new Resena();
        Resena resena19 = new Resena();
        Resena resena20 = new Resena();
        ListaDe20Resenas listaDe20Resenas = new ListaDe20Resenas(resena1, resena2, resena3, resena4, resena5, resena6, resena7, resena8, resena9, resena10, resena11, resena12, resena13, resena14, resena15, resena16, resena17, resena18, resena19, resena20);
        return listaDe20Resenas;
    }

    private static class ListaDe20Resenas {
        public final Resena resena1;
        public final Resena resena2;
        public final Resena resena3;
        public final Resena resena4;
        public final Resena resena5;
        public final Resena resena6;
        public final Resena resena7;
        public final Resena resena8;
        public final Resena resena9;
        public final Resena resena10;
        public final Resena resena11;
        public final Resena resena12;
        public final Resena resena13;
        public final Resena resena14;
        public final Resena resena15;
        public final Resena resena16;
        public final Resena resena17;
        public final Resena resena18;
        public final Resena resena19;
        public final Resena resena20;

        public ListaDe20Resenas(Resena resena1, Resena resena2, Resena resena3, Resena resena4, Resena resena5, Resena resena6, Resena resena7, Resena resena8, Resena resena9, Resena resena10, Resena resena11, Resena resena12, Resena resena13, Resena resena14, Resena resena15, Resena resena16, Resena resena17, Resena resena18, Resena resena19, Resena resena20) {
            this.resena1 = resena1;
            this.resena2 = resena2;
            this.resena3 = resena3;
            this.resena4 = resena4;
            this.resena5 = resena5;
            this.resena6 = resena6;
            this.resena7 = resena7;
            this.resena8 = resena8;
            this.resena9 = resena9;
            this.resena10 = resena10;
            this.resena11 = resena11;
            this.resena12 = resena12;
            this.resena13 = resena13;
            this.resena14 = resena14;
            this.resena15 = resena15;
            this.resena16 = resena16;
            this.resena17 = resena17;
            this.resena18 = resena18;
            this.resena19 = resena19;
            this.resena20 = resena20;
        }
    }
    private static class ListaDe10Resenas {
        public final Resena resena1;
        public final Resena resena2;
        public final Resena resena3;
        public final Resena resena4;
        public final Resena resena5;
        public final Resena resena6;
        public final Resena resena7;
        public final Resena resena8;
        public final Resena resena9;
        public final Resena resena10;

        public ListaDe10Resenas(Resena resena1, Resena resena2, Resena resena3, Resena resena4, Resena resena5, Resena resena6, Resena resena7, Resena resena8, Resena resena9, Resena resena10) {
            this.resena1 = resena1;
            this.resena2 = resena2;
            this.resena3 = resena3;
            this.resena4 = resena4;
            this.resena5 = resena5;
            this.resena6 = resena6;
            this.resena7 = resena7;
            this.resena8 = resena8;
            this.resena9 = resena9;
            this.resena10 = resena10;
        }
    }
    private static ListaDe10Resenas getListaDe10Resenas() {
        Resena resena1 = new Resena();
        Resena resena2 = new Resena();
        Resena resena3 = new Resena();
        Resena resena4 = new Resena();
        Resena resena5 = new Resena();
        Resena resena6 = new Resena();
        Resena resena7 = new Resena();
        Resena resena8 = new Resena();
        Resena resena9 = new Resena();
        Resena resena10 = new Resena();
        ListaDe10Resenas listaDe10Resenas = new ListaDe10Resenas(resena1, resena2, resena3, resena4, resena5, resena6, resena7, resena8, resena9, resena10);
        return listaDe10Resenas;
    }

    private void  calculoDePromedioDe7Apuntes(ListaDe7Apuntes listaDe7Apuntes) {
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte1.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte2.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte3.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte4.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte5.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte6.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenas(listaDe7Apuntes.apunte7.getId());
    }

    private void resenasMalasSimuladasPara7Apuntes() {
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L))
                .thenReturn(List.of(new Resena(0), new Resena(2), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L))
                .thenReturn(List.of(new Resena(4), new Resena(1), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(3L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(4L))
                .thenReturn(List.of(new Resena(1), new Resena(3), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(5L))
                .thenReturn(List.of(new Resena(1), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(6L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(7L))
                .thenReturn(List.of(new Resena(1), new Resena(4), new Resena(4)));
    }

    private static ListaDe7Apuntes getListaDe7Apuntes() {
        Apunte apunte1 = new Apunte();
        apunte1.setId(1L);
        apunte1.setActivo(true);
        Apunte apunte2 = new Apunte();
        apunte2.setId(2L);
        apunte2.setActivo(true);
        Apunte apunte3 = new Apunte();
        apunte3.setId(3L);
        apunte3.setActivo(true);
        Apunte apunte4 = new Apunte();
        apunte4.setId(4L);
        apunte4.setActivo(true);
        Apunte apunte5 = new Apunte();
        apunte5.setId(5L);
        apunte5.setActivo(true);
        Apunte apunte6 = new Apunte();
        apunte6.setId(6L);
        apunte6.setActivo(true);
        Apunte apunte7 = new Apunte();
        apunte7.setId(7L);
        apunte7.setActivo(true);
        ListaDe7Apuntes listaDe7Apuntes = new ListaDe7Apuntes(apunte1, apunte2, apunte3, apunte4, apunte5, apunte6, apunte7);
        return listaDe7Apuntes;
    }

    private static class ListaDe7Apuntes {
        public final Apunte apunte1;
        public final Apunte apunte2;
        public final Apunte apunte3;
        public final Apunte apunte4;
        public final Apunte apunte5;
        public final Apunte apunte6;
        public final Apunte apunte7;

        public ListaDe7Apuntes(Apunte apunte1, Apunte apunte2, Apunte apunte3, Apunte apunte4, Apunte apunte5, Apunte apunte6, Apunte apunte7) {
            this.apunte1 = apunte1;
            this.apunte2 = apunte2;
            this.apunte3 = apunte3;
            this.apunte4 = apunte4;
            this.apunte5 = apunte5;
            this.apunte6 = apunte6;
            this.apunte7 = apunte7;
        }
    }
    private void resenasBuenasSimuladasPara7Apuntes() {
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
    }

    private static ListaDe8UsuarioApunteConTipoDeAccesoEditar getListaDe8UsuarioApunteConTipoDeAccesoEditar(ListaDe8Usuarios listaDe8Usuarios, ListaDe7Apuntes listaDe7Apuntes) {
        UsuarioApunte usuarioApunte2 = new UsuarioApunte(listaDe8Usuarios.usuario2, listaDe7Apuntes.apunte1);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte(listaDe8Usuarios.usuario3, listaDe7Apuntes.apunte2);
        usuarioApunte3.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte4 = new UsuarioApunte(listaDe8Usuarios.usuario4, listaDe7Apuntes.apunte3);
        usuarioApunte4.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte5 = new UsuarioApunte(listaDe8Usuarios.usuario5, listaDe7Apuntes.apunte4);
        usuarioApunte5.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte6 = new UsuarioApunte(listaDe8Usuarios.usuario6, listaDe7Apuntes.apunte5);
        usuarioApunte6.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte7 = new UsuarioApunte(listaDe8Usuarios.usuario7, listaDe7Apuntes.apunte6);
        usuarioApunte7.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        UsuarioApunte usuarioApunte8 = new UsuarioApunte(listaDe8Usuarios.usuario8, listaDe7Apuntes.apunte7);
        usuarioApunte8.setTipoDeAcceso(TipoDeAcceso.EDITAR);
        ListaDe8UsuarioApunteConTipoDeAccesoEditar listaDe8UsuarioApunteConTipoDeAccesoEditar = new ListaDe8UsuarioApunteConTipoDeAccesoEditar(usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5, usuarioApunte6, usuarioApunte7, usuarioApunte8);
        return listaDe8UsuarioApunteConTipoDeAccesoEditar;
    }

    private static class ListaDe8UsuarioApunteConTipoDeAccesoEditar {
        public final UsuarioApunte usuarioApunte2;
        public final UsuarioApunte usuarioApunte3;
        public final UsuarioApunte usuarioApunte4;
        public final UsuarioApunte usuarioApunte5;
        public final UsuarioApunte usuarioApunte6;
        public final UsuarioApunte usuarioApunte7;
        public final UsuarioApunte usuarioApunte8;

        public ListaDe8UsuarioApunteConTipoDeAccesoEditar(UsuarioApunte usuarioApunte2, UsuarioApunte usuarioApunte3, UsuarioApunte usuarioApunte4, UsuarioApunte usuarioApunte5, UsuarioApunte usuarioApunte6, UsuarioApunte usuarioApunte7, UsuarioApunte usuarioApunte8) {
            this.usuarioApunte2 = usuarioApunte2;
            this.usuarioApunte3 = usuarioApunte3;
            this.usuarioApunte4 = usuarioApunte4;
            this.usuarioApunte5 = usuarioApunte5;
            this.usuarioApunte6 = usuarioApunte6;
            this.usuarioApunte7 = usuarioApunte7;
            this.usuarioApunte8 = usuarioApunte8;
        }
    }

    private static ListaDe8Usuarios getListaDe8Usuarios() {
        Usuario usuario1 = new Usuario(1L);
        Usuario usuario2 = new Usuario(2L);
        Usuario usuario3 = new Usuario(3L);
        Usuario usuario4 = new Usuario(4L);
        Usuario usuario5 = new Usuario(5L);
        Usuario usuario6 = new Usuario(6L);
        Usuario usuario7 = new Usuario(7L);
        Usuario usuario8 = new Usuario(8L);
        ListaDe8Usuarios listaDe8Usuarios = new ListaDe8Usuarios(usuario2, usuario3, usuario4, usuario5, usuario6, usuario7, usuario8);
        return listaDe8Usuarios;
    }

    private static class ListaDe8Usuarios {
        public final Usuario usuario2;
        public final Usuario usuario3;
        public final Usuario usuario4;
        public final Usuario usuario5;
        public final Usuario usuario6;
        public final Usuario usuario7;
        public final Usuario usuario8;

        public ListaDe8Usuarios(Usuario usuario2, Usuario usuario3, Usuario usuario4, Usuario usuario5, Usuario usuario6, Usuario usuario7, Usuario usuario8) {
            this.usuario2 = usuario2;
            this.usuario3 = usuario3;
            this.usuario4 = usuario4;
            this.usuario5 = usuario5;
            this.usuario6 = usuario6;
            this.usuario7 = usuario7;
            this.usuario8 = usuario8;
        }
    }

    private void calculoDePromedioDeResenasPor6Usuarios(ListaDe8Usuarios listaDe8Usuarios) {
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario2.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario3.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario4.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario5.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario6.getId());
        servicioUsuarioApunteResena.calcularPromedioPuntajeResenasPorUsuario(listaDe8Usuarios.usuario7.getId());
    }

    private void obtenerResenasBuenasPara7ApuntesV2() {
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
    }

    private void obtenerApuntesPara7Usuarios(ListaDe8UsuarioApunteConTipoDeAccesoEditar listaDe8UsuarioApunteConTipoDeAccesoEditar) {
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(2L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte2));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(3L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte3));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(4L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte4));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(5L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte5));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(6L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte6));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(7L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte7));
        when(servicioUsuarioApunteMock.obtenerApuntesPorUsuario(8L))
                .thenReturn(List.of(listaDe8UsuarioApunteConTipoDeAccesoEditar.usuarioApunte8));
    }

    private void obtenerMalasResenasPara7ApuntesV2() {
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(1L))
                .thenReturn(List.of(new Resena(1), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(2L))
                .thenReturn(List.of(new Resena(5), new Resena(5), new Resena(5)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(3L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(4L))
                .thenReturn(List.of(new Resena(0), new Resena(1), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(5L))
                .thenReturn(List.of(new Resena(2), new Resena(4), new Resena(0)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(6L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
        when(repositorioUsuarioApunteResenaMock.obtenerResenasPorIdApunte(7L))
                .thenReturn(List.of(new Resena(4), new Resena(4), new Resena(4)));
    }

    private static ListaDe5ResenasBuenas getListaDe5ResenasBuenas() {
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
        ListaDe5ResenasBuenas listaDe5ResenasBuenas = new ListaDe5ResenasBuenas(resena1, resena2, resena3, resena4, resena5);
        return listaDe5ResenasBuenas;
    }

    private static class ListaDe5ResenasBuenas {
        public final Resena resena1;
        public final Resena resena2;
        public final Resena resena3;
        public final Resena resena4;
        public final Resena resena5;

        public ListaDe5ResenasBuenas(Resena resena1, Resena resena2, Resena resena3, Resena resena4, Resena resena5) {
            this.resena1 = resena1;
            this.resena2 = resena2;
            this.resena3 = resena3;
            this.resena4 = resena4;
            this.resena5 = resena5;
        }
    }

    private static ListaDe5ResenasMalas getListaDe5ResenasMalas() {
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
        ListaDe5ResenasMalas listaDe5ResenasMalas = new ListaDe5ResenasMalas(resena1, resena2, resena3, resena4, resena5);
        return listaDe5ResenasMalas;
    }

    private static class ListaDe5ResenasMalas {
        public final Resena resena1;
        public final Resena resena2;
        public final Resena resena3;
        public final Resena resena4;
        public final Resena resena5;

        public ListaDe5ResenasMalas(Resena resena1, Resena resena2, Resena resena3, Resena resena4, Resena resena5) {
            this.resena1 = resena1;
            this.resena2 = resena2;
            this.resena3 = resena3;
            this.resena4 = resena4;
            this.resena5 = resena5;
        }
    }

    private static ObtenerDe6A10UsuarioApunte getObtenerDe6A10UsuarioApunte(Apunte apunte6, Apunte apunte7, Apunte apunte8, Apunte apunte9, Apunte apunte10) {
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
        ObtenerDe6A10UsuarioApunte obtenerDe6A10UsuarioApunte = new ObtenerDe6A10UsuarioApunte(usuarioApunte6, usuarioApunte7, usuarioApunte8, usuarioApunte9, usuarioApunte10);
        return obtenerDe6A10UsuarioApunte;
    }

    private static class ObtenerDe6A10UsuarioApunte {
        public final UsuarioApunte usuarioApunte6;
        public final UsuarioApunte usuarioApunte7;
        public final UsuarioApunte usuarioApunte8;
        public final UsuarioApunte usuarioApunte9;
        public final UsuarioApunte usuarioApunte10;

        public ObtenerDe6A10UsuarioApunte(UsuarioApunte usuarioApunte6, UsuarioApunte usuarioApunte7, UsuarioApunte usuarioApunte8, UsuarioApunte usuarioApunte9, UsuarioApunte usuarioApunte10) {
            this.usuarioApunte6 = usuarioApunte6;
            this.usuarioApunte7 = usuarioApunte7;
            this.usuarioApunte8 = usuarioApunte8;
            this.usuarioApunte9 = usuarioApunte9;
            this.usuarioApunte10 = usuarioApunte10;
        }
    }

    private static ObtenerDe1A5UsuarioApunte getObtenerDe1A5UsuarioApunte(Apunte apunte1, Apunte apunte2, Apunte apunte3, Apunte apunte4, Apunte apunte5) {
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
        ObtenerDe1A5UsuarioApunte obtenerDe1A5UsuarioApunte = new ObtenerDe1A5UsuarioApunte(usuarioApunte1, usuarioApunte2, usuarioApunte3, usuarioApunte4, usuarioApunte5);
        return obtenerDe1A5UsuarioApunte;
    }

    private static class ObtenerDe1A5UsuarioApunte {
        public final UsuarioApunte usuarioApunte1;
        public final UsuarioApunte usuarioApunte2;
        public final UsuarioApunte usuarioApunte3;
        public final UsuarioApunte usuarioApunte4;
        public final UsuarioApunte usuarioApunte5;

        public ObtenerDe1A5UsuarioApunte(UsuarioApunte usuarioApunte1, UsuarioApunte usuarioApunte2, UsuarioApunte usuarioApunte3, UsuarioApunte usuarioApunte4, UsuarioApunte usuarioApunte5) {
            this.usuarioApunte1 = usuarioApunte1;
            this.usuarioApunte2 = usuarioApunte2;
            this.usuarioApunte3 = usuarioApunte3;
            this.usuarioApunte4 = usuarioApunte4;
            this.usuarioApunte5 = usuarioApunte5;
        }
    }

    private static class Obter4UsuarioApunteConTipoDeAccesoEditar {
        public final UsuarioApunte usuarioApunte1;
        public final UsuarioApunte usuarioApunte2;
        public final UsuarioApunte usuarioApunte3;
        public final UsuarioApunte usuarioApunte4;

        public Obter4UsuarioApunteConTipoDeAccesoEditar(UsuarioApunte usuarioApunte1, UsuarioApunte usuarioApunte2, UsuarioApunte usuarioApunte3, UsuarioApunte usuarioApunte4) {
            this.usuarioApunte1 = usuarioApunte1;
            this.usuarioApunte2 = usuarioApunte2;
            this.usuarioApunte3 = usuarioApunte3;
            this.usuarioApunte4 = usuarioApunte4;
        }
    }

    private static Obter4UsuarioApunteConTipoDeAccesoEditar getObter4UsuarioApunteConTipoDeAccesoEditar(Apunte apunte1, Apunte apunte2, Apunte apunte3, Apunte apunte4) {
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
        Obter4UsuarioApunteConTipoDeAccesoEditar obtener4UsuarioApunteConTipoDeAccesoEditar = new Obter4UsuarioApunteConTipoDeAccesoEditar(usuarioApunte1, usuarioApunte2, usuarioApunte3, usuarioApunte4);
        return obtener4UsuarioApunteConTipoDeAccesoEditar;
    }
}






