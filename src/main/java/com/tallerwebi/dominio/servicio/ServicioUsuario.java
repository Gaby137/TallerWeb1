package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;

public interface ServicioUsuario {
    Usuario obtenerPorId(Long id);

    boolean actualizar(Usuario usuario);
}
