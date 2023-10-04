// RepositorioPuntaje.java
package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Puntaje;

import java.util.List;

public interface RepositorioPuntaje {
    void registrarPuntaje(Puntaje puntaje);
    Puntaje obtenerPuntaje(Long id);
    List<Puntaje> obtenerTodosLosPuntajes();  // Agregamos este método
    void modificarPuntaje(Puntaje puntaje);
    void eliminarPuntaje(Puntaje puntaje);
}