package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.MateriaCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateriaCarrera;
import com.tallerwebi.dominio.servicio.ServicioAdministradorImpl;
import com.tallerwebi.presentacion.DatosCarrera;
import com.tallerwebi.presentacion.DatosMateria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ServicioAdministradorTest {

    private ServicioAdministradorImpl servicioAdministrador;

    private RepositorioMateria repositorioMateriaMock;
    private RepositorioCarrera repositorioCarreraMock;
    private RepositorioMateriaCarrera repositorioMateriaCarreraMock;

    @BeforeEach
    public void init(){
        // Configuración de objetos simulados
        repositorioMateriaMock = mock(RepositorioMateria.class);
        repositorioCarreraMock = mock(RepositorioCarrera.class);
        repositorioMateriaCarreraMock = mock(RepositorioMateriaCarrera.class);
        servicioAdministrador = new ServicioAdministradorImpl(repositorioCarreraMock, repositorioMateriaMock, repositorioMateriaCarreraMock);
    }


    @Test
    public void testListadoCarreras() {
        List<Carrera> carrerasMock = new ArrayList<>();
        carrerasMock.add(new Carrera("Carrera1", new Date(), new Date()));
        carrerasMock.add(new Carrera("Carrera2", new Date(), new Date()));
        when(repositorioCarreraMock.obtenerLista()).thenReturn(carrerasMock);

        List<Carrera> result = servicioAdministrador.listadoCarreras();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testListadoMaterias() {
        List<Materia> materiasMock = new ArrayList<>();
        materiasMock.add(new Materia("Materia1", new Date(), new Date()));
        materiasMock.add(new Materia("Materia2", new Date(), new Date()));
        when(repositorioMateriaMock.obtenerLista()).thenReturn(materiasMock);

        List<Materia> result = servicioAdministrador.listadoMaterias();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testObtenerMateriasPorCarrera() {
        Long idCarrera = 1L;

        List<MateriaCarrera> materiasCarreraMock = new ArrayList<>();
        MateriaCarrera materiaCarrera = new MateriaCarrera();
        materiaCarrera.setMateria(new Materia("Materia1", new Date(), new Date()));

        Carrera carrera = new Carrera("Carrera1", new Date(), new Date());
        carrera.setId(idCarrera); // Asignar el id correspondiente
        materiaCarrera.setCarrera(carrera);

        materiasCarreraMock.add(materiaCarrera);

        when(repositorioMateriaCarreraMock.obtenerTodasLasMaterias()).thenReturn(materiasCarreraMock);

        List<Materia> result = servicioAdministrador.obtenerMateriasPorCarrera(idCarrera);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Materia1", result.get(0).getDescripcion());
    }

    @Test
    public void testObtenerMateria() {
        Long idMateria = 1L;

        Materia materiaMock = new Materia("Materia1", new Date(), new Date());
        when(repositorioMateriaMock.obtenerMateria(idMateria)).thenReturn(materiaMock);

        Materia result = servicioAdministrador.obtenerMateria(idMateria);

        assertNotNull(result);
        assertEquals("Materia1", result.getDescripcion());
    }

}





