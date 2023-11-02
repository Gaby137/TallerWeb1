package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


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
    public void testObtenerApuntesDeOtrosUsuarios() {
        // Crear tres usuarios y sus apuntes
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        Usuario usuario3 = new Usuario();
        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        Apunte apunte3 = new Apunte();

        UsuarioApunte usuarioApunte1 = new UsuarioApunte(usuario1, apunte1);
        UsuarioApunte usuarioApunte2 = new UsuarioApunte(usuario2, apunte2);
        UsuarioApunte usuarioApunte3 = new UsuarioApunte(usuario3, apunte3);

        // Registrar los usuarios y sus apuntes en la base de datos
        repositorioUsuarioApunte.registrar(usuarioApunte1);
        repositorioUsuarioApunte.registrar(usuarioApunte2);
        repositorioUsuarioApunte.registrar(usuarioApunte3);

        // Obtener apuntes de otros usuarios para usuario1
        List<UsuarioApunte> apuntesOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(usuario1.getId());

        // Verificar que la lista no contiene el apunte de usuario1
        for (UsuarioApunte usuarioApunte : apuntesOtrosUsuarios) {
            assertNotEquals(usuario1.getId(), usuarioApunte.getUsuario().getId());
        }

        // Verificar que la lista contiene los apuntes de usuario2 y usuario3
        assertTrue(apuntesOtrosUsuarios.stream().anyMatch(ua -> Objects.equals(ua.getUsuario().getId(), usuario2.getId())));
        assertTrue(apuntesOtrosUsuarios.stream().anyMatch(ua -> Objects.equals(ua.getUsuario().getId(), usuario3.getId())));

        // Verificar que la lista tiene el tamaño correcto
        assertEquals(2, apuntesOtrosUsuarios.size());
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
}




