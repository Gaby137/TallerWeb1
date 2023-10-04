package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioApunte")
public class RepositorioApunteImpl implements RepositorioApunte {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioApunteImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void registrarApunte(Apunte apunte) {
        sessionFactory.getCurrentSession().save(apunte);
    }

    @Override
    public Apunte obtenerApunte(Long id) {
        return (Apunte) sessionFactory.getCurrentSession().createCriteria(Apunte.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void modificarApunte(Apunte apunte) {
        sessionFactory.getCurrentSession().update(apunte);
    }

    @Override
    public void eliminarApunte(Apunte apunte) {
        sessionFactory.getCurrentSession().delete(apunte);
    }

    @Override
    public List<Apunte> obtenerApuntes() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Apunte.class);
         return criteria.list();
    }
}
