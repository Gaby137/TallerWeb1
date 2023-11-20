package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;

import java.util.List;

public interface RepositorioMateria {

    void registrarMateria(Materia materia);
    Materia obtenerMateria(Long id);
    void modificarMateria(Materia materia);
    void eliminarMateria(Materia materia);
    List<Materia> obtenerLista();
}
