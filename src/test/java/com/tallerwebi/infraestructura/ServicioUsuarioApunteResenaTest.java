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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioApunteResenaTest {

    private ServicioUsuarioApunteResenaImpl servicioUsuarioApunteResena;
    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResenaMock;
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


}






