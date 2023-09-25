package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Resena resena);
    List<Resena> buscar(Long id);

}
