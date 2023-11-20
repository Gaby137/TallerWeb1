package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.MateriaCarrera;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.UsuarioApunte;

import java.util.List;

public interface RepositorioMateriaCarrera {

    void registrar(MateriaCarrera materiaCarrera);
    List<MateriaCarrera> obtenerTodasLasMaterias();

}

