package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.*;
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
    private RepositorioApunte repositorioApunte;

    @Autowired
    public ServicioAdministradorImpl(RepositorioCarrera repositorioCarrera, RepositorioMateria repositorioMateria, RepositorioMateriaCarrera repositorioMateriaCarrera, RepositorioApunte repositorioApunte) {
        this.repositorioCarrera = repositorioCarrera;
        this.repositorioMateria = repositorioMateria;
        this.repositorioMateriaCarrera = repositorioMateriaCarrera;
        this.repositorioApunte = repositorioApunte;
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

    @Override
    public List<Apunte> filtrado(Long idCarrera, Long idMateria, Long idUsuario) {
        List<Apunte> apuntes = repositorioApunte.filtrar(idCarrera, idMateria);
        List<Apunte> apuntesActivos = new ArrayList<>();

        for (Apunte apunte : apuntes) {
            if (apunte.isActivo()) {
                apuntesActivos.add(apunte);
                // Verificar si existe una relación con el usuario específico
                boolean tieneRelacionConUsuario = apunte.getRelacionesUsuarioApunte().stream()
                        .anyMatch(relacion -> relacion.getUsuario().getId().equals(idUsuario));

                // Establecer sePuedeComprar basado en la existencia de la relación
                apunte.setSePuedeComprar(!tieneRelacionConUsuario);
            }
        }



        return apuntesActivos;
    }
}

