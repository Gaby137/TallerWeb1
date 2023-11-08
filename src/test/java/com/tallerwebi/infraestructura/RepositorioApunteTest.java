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
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        repositorioApunte.registrarApunte(apunte);

        Apunte apunteGuardado = repositorioApunte.obtenerApunte(apunte.getId());

        assertThat(apunteGuardado, equalTo(apunte));
    }


    @Transactional
    @Rollback
    @Test
    public void borrarApunte() {
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        repositorioApunte.registrarApunte(apunte);

        Long id = apunte.getId();

        repositorioApunte.eliminarApunte(apunte);

        Apunte apunteBorrado = repositorioApunte.obtenerApunte(id);

        assertNull(apunteBorrado, "El apunte debe haber sido borrado correctamente");
    }

    @Transactional
    @Rollback
    @Test
    public void deberiaDevolverApunteAlBuscarloPorSuId() {
        Apunte apunte = new Apunte();
        apunte.setNombre("Nombre Apunte");
        apunte.setDescripcion("Descripcion");
        apunte.setPrecio(15);
        apunte.setPathArchivo("Path");

        repositorioApunte.registrarApunte(apunte);

        Apunte apunteObtenido = repositorioApunte.obtenerApunte(apunte.getId());

        assertEquals(apunte, apunteObtenido, "El apunte encontrado debe ser igual al apunte original");
        assertEquals(apunte.getId(), apunteObtenido.getId(), "Debe haber exactamente un apunte");
    }


}





