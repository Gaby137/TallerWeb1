package com.tallerwebi.infraestructura;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class UsuarioApuntesTest {

    @Autowired
    private SessionFactory sessionFactory;


    @Test
    public void testUsuarioTieneListaDeApuntes() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("NombreUsuario");
        usuario.setApellido("ApellidoUsuario");
        usuario.setEmail("usuario@example.com");
        usuario.setPassword("password");

        // Crear apuntes
        Apunte apunte1 = new Apunte();
        apunte1.setNombre("Apunte1");
        Apunte apunte2 = new Apunte();
        apunte2.setNombre("Apunte2");

        // Crear relaciones UsuarioApunte
        UsuarioApunte relacion1 = new UsuarioApunte();
        relacion1.setUsuario(usuario);
        relacion1.setApunte(apunte1);
        relacion1.setTipoDeAcceso(TipoDeAcceso.LEER);

        UsuarioApunte relacion2 = new UsuarioApunte();
        relacion2.setUsuario(usuario);
        relacion2.setApunte(apunte2);
        relacion2.setTipoDeAcceso(TipoDeAcceso.EDITAR);

        usuario.getRelacionesUsuarioApunte().add(relacion1);
        usuario.getRelacionesUsuarioApunte().add(relacion2);

        // Persistir el usuario y las relaciones
        Session session = sessionFactory.openSession();

        session.save(usuario);
        session.save(apunte1);
        session.save(apunte2);
        session.save(relacion1);
        session.save(relacion2);

        // Recuperar el usuario de la base de datos
        Usuario usuarioPersistido = session.find(Usuario.class, 1L);

        // Verificar que el usuario tenga una lista de relaciones UsuarioApunte no nula
        assertNotNull(usuarioPersistido.getRelacionesUsuarioApunte());
        // Verificar que el usuario tenga 2 relaciones UsuarioApunte en su lista
        assertEquals(2, usuarioPersistido.getRelacionesUsuarioApunte().size());
    }
}

