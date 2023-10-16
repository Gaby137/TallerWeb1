package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.UsuarioApunte;

import java.util.List;

public interface RepositorioUsuarioApunte {


    void registrar(UsuarioApunte usuarioApunte);

    List<UsuarioApunte> obtenerApuntesPorIdUsuario(Long id);
}
