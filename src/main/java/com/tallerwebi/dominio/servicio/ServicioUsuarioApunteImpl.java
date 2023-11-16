package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("servicioUsuarioApunte")
@Transactional
public class ServicioUsuarioApunteImpl implements ServicioUsuarioApunte {

    private RepositorioUsuarioApunte repositorioUsuarioApunte;
    private ServicioUsuario servicioUsuario;
    private ServicioApunte servicioApunte;

    @Autowired
    public ServicioUsuarioApunteImpl(RepositorioUsuarioApunte repositorioUsuarioApunte, ServicioUsuario servicioUsuario, ServicioApunte servicioApunte) {
        this.repositorioUsuarioApunte = repositorioUsuarioApunte;
        this.servicioUsuario = servicioUsuario;
        this.servicioApunte = servicioApunte;
    }

    @Override
    public List<UsuarioApunte> obtenerApuntesPorUsuario(Long id) {
        return this.repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id);
    }

    @Override
    public List<Apunte> obtenerApuntesDeOtrosUsuarios(Long id) {
        List<UsuarioApunte> apuntesDeOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(id);

        List<Apunte> apuntesDeOtrosUsuariosList = new ArrayList<>();

        for (UsuarioApunte usuarioApunte : apuntesDeOtrosUsuarios) {
            if (usuarioApunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR)
                apuntesDeOtrosUsuariosList.add(usuarioApunte.getApunte());
        }

        return apuntesDeOtrosUsuariosList;
    }

    @Override
    public List<Apunte> obtenerTodosLosApuntes(Long id){
        List<UsuarioApunte> apuntesDeOtrosUsuarios = repositorioUsuarioApunte.obtenerApuntesDeOtrosUsuarios(id);
        List<UsuarioApunte> apuntesDelUsuario = repositorioUsuarioApunte.obtenerApuntesPorIdUsuario(id);

        List<Apunte> apuntesDeTodosLosUsuarios = new ArrayList<>();

        for (UsuarioApunte usuarioApunte : apuntesDeOtrosUsuarios) {
            if (usuarioApunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR)
                apuntesDeTodosLosUsuarios.add(usuarioApunte.getApunte());
        }

        for (UsuarioApunte usuarioApunte : apuntesDelUsuario) {
            if (usuarioApunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR)
                apuntesDeTodosLosUsuarios.add(usuarioApunte.getApunte());
        }

        return apuntesDeTodosLosUsuarios;
    }



    @Override
    public Usuario obtenerVendedorPorApunte(Long id) {
        List<UsuarioApunte> usuarioApuntes = repositorioUsuarioApunte.obtenerUsuarioPorIdDeApunte(id);

        for (UsuarioApunte usuarioApunte : usuarioApuntes) {
            if (usuarioApunte.getTipoDeAcceso() == TipoDeAcceso.EDITAR) {
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
            apunte.setActivo(true);

            UsuarioApunte usuarioApunte = new UsuarioApunte(comprador, apunte);
            usuarioApunte.setUsuario(comprador);
            usuarioApunte.setApunte(apunte);
            usuarioApunte.setTipoDeAcceso(TipoDeAcceso.LEER);

            repositorioUsuarioApunte.registrar(usuarioApunte);

            return true;
        }

        return false;
    }

    @Override
    public void eliminarApunte(Long id) {
        List<UsuarioApunte> relacionesUsuarioApunte = repositorioUsuarioApunte.obtenerRelacionesUsuarioApuntePorIdDeApunte(id);
        boolean existeRelacionLeer = false;

        for (UsuarioApunte relacion : relacionesUsuarioApunte) {
            if (relacion.getTipoDeAcceso() == TipoDeAcceso.LEER) {
                existeRelacionLeer = true;
                break;
            }
        }

        Apunte apunte = servicioApunte.obtenerPorId(id);

        if (existeRelacionLeer) {
            apunte.setActivo(false);
            servicioApunte.actualizar(apunte);
        } else {
            for (UsuarioApunte relacion : relacionesUsuarioApunte) {
                repositorioUsuarioApunte.eliminarRelacionUsuarioApuntePorId(relacion.getId());
            }
        }
    }

    @Override
    public TipoDeAcceso obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(Long idUsuario, Long idApunte) {
        return repositorioUsuarioApunte.obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(idUsuario, idApunte);
    }


}
