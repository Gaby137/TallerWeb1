package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.UsuarioApunteResena;

import java.util.List;

public interface ServicioUsuarioApunteResena {

    void registrar(UsuarioApunteResena usuarioApunteResena);
    List<UsuarioApunteResena> obtenerResenasPorUsuario(Long idUsuario);
    List<UsuarioApunteResena> obtenerResenasPorApunte(Long idApunte);
}
