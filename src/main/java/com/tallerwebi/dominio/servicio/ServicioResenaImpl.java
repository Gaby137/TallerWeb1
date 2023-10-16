package com.tallerwebi.dominio.servicio;

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

    @Autowired
    public ServicioResenaImpl(RepositorioResena repositorioResena){
        this.repositorioResena = repositorioResena;
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
    public List<Resena> buscar(Long id) {
        return this.repositorioResena.buscar(id);
    }
    @Override
    public List<Resena> listar(){

        return this.repositorioResena.listar();
    }
}
