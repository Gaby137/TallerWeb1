package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioResena;
import com.tallerwebi.dominio.entidad.Resena;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioReseña")
public class RepositorioResenaImpl implements RepositorioResena {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioResenaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(Resena resena) {
            sessionFactory.getCurrentSession().save(resena);
        }

    @Override
    public void modificar(Resena resena) {
        sessionFactory.getCurrentSession().update(resena);
    }

    @Override
    public void borrar(Long id) {
        Session session = sessionFactory.openSession();

        Resena r = (Resena) sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();

        if (r != null) {
            session.beginTransaction();
            session.delete(r);
            session.getTransaction().commit();
            session.close();
        }
    }

    @Override
    public Resena buscar(Long id) {
        return (Resena) sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
    @Override
    public List<Resena> listar() {
        return sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .list();
    }

    
}
