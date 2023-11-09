package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;

import java.util.List;

public interface RepositorioUsuarioApunteResena {
    void guardar(UsuarioApunteResena usuarioApunteResena);
    List<Resena> obtenerResenasPorIdApunte(Long idApunte);
    List<UsuarioApunteResena> existeResenaConApunteYUsuario(Long idUsuario,Long idApunte);

    List<Resena> obtenerResenasPorIdUsuario(Long idUsuario);
}

