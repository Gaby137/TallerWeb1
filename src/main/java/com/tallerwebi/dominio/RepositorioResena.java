package com.tallerwebi.dominio;

public interface RepositorioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Resena resena);
    Resena buscar(Long id);

}
