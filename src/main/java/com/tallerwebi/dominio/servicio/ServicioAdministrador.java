package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.presentacion.DatosCarrera;
import com.tallerwebi.presentacion.DatosMateria;

import java.util.List;

public interface ServicioAdministrador {
    void registrarCarrera(DatosCarrera datosCarrera);
    void registrarMateria(DatosMateria datosMateria);
    List<Carrera> listadoCarreras();
    List<Materia> listadoMaterias();
    List<Materia> obtenerMateriasPorCarrera(Long idCarrera);
    Materia obtenerMateria(Long idMateria);

    List<Apunte> filtrado(Long idCarrera, Long idMateria, Long idUsuario);
}
