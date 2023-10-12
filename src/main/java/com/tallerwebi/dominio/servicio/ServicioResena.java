package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Resena;

import java.util.List;

public interface ServicioResena {

    void guardar(Resena resena);

    void modificar(Resena resena);

    void borrar(Long id);

    List<Resena> buscar(Long id);

    List<Resena> listar(Long id);

}
