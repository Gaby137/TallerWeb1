package com.tallerwebi.dominio.servicio;
import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.excepcion.ApunteYaCompradoException;
import com.tallerwebi.dominio.excepcion.PuntosInsuficientesException;

import java.util.List;

public interface ServicioUsuarioApunte {

    List<UsuarioApunte> obtenerApuntesPorUsuario(Long id);
    List<Apunte> obtenerApuntesDeOtrosUsuarios(Long id);

    List<Apunte> obtenerTodosLosApuntes(Long id);

    Usuario obtenerVendedorPorApunte(Long id);
    boolean comprarApunte(Usuario comprador, Usuario vendedor, Apunte apunte) throws ApunteYaCompradoException, PuntosInsuficientesException;

    void eliminarApunte(Long id);

    TipoDeAcceso obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(Long idUsuario, Long idApunte);

    boolean existeRelacionUsuarioApunteEditar(Long idUsuario, Long idApunte);

    List<Apunte> obtenerApuntesPorIdDeUsuario(Long id);
}
