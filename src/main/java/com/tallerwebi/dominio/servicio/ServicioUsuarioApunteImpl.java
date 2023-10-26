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
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ServicioUsuarioApunteImpl(RepositorioUsuarioApunte repositorioUsuarioApunte, ServicioUsuario servicioUsuario) {
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
        this.servicioUsuario=servicioUsuario;
    }

    public List<UsuarioApunte> obtenerApuntesPorUsuario(Long id) {
        return this.repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id);
    }
    public List<Apunte> obtenerApuntesDeOtrosUsuarios(Long id) {
        List<UsuarioApunte> apuntesDeOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(id);

        List<Apunte> apuntesDeOtrosUsuariosList = new ArrayList<>();

        for (UsuarioApunte usuarioApunte : apuntesDeOtrosUsuarios) {
            if(usuarioApunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR)
            apuntesDeOtrosUsuariosList.add(usuarioApunte.getApunte());
        }

        return apuntesDeOtrosUsuariosList;
    }


    @Override
    public Usuario obtenerVendedorPorApunte(Long id){
        List<UsuarioApunte> usuarioApuntes = repositorioUsuarioApunte.obtenerUsuarioPorIdDeApunte(id);

        for (UsuarioApunte usuarioApunte : usuarioApuntes){
            if(usuarioApunte.getTipoDeAcceso()== TipoDeAcceso.EDITAR){
                return usuarioApunte.getUsuario();
            }
        }

        return null;
    }

    @Override
    @Transactional
    public boolean comprarApunte(Usuario comprador, Usuario vendedor, Apunte apunte) {
        if (comprador == null || vendedor == null || apunte == null) {
            return false;
        }

        if (comprador.getPuntos() >= apunte.getPrecio()) {
            comprador.setPuntos(comprador.getPuntos() - apunte.getPrecio());
            vendedor.setPuntos(vendedor.getPuntos() + apunte.getPrecio());
            servicioUsuario.actualizar(comprador);
            servicioUsuario.actualizar(vendedor);

            UsuarioApunte usuarioApunte = new UsuarioApunte(comprador, apunte);
            usuarioApunte.setUsuario(comprador);
            usuarioApunte.setApunte(apunte);
            usuarioApunte.setTipoDeAcceso(TipoDeAcceso.LEER);

            repositorioUsuarioApunte.registrar(usuarioApunte);

            return true;
        }

        return false;
    }
}

