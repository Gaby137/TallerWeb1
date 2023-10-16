package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
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
    public ServicioUsuarioApunteImpl(RepositorioUsuarioApunte repositorioUsuarioApunte){
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
    }


    @Override
    public List<Apunte> obtenerApuntesPorUsuario(Long id) {
        Set<Apunte> apuntesPorUsuario = repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id).stream()
                .map(UsuarioApunte::getApunte)
                .collect(Collectors.toSet());

        return new ArrayList<>(apuntesPorUsuario);
    }
}

