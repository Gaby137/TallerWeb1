package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.presentacion.DatosApunte;

import java.util.List;

public interface ServicioApunte {
    List<Apunte> obtenerApuntes();
    boolean registrar(DatosApunte datosApunte);
    Apunte obtenerPorId(Long id);
    boolean actualizar(Apunte apunte);
    void eliminar(Long id);
}
