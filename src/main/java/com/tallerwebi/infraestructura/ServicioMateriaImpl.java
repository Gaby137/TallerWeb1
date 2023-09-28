package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioMateria;
import com.tallerwebi.dominio.Materia;
import com.tallerwebi.dominio.ServicioMateria;
import com.tallerwebi.presentacion.DatosMateria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service("servicioMateria")
@Transactional
public class ServicioMateriaImpl implements ServicioMateria {

    private RepositorioMateria repositorioMateria;

    @Autowired
    public ServicioMateriaImpl(RepositorioMateria repositorioMateria){
        this.repositorioMateria = repositorioMateria;
    }

    @Override
    public List<Materia> obtenerMaterias() {
        return null;
    }

    @Override
    public boolean registrar(DatosMateria materiaParam) {
        if (materiaParam.getDescripcion() == null || materiaParam.getDescripcion().isEmpty()) {
            return false;
        } else {
            Materia nuevaMateria = new Materia(materiaParam.getDescripcion(), new Date(), new Date());
            repositorioMateria.registrarMateria(nuevaMateria);
            return true;
        }
    }

    @Override
    public void actualizar(Materia materia) {
        this.repositorioMateria.modificarMateria(materia);
    }

    @Override
    public void eliminar(Materia materia) {
        this.repositorioMateria.eliminarMateria(materia);
    }

    @Override
    public Materia obtenerPorId(Long id) {
        return this.repositorioMateria.obtenerMateria(id);
    }
}
