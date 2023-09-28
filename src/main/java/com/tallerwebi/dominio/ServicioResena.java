package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Long id);
    List<Resena> buscar(Long id);
    List <Resena> listar();

}
