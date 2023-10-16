package com.tallerwebi.dominio.servicio;
import com.tallerwebi.dominio.entidad.Apunte;

import java.util.List;

public interface ServicioUsuarioApunte {

    List<Apunte> obtenerApuntesPorUsuario(Long id);

}
