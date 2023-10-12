package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosRegistro;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(DatosRegistro usuario, MultipartFile fotoUrl) throws UsuarioExistente, IOException;

    String almacenarFotoDePerfil(MultipartFile fotoPerfil) throws IOException;

}
