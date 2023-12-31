package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.presentacion.DatosMateria;

import java.util.List;

public interface ServicioMateria {
    List<Materia> obtenerMaterias();
    boolean registrar(DatosMateria materia);
    Materia obtenerPorId(Long id);
    void actualizar(Materia materia);
    void eliminar(Materia materia);

}
