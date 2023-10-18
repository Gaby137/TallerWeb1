package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("servicioUsuarioApunte")
@Transactional
public class ServicioUsuarioApunteImpl implements ServicioUsuarioApunte {

    private RepositorioUsuarioApunte repositorioUsuarioApunte;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ServicioUsuarioApunteImpl(RepositorioUsuarioApunte repositorioUsuarioApunte) {
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
    }


    @Override
    public List<Apunte> obtenerApuntesPorUsuario(Long id) {
        Set<Apunte> apuntesPorUsuario = repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id).stream()
                .map(UsuarioApunte::getApunte)
                .collect(Collectors.toSet());

        return new ArrayList<>(apuntesPorUsuario);
    }

    @Override
    public List<Apunte> obtenerApuntesDeOtrosUsuarios(Long id) {

        Set<Apunte> apuntesDeOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(id).stream()
                .filter(usuarioApunte -> !usuarioApunte.getUsuario().getId().equals(id))
                .map(UsuarioApunte::getApunte)
                .collect(Collectors.toSet());

        Set<Apunte> apuntesCompradosPorUsuario = repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id).stream()
                .map(UsuarioApunte::getApunte)
                .collect(Collectors.toSet());

        apuntesDeOtrosUsuarios.removeAll(apuntesCompradosPorUsuario);

        return new ArrayList<>(apuntesDeOtrosUsuarios);
    }

    @Override
    public boolean comprarApunte(Usuario usuario, Apunte apunte) {
        if (usuario == null || apunte == null) {
            return false;
        }

        if (usuario.getPuntos() >= apunte.getPrecio()) {
            usuario.setPuntos(usuario.getPuntos() - apunte.getPrecio());
            servicioUsuario.actualizar(usuario);

            UsuarioApunte usuarioApunte = new UsuarioApunte(usuario, apunte);
            usuarioApunte.setUsuario(usuario);
            usuarioApunte.setApunte(apunte);
            usuarioApunte.setTipoDeAcceso(TipoDeAcceso.LEER);

            repositorioUsuarioApunte.registrar(usuarioApunte);

            return true;
        }

        return false;
    }
}

