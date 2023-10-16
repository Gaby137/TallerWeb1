package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service("servicioUsuarioApunteResena")
@Transactional
public class ServicioUsuarioApunteResenaImpl implements ServicioUsuarioApunteResena {

    private RepositorioUsuarioApunteResena repositorioUsuarioApunteResena;

    @Autowired
    public ServicioUsuarioApunteResenaImpl(RepositorioUsuarioApunteResena repositorioUsuarioApunteResena){
        this.repositorioUsuarioApunteResena = repositorioUsuarioApunteResena;
    }

    @Override
    public void registrar(UsuarioApunteResena usuarioApunteResena) {

        repositorioUsuarioApunteResena.guardar(usuarioApunteResena);;
    }

    @Override
    public List<Resena> obtenerLista(Long idApunte) {
        return repositorioUsuarioApunteResena.obtenerResenasPorIdApunte(idApunte);
    }
}

