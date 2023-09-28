package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosApunte;

import java.util.List;

public interface ServicioApunte {
    List<Apunte> obtenerApuntes();
    boolean registrar(DatosApunte datosApunte);
    Apunte obtenerPorId(Long id);
    void actualizar(Apunte apunte);
    void eliminar(Long id);
}
