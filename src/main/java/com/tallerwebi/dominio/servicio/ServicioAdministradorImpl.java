package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.MateriaCarrera;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.iRepositorio.RepositorioCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateriaCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.presentacion.DatosCarrera;
import com.tallerwebi.presentacion.DatosMateria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("servicioAdministrador")
@Transactional
public class ServicioAdministradorImpl implements ServicioAdministrador {
    private RepositorioCarrera repositorioCarrera;
    private RepositorioMateria repositorioMateria;
    private RepositorioMateriaCarrera repositorioMateriaCarrera;

    @Autowired
    public ServicioAdministradorImpl(RepositorioCarrera repositorioCarrera, RepositorioMateria repositorioMateria, RepositorioMateriaCarrera repositorioMateriaCarrera) {
        this.repositorioCarrera = repositorioCarrera;
        this.repositorioMateria = repositorioMateria;
        this.repositorioMateriaCarrera = repositorioMateriaCarrera;
    }


    @Override
    public void registrarCarrera(DatosCarrera datosCarrera) {
        Carrera carrera = new Carrera(datosCarrera.getDescripcion(), new Date(), new Date());
        repositorioCarrera.guardar(carrera);
    }

    @Override
    public void registrarMateria(DatosMateria datosMateria) {
            Materia materia = new Materia(datosMateria.getDescripcion(), new Date(), new Date());
            Carrera carrera = repositorioCarrera.obtenerCarrera(datosMateria.getIdCarrera());
        MateriaCarrera materiaCarrera = new MateriaCarrera();
        materiaCarrera.setMateria(materia);
        materiaCarrera.setCarrera(carrera);

        repositorioMateriaCarrera.registrar(materiaCarrera);

    }

    @Override
    public List<Carrera> listadoCarreras() {
        if (repositorioCarrera.obtenerLista().size() > 0){
            return repositorioCarrera.obtenerLista();
        }
        return null;
    }
    @Override
    public List<Materia> listadoMaterias() {
        if (repositorioMateria.obtenerLista().size() > 0){
            return repositorioMateria.obtenerLista();
        }
        return null;
    }

    @Transactional
    @Override
    public List<Materia> obtenerMateriasPorCarrera(Long idCarrera) {
        List<MateriaCarrera> registrosMateriaCarrera = repositorioMateriaCarrera.obtenerTodasLasMaterias();

        // Lista para almacenar las materias que corresponden al idCarrera
        List<Materia> materiasPorCarrera = new ArrayList<>();

        // Iterar sobre los registros y filtrar por idCarrera
        for (MateriaCarrera relacion : registrosMateriaCarrera) {
            if (relacion.getCarrera().getId().equals(idCarrera)) {
                // Agregar la materia a la lista
                materiasPorCarrera.add(relacion.getMateria());
            }
        }

        return materiasPorCarrera;
    }

    @Override
    public Materia obtenerMateria(Long idMateria) {
        return repositorioMateria.obtenerMateria(idMateria);
    }
}

