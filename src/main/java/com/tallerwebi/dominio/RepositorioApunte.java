package com.tallerwebi.dominio;

public interface RepositorioApunte {

    void registrarApunte(Apunte apunte);
    Apunte obtenerApunte(Long id);
    void modificarApunte(Apunte apunte);
    void eliminarApunte(Apunte apunte);
}
