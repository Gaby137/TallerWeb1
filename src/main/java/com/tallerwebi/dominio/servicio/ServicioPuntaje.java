package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Puntaje;
import com.tallerwebi.presentacion.DatosPuntaje;

import java.util.List;

public interface ServicioPuntaje {
    List<Puntaje> obtenerPuntajes();
    boolean registrar(DatosPuntaje datosPuntaje);
    Puntaje obtenerPorId(Long id);
    void actualizar(Puntaje puntaje);
    void eliminar(Puntaje puntaje);
}
 
