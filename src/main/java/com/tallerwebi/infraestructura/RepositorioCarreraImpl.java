package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.iRepositorio.RepositorioCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioCarrera")
public class RepositorioCarreraImpl implements RepositorioCarrera {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCarreraImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(Carrera carrera) {
        sessionFactory.getCurrentSession().save(carrera);
    }
    @Override
    @SuppressWarnings("unchecked")
    public List<Carrera> obtenerLista() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Carrera.class)
                .addOrder(Order.asc("descripcion")) // Ordenar por el campo 'descripcion' de manera ascendente
                .list();
    }

    @Override
    public Carrera obtenerCarrera(Long idCarrera) {
        return (Carrera) sessionFactory.getCurrentSession().createCriteria(Carrera.class)
                .add(Restrictions.eq("id", idCarrera))
                .uniqueResult();
    }
}
