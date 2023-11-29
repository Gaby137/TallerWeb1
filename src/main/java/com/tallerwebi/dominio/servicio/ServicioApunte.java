package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.presentacion.DatosApunte;

import java.util.List;

public interface ServicioApunte{
    List<Apunte> obtenerApuntes();
    Apunte obtenerPorId(Long id);
    Apunte obtenerApuntePorIdResena(Long idResena);
    List<UsuarioApunteResena> getListadoDeResenasConSusUsuariosPorIdApunte(Long idApunte);
    boolean actualizar(Apunte apunte);
    void eliminar(Long id);
    List<Apunte> obtenerApuntesNovedades();

    Materia obtenerMateriaPorIdDeApunte(Long id);
}
