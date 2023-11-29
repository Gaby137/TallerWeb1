package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.ArchivoInexistenteException;
import com.tallerwebi.presentacion.DatosApunte;

import java.util.List;

public interface ServicioUsuarioApunteResena {

    boolean registrarResena(Usuario usuario, Apunte apunte, Resena resena);

    void registrarApunte(DatosApunte datosApunte, Usuario usuario) throws ArchivoInexistenteException;

    boolean dar100PuntosAlUsuarioPorBuenasResenas(Long idApunte);

    void darPuntosAlUsuarioPorParticipacionContinua(Usuario usuario);

    List<Resena> obtenerListaDeResenasPorIdApunte(Long idApunte);

    List<Resena> obtenerResenasPorIdDeUsuario(Long idUsuario);

    double calcularPromedioPuntajeResenas(Long apunteId);
    double calcularPromedioPuntajeResenasPorUsuario(Long usuarioId);
    List<Apunte> obtenerMejoresApuntes(Long usuarioId);
    List<Usuario> obtenerUsuariosDestacados(Long usuarioId);
    List<Apunte> obtenerApuntesComprados(Usuario usuario);
    List<Apunte> obtenerApuntesCreados(Usuario usuario);
    //List<Apunte> obtenerApuntesCreadosYVerSiPuedeComprar(Usuario usuario, Usuario usuarioActual);
    boolean existeResena(Long idUsuario, Long idApunte);

    Resena obtenerResenasPorIdDeUsuarioYApunte(Long idUsuario, Long idApunte);
}
