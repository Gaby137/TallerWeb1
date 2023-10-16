package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosPuntaje;
import com.tallerwebi.presentacion.DatosRegistro;

import java.io.IOException;
import java.util.List;

public interface ServicioUsuarioApunteResena {

    void registrar(UsuarioApunteResena usuarioApunteResena);
    List<Resena> obtenerLista(Long idApunte);

}
