package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.entidad.Resena;
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
import static org.hamcrest.Matchers.hasSize;




import javax.transaction.Transactional;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioResenaTest {

    @Autowired
    private RepositorioResena repositorioResena;

    @Transactional
    @Rollback
    @Test
    public void guardarResena() {
        // Crear una nueva reseña
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Guardar la reseña en la base de datos
        repositorioResena.guardar(resena);

        // Buscar la reseña recién guardada por ID
        Resena reseñaGuardada = repositorioResena.buscar(resena.getId());

        // Verificar que la reseña guardada no sea nula
        assertNotNull(reseñaGuardada, "La reseña encontrada no debe ser nula");

        // Verificar que la reseña guardada sea igual a la reseña original
        assertThat(reseñaGuardada, equalTo(resena));
    }


    /*@Transactional
    @Rollback
    @Test
    public void borrarResena() {
        // Crear una nueva reseña
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Guardar la reseña en la base de datos
        repositorioResena.guardar(resena);

        Long id = resena.getId();

        // Borrar la reseña de la base de datos
        repositorioResena.borrar(id);

        // Intentar buscar la reseña borrada por ID
        Resena resenaBorrada = repositorioResena.buscar(id);

        // Verificar que la reseña no se encuentre en la base de datos
        assertNull(resenaBorrada, "La reseña debe haber sido borrada correctamente");
    }*/

    @Transactional
    @Rollback
    @Test
    public void buscarResenaPorId() {
        // Crear una nueva reseña
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Guardar la reseña en la base de datos
        repositorioResena.guardar(resena);

        // Buscar la reseña por ID
        Resena resenaEncontrada = repositorioResena.buscar(resena.getId());

        // Verificar que la reseña se encuentre en la base de datos y sea igual a la original
        assertNotNull(resenaEncontrada, "Debe haber una reseña con ese ID");
        assertEquals(resena.getId(), resenaEncontrada.getId(), "Los IDs de las reseñas deben coincidir");
        assertEquals(resena.getDescripcion(), resenaEncontrada.getDescripcion(), "Las descripciones de las reseñas deben coincidir");
        assertEquals(resena.getCantidadDeEstrellas(), resenaEncontrada.getCantidadDeEstrellas(), "Las cantidades de estrellas de las reseñas deben coincidir");
    }


}





