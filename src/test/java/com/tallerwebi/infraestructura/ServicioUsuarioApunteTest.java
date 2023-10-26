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

        @BeforeEach
        public void init() {
            servicioUsuarioMock = mock(ServicioUsuario.class);
            repositorioUsuarioApunteMock = mock(RepositorioUsuarioApunte.class);
            requestMock = mock(HttpServletRequest.class);
            sessionMock = mock(HttpSession.class);
            servicioUsuarioApunte = new ServicioUsuarioApunteImpl(repositorioUsuarioApunteMock, servicioUsuarioMock);  // Pasar el servicioUsuarioMock como argumento
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

    }



