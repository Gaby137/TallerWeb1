package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import com.tallerwebi.dominio.servicio.ServicioResenaImpl;
import com.tallerwebi.dominio.servicio.ServicioUsuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioApunteTest {

    @Autowired
    private RepositorioUsuarioApunteImpl repositorioUsuarioApunte;

    @Transactional
    @Rollback
    @Test
    public void alQuererObtenerUnApunteDeUnUsuarioPorSuIdQueSeEncuentreAlUsuario() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        UsuarioApunte usuarioApunte = new UsuarioApunte(usuario, apunte);

        repositorioUsuarioApunte.registrar(usuarioApunte);

        List<UsuarioApunte> resultado = repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(usuario.getId());
        assertEquals(1, resultado.size());
        assertEquals(usuario.getId(), resultado.get(0).getUsuario().getId());
    }

    @Transactional
    @Rollback
    @Test
    public void queAlObtenerApuntesDeOtrosUsuariosNoObtengaElDelUsuarioQueLosSolicita() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        Apunte apunte3 = new Apunte();

        UsuarioApunte usuarioApunte1 = new UsuarioApunte(usuario1, apunte1);
        UsuarioApunte usuarioApunte2 = new UsuarioApunte(usuario2, apunte2);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte(usuario3, apunte3);

        repositorioUsuarioApunte.registrar(usuarioApunte1);
        repositorioUsuarioApunte.registrar(usuarioApunte2);
        repositorioUsuarioApunte.registrar(usuarioApunte3);

        List<UsuarioApunte> apuntesOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuario1.getId());

        for (UsuarioApunte usuarioApunte : apuntesOtrosUsuarios) {
            assertNotEquals(usuario1.getId(), usuarioApunte.getUsuario().getId());
        }
        
    }
    @Transactional
    @Rollback
    @Test
    public void obtenerTipoDeAccesoPorIdsDeUsuarioYApunte() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();

        TipoDeAcceso tipoEsperado = TipoDeAcceso.EDITAR;
        UsuarioApunte usuarioApunte = new UsuarioApunte();
        usuarioApunte.setUsuario(usuario);
        usuarioApunte.setApunte(apunte);
        usuarioApunte.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        repositorioUsuarioApunte.registrar(usuarioApunte);

        TipoDeAcceso tipoObtenido = repositorioUsuarioApunte.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(usuario.getId(), apunte.getId());

        assertEquals(tipoEsperado, tipoObtenido);
    }

    @Transactional
    @Rollback
    @Test
    public void queElimineLaRelacionUsuarioApuntePorSuId() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();

        UsuarioApunte usuarioApunte = new UsuarioApunte();
        usuarioApunte.setUsuario(usuario);
        usuarioApunte.setApunte(apunte);
        usuarioApunte.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        repositorioUsuarioApunte.registrar(usuarioApunte);

        Long usuarioApunteId = usuarioApunte.getId();

        repositorioUsuarioApunte.eliminarRelacionUsuarioApuntePorId(usuarioApunteId);

        List<UsuarioApunte> usuarioApuntesBuscados = repositorioUsuarioApunte.obtenerRelacionesUsuarioApuntePorIdDeApunte(apunte.getId());

        assertTrue(usuarioApuntesBuscados.isEmpty());
    }

    @Transactional
    @Rollback
    @Test
    public void obtenerLaRelacionUsuarioApuntePorElIdDelApunte(){
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();


        UsuarioApunte usuarioApunte1 = new UsuarioApunte();
        usuarioApunte1.setUsuario(usuario);
        usuarioApunte1.setApunte(apunte);
        usuarioApunte1.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        repositorioUsuarioApunte.registrar(usuarioApunte1);

        UsuarioApunte usuarioApunte2 = new UsuarioApunte();
        usuarioApunte2.setUsuario(usuario);
        usuarioApunte2.setApunte(apunte);
        usuarioApunte2.setTipoDeAcceso(TipoDeAcceso.LEER);

        repositorioUsuarioApunte.registrar(usuarioApunte2);

        List<UsuarioApunte> usuarioApuntesBuscados = repositorioUsuarioApunte.obtenerRelacionesUsuarioApuntePorIdDeApunte(apunte.getId());

        assertEquals(2, usuarioApuntesBuscados.size());

    }
}




