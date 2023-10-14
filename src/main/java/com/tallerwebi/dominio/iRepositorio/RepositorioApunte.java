package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;

import java.util.List;

public interface RepositorioApunte {

    void registrarApunte(Apunte apunte);
    Apunte obtenerApunte(Long id);
    Apunte obtenerApuntePorIdResena(Long idResena);
    List<UsuarioApunteResena> getListadoDeResenasConSusUsuariosPorIdApunte(Long idApunte);
    void modificarApunte(Apunte apunte);
    void eliminarApunte(Apunte apunte);
    List<Apunte> obtenerApuntes();
}
