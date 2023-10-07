package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;

import com.tallerwebi.dominio.servicio.ServicioMateriaImpl;
import com.tallerwebi.presentacion.DatosMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioMateriaTest {

    private ServicioMateriaImpl servicioMateria;

    private RepositorioMateria repositorioMateriaMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioMateriaMock = mock(RepositorioMateria.class);
        servicioMateria = new ServicioMateriaImpl(repositorioMateriaMock);
    }


    @Test
    public void guardarMateria() {
        DatosMateria materia = new DatosMateria("Geometria");

        // Ejecución de la prueba
        boolean resultado = servicioMateria.registrar(materia);

        // Verificación
        assertTrue(resultado);
        // Verifica que se llamó al método del repositorio
        verify(repositorioMateriaMock).registrarMateria(any(Materia.class));
    }

    @Test
    public void borrarMateria() {
        Materia materia = new Materia();
        materia.setDescripcion("Geometria");

        servicioMateria.eliminar(materia);

        verify(repositorioMateriaMock).eliminarMateria(materia);
    }

    @Test
    public void buscarMateriaPorId() {
        Long idEjemplo = 1L;
        Materia materia = new Materia("Geometria", new Date(), new Date());

        when(repositorioMateriaMock.obtenerMateria(idEjemplo)).thenReturn(materia);

        Materia materiaGuardada = servicioMateria.obtenerPorId(idEjemplo);

        assertNotNull(materiaGuardada);
        assertEquals(materia, materiaGuardada);
    }

}





