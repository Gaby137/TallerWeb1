package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
            boolean result;
            if (usuario.getNombre() == null || usuario.getNombre().isEmpty() ||
                  usuario.getId() == null) {

                result = false;
            }else {

                repositorioUsuario.modificar(usuario);
                result = true;
            }
            return result;
        }
    }

