package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;
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

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioTest {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Transactional
    @Rollback
    @Test
    public void buscarUsuarioPorId() {
        // Crear un nuevo apunte
        Usuario usuario = new Usuario();
        usuario.setNombre("Nombre Usuario");

        // Guardar el apunte en la base de datos
        repositorioUsuario.guardar(usuario);

        Usuario usuarioObtenido = repositorioUsuario.buscarPorId(usuario.getId());

        // Verificar que se encuentre el usuario
        assertEquals(usuario, usuarioObtenido, "El apunte encontrado debe ser igual al apunte original");
    }

}
