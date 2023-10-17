package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Resena;

import java.util.List;

public interface RepositorioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Long id);
    Resena buscar(Long id);
    List<Resena> listar();

}
