package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password, String nombre);
    void registrar(Usuario usuario) throws UsuarioExistente;

}
