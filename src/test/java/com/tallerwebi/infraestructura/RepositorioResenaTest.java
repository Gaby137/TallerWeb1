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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioResenaTest {

    @Autowired
    private RepositorioResena repositorioResena;

    @Transactional
    @Rollback
    @Test
    public void modificarResena() {
        // Crear una nueva reseña
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Guardar la reseña en la base de datos
        repositorioResena.guardar(resena);

        // Modificar la reseña
        resena.setDescripcion("Excelente reseña");
        resena.setCantidadDeEstrellas(4); // Cambiar la calificación

        // Actualizar la reseña en la base de datos
        repositorioResena.modificar(resena);

        // Buscar la reseña modificada por ID
        Resena reseñaModificada = repositorioResena.buscar(resena.getId()).get(0);

        // Verificar que la reseña modificada tenga los cambios
        assertThat(reseñaModificada.getDescripcion(), equalTo("Excelente reseña"));
        assertThat(reseñaModificada.getCantidadDeEstrellas(), equalTo(4));

}
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
        Resena reseñaGuardada = repositorioResena.buscar(resena.getId()).get(0);


        // Verificar que la reseña guardada sea igual a la reseña original
        assertThat(reseñaGuardada, equalTo(resena));
    }


    @Transactional
    @Rollback
    @Test
    public void borrarResena() {
        // Crear una nueva reseña
        Resena resena = new Resena();
        resena.setDescripcion("Buena reseña");
        resena.setCantidadDeEstrellas(5);

        // Guardar la reseña en la base de datos
        repositorioResena.guardar(resena);

        Long id=resena.getId();
        // Borrar la reseña de la base de datos
        repositorioResena.borrar(id);

        // Intentar buscar la reseña borrada por ID
        List<Resena> resenas = repositorioResena.buscar(resena.getId());

        // Verificar que no se encuentre ninguna reseña con ese ID
        assertTrue(resenas.isEmpty(), "No debe haber ninguna reseña con ese ID");
    }

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
        List<Resena> reseñas = repositorioResena.buscar(resena.getId());

        // Verificar que se encuentre la reseña con ese ID
        assertEquals(1, reseñas.size(), "Debe haber exactamente una reseña con ese ID");
        assertEquals(resena, reseñas.get(0), "La reseña encontrada debe ser igual a la reseña original");
    }

}





