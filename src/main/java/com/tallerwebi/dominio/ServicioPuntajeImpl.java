package com.tallerwebi.dominio;

import com.tallerwebi.presentacion.DatosPuntaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service("servicioPuntaje")
@Transactional
public class ServicioPuntajeImpl implements ServicioPuntaje {
    private RepositorioPuntaje repositorioPuntaje;

    @Autowired
    public ServicioPuntajeImpl(RepositorioPuntaje repositorioPuntaje) {
        this.repositorioPuntaje = repositorioPuntaje;
    }

    @Override
    public List<Puntaje> obtenerPuntajes() {
        return repositorioPuntaje.obtenerTodosLosPuntajes();
    }

    @Override
    public boolean registrar(DatosPuntaje datosPuntaje) {
        boolean result;
        if (datosPuntaje.getUsuario() == null || datosPuntaje.getPuntos() <= 0) {
            result = false;
        } else {
            Puntaje puntaje = new Puntaje(datosPuntaje.getPuntos(), new Date(), new Date());
    
            repositorioPuntaje.registrarPuntaje(puntaje);
            result = true;
        }
        return result;
    }

    @Override
    public Puntaje obtenerPorId(Long id) {
        return repositorioPuntaje.obtenerPuntaje(id);
    }

    @Override
    public void actualizar(Puntaje puntaje) {
        repositorioPuntaje.modificarPuntaje(puntaje);
    }

    @Override
    public void eliminar(Puntaje puntaje) {
        repositorioPuntaje.eliminarPuntaje(puntaje);
    }
}
