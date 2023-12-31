package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioUsuario {
    Usuario obtenerPorId(Long id);
    boolean actualizar(Usuario usuario);
    List<Usuario> buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(Long id);

    boolean existeCodigoCreadorEnLaBaseDeDatos(String codigoCreador);

    Usuario buscarUsuarioPorCodigoDeCreador(String codigoDeCreador);

    boolean mostrarPopUp(Long id);
}
