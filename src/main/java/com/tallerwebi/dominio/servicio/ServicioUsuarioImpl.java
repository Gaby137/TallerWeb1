package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioUsuario")
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        if (usuario == null || usuario.getNombre() == null || usuario.getNombre().isEmpty() || usuario.getId() == null) {
            return false;
        }
        repositorioUsuario.modificar(usuario);
        return true;
    }

    @Override
    public List<Usuario> buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(Long id){
        return repositorioUsuario.buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(id);
    }

    @Override
    public boolean existeCodigoCreadorEnLaBaseDeDatos(String codigoCreador) {
        return repositorioUsuario.existeCodigoCreadorEnLaBaseDeDatos(codigoCreador);
    }

    @Override
    public Usuario buscarUsuarioPorCodigoDeCreador(String codigoDeCreador){
        return repositorioUsuario.buscarUsuarioPorCodigoCreador(codigoDeCreador);
    }

    @Override
    public boolean mostrarPopUp(Long id){
        return repositorioUsuario.obtenerEstadoPopupPorIdUsuario(id);
    }
}

