package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioApunte {

    void registrarApunte(Apunte apunte);
    Apunte obtenerApunte(Long id);
    void modificarApunte(Apunte apunte);
    void eliminarApunte(Apunte apunte);
    List<Apunte> obtenerApuntes();
}
