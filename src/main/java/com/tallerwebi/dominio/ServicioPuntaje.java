package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosPuntaje;

import java.util.List;

public interface ServicioPuntaje {
    List<Puntaje> obtenerPuntajes();
    boolean registrar(DatosPuntaje datosPuntaje);
    Puntaje obtenerPorId(Long id);
    void actualizar(Puntaje puntaje);
    void eliminar(Puntaje puntaje);
}
 
