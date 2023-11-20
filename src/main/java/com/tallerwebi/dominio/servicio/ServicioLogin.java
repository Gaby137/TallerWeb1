package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;

import java.io.IOException;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(DatosRegistro usuario) throws UsuarioExistente, IOException;
    String generarCodigoCreador();
}
