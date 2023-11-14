package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.UsuarioApunte;

import java.util.List;

public interface RepositorioUsuarioApunte {


    void registrar(UsuarioApunte usuarioApunte);

    List<UsuarioApunte> obtenerApuntesPorIdUsuario(Long id);

    List<UsuarioApunte> obtenerApuntesDeOtrosUsuarios(Long id);

    List<UsuarioApunte> obtenerUsuarioPorIdDeApunte(Long id);
    TipoDeAcceso obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(Long idUsuario, Long idApunte);

    void eliminarRelacionUsuarioApuntePorId(Long id);

    List<UsuarioApunte> obtenerRelacionesUsuarioApuntePorIdDeApunte(Long id);

    boolean existeRelacionUsuarioApunteLeerAsociadaAIdDeApunte(Long id);
}

