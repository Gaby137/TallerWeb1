package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;

import java.util.List;

public interface RepositorioCarrera {

    void guardar(Carrera carrera);

    List<Carrera> obtenerLista();
    Carrera obtenerCarrera(Long idCarrera);
}
