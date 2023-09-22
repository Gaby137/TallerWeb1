package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioResena;
import com.tallerwebi.dominio.Resena;
import com.tallerwebi.dominio.ServicioResena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioResena")
@Transactional
public class ServicioResenaImpl implements ServicioResena {

    private RepositorioResena repositorioResena;

    @Autowired
    public ServicioResenaImpl(RepositorioResena servicioResena){
        this.repositorioResena = servicioResena;
    }
    @Override
    public void guardar(Resena resena) {
        this.repositorioResena.guardar(resena);
    }

    @Override
    public void modificar(Resena resena) {
        this.repositorioResena.modificar(resena);
    }

    @Override
    public void borrar(Resena resena) {
        this.repositorioResena.borrar(resena);
    }

    @Override
    public Resena buscar(Long id) {
        return this.repositorioResena.buscar(id);
    }
}
