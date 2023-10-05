package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario servicioLoginDao;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao){
        this.servicioLoginDao = servicioLoginDao;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return servicioLoginDao.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        Usuario usuarioEncontrado = servicioLoginDao.buscarUsuario(usuario.getEmail());
        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }
        servicioLoginDao.guardar(usuario);
    }

}

