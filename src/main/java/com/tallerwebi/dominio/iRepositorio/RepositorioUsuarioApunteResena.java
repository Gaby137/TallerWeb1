package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;

import java.util.List;

public interface RepositorioUsuarioApunteResena {


    void guardar(UsuarioApunteResena usuarioApunteResena);
    List<Resena> obtenerResenasPorIdApunte(Long idApunte);
    void registrar(UsuarioApunteResena usuarioApunteResena);
    List<UsuarioApunteResena> obtenerResenasPorUsuario(Long idUsuario);
    List<UsuarioApunteResena> obtenerResenasPorApunte(Long idApunte);

}

