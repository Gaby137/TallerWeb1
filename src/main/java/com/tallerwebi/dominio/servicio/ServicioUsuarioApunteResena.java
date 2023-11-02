package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.DatosPuntaje;
import com.tallerwebi.presentacion.DatosRegistro;

import java.io.IOException;
import java.util.List;

public interface ServicioUsuarioApunteResena {

    boolean registrar(Usuario usuario, Apunte apunte, Resena resena);
    List<Resena> obtenerLista(Long idApunte);
    boolean dar100PuntosAlUsuarioPorBuenasResenas(Long idApunte);
    double calcularPromedioPuntajeResenas(Long apunteId);
    double calcularPromedioPuntajeResenasPorUsuario(Long usuarioId);
    List<Apunte> obtenerMejoresApuntes(Long usuarioId);
    List<Usuario> obtenerUsuariosDestacados(Long usuarioId);
    List<UsuarioApunte> obtenerApuntesComprados(Usuario usuario);
    List<UsuarioApunte> obtenerApuntesCreados(Usuario usuario);
    List<UsuarioApunte> obtenerApuntesCreadosYVerSiPuedeComprar(Usuario usuario, Usuario usuarioActual);
    boolean existeResena(Long idUsuario, Long idApunte);
}
