package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension .class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioApunteResenaTest {

        @Autowired
        private RepositorioUsuarioApunteResenaImpl repositorioUsuarioApunteResena;


    @Transactional
    @Rollback
    @Test
    public void guardarYObtenerResenasPorIdApunte() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena1 = new Resena();
        Resena resena2 = new Resena();

        UsuarioApunteResena usuarioApunteResena1 = new UsuarioApunteResena(usuario, resena1, apunte);
        UsuarioApunteResena usuarioApunteResena2 = new UsuarioApunteResena(usuario, resena2, apunte);

        repositorioUsuarioApunteResena.guardar(usuarioApunteResena1);
        repositorioUsuarioApunteResena.guardar(usuarioApunteResena2);

        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(apunte.getId());

        assertEquals(2, resenas.size());
        assertTrue(resenas.contains(resena1));
        assertTrue(resenas.contains(resena2));
    }

    @Transactional
    @Rollback
    @Test
    public void verificarSiExisteResenaConApunteYUsuario() {
        Usuario usuario = new Usuario();
        Apunte apunte = new Apunte();
        Resena resena = new Resena();

        UsuarioApunteResena usuarioApunteResena = new UsuarioApunteResena(usuario, resena, apunte);

        repositorioUsuarioApunteResena.guardar(usuarioApunteResena);

        boolean existeResena = repositorioUsuarioApunteResena.existeResenaConApunteYUsuario(usuario.getId(), apunte.getId()).size() > 0;

        assertTrue(existeResena);
    }

    @Transactional
    @Rollback
    @Test
    public void obtenerResenasPorIdUsuario() {
        Usuario usuario = new Usuario();
        Apunte apunte1 = new Apunte();
        Apunte apunte2 = new Apunte();
        Resena resena1 = new Resena();
        Resena resena2 = new Resena();

        UsuarioApunteResena usuarioApunteResena1 = new UsuarioApunteResena(usuario, resena1, apunte1);
        UsuarioApunteResena usuarioApunteResena2 = new UsuarioApunteResena(usuario, resena2, apunte2);

        repositorioUsuarioApunteResena.guardar(usuarioApunteResena1);
        repositorioUsuarioApunteResena.guardar(usuarioApunteResena2);

        List<Resena> resenas = repositorioUsuarioApunteResena.obtenerResenasPorIdUsuario(usuario.getId());

        assertEquals(2, resenas.size());
        assertTrue(resenas.contains(resena1));
        assertTrue(resenas.contains(resena2));
    }
}

