package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;

public interface ServicioResena {

    void guardar(Resena resena);
    void modificar(Resena resena);
    void borrar(Resena resena);
    Resena buscar(Long id);
}
