package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.entidad.Resena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioResena")
@Transactional
public class ServicioResenaImpl implements ServicioResena {

    private RepositorioResena repositorioResena;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ServicioResenaImpl(RepositorioResena repositorioResena, ServicioUsuario servicioUsuario) {
        this.repositorioResena = repositorioResena;
        this.servicioUsuario = servicioUsuario;
    }



    @Override
    public void guardar(Resena resena) {
        repositorioResena.guardar(resena);
    }

    @Override
    public void modificar(Resena resena) {
        this.repositorioResena.modificar(resena);
    }

    @Override
    public void borrar(Long id) {
        this.repositorioResena.borrar(id);
    }

    @Override
    public Resena buscar(Long id) {
        return this.repositorioResena.buscar(id);
    }
    @Override
    public List<Resena> listar(){

        return this.repositorioResena.listar();
    }
}
