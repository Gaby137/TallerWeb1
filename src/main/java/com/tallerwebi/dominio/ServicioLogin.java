package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password, String nombre);
    void registrar(Usuario usuario) throws UsuarioExistente;

}
