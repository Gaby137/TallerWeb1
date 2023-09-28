package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Puntaje;
import com.tallerwebi.dominio.RepositorioPuntaje;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPuntaje")
public class RepositorioPuntajeImpl implements RepositorioPuntaje {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPuntajeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void registrarPuntaje(Puntaje puntaje) {
        sessionFactory.getCurrentSession().save(puntaje);
    }

    @Override
    public Puntaje obtenerPuntaje(Long id) {
        return (Puntaje) sessionFactory.getCurrentSession().createCriteria(Puntaje.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void modificarPuntaje(Puntaje puntaje) {
        sessionFactory.getCurrentSession().update(puntaje);
    }

    @Override
    public void eliminarPuntaje(Puntaje puntaje) {
        sessionFactory.getCurrentSession().delete(puntaje);
    }

    @Override
    public List<Puntaje> obtenerTodosLosPuntajes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTodosLosPuntajes'");
    }
}
