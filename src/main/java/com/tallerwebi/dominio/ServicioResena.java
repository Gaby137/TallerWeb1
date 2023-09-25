package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Resena resena);
    List<Resena> buscar(Long id);

}
