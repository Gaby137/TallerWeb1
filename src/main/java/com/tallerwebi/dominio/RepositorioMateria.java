package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioMateria {

    void registrarMateria(Materia materia);
    Materia obtenerMateria(Long id);
    void modificarMateria(Materia materia);
    void eliminarMateria(Materia materia);
}
