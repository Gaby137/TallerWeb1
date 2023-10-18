package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioApunteTest {

    @Autowired
    private RepositorioApunte repositorioApunte;

    @Transactional
    @Rollback
    @Test
    public void guardarApunte() {
        // Crear un nuevo apunte
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        // Guardar el apunte en la base de datos
        repositorioApunte.registrarApunte(apunte);

        // Buscar por id el apunte recien guardado
        Apunte apunteGuardado = repositorioApunte.obtenerApunte(apunte.getId());

        // Verificar que la reseña guardada sea igual a la reseña original
        assertThat(apunteGuardado, equalTo(apunte));
    }


    @Transactional
    @Rollback
    @Test
    public void borrarApunte() {
        // Crear un nuevo apunte
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        // Guardar el apunte en la base de datos
        repositorioApunte.registrarApunte(apunte);

        Long id = apunte.getId();

        // Borrar la reseña de la base de datos
        repositorioApunte.eliminarApunte(apunte);

        // Intentar buscar la reseña borrada por ID
        Apunte apunteBorrado = repositorioApunte.obtenerApunte(id);

        // Verificar que no se encuentre ninguna reseña con ese ID
        assertNull(apunteBorrado, "El apunte debe haber sido borrado correctamente");
    }

    @Transactional
    @Rollback
    @Test
    public void deberiaDevolverApunteAlBuscarloPorSuId() {
        // Crear un nuevo apunte
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        // Guardar el apunte en la base de datos
        repositorioApunte.registrarApunte(apunte);

        Apunte apunteObtenido = repositorioApunte.obtenerApunte(apunte.getId());

        // Verificar que se encuentre el apunte
        assertEquals(apunte, apunteObtenido, "El apunte encontrado debe ser igual al apunte original");
        assertEquals(apunte.getId(), apunteObtenido.getId(), "Debe haber exactamente un apunte");
    }


}





