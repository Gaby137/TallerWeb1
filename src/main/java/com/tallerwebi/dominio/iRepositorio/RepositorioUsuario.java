package com.tallerwebi.dominio.iRepositorio;

import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    Usuario buscarUsuario(String email);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
    Usuario buscarPorId(Long id);

    List<Usuario> buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(Long id);

    boolean existeCodigoCreadorEnLaBaseDeDatos(String codigoCreador);

    Usuario buscarUsuarioPorCodigoCreador(String codigoCreador);
}

