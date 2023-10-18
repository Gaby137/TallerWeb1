package com.tallerwebi.dominio.servicio;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioUsuarioApunte {

    List<Apunte> obtenerApuntesPorUsuario(Long id);
    List<Apunte> obtenerApuntesDeOtrosUsuarios(Long id);
    boolean comprarApunte(Usuario usuario, Apunte apunte);
}
