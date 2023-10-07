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
        Session session = sessionFactory.getCurrentSession();
        Resena resena = session.get(Resena.class, id);

        if (resena != null) {
            session.delete(resena);
        }
    }

    @Override
    public List<Resena> buscar(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .add(Restrictions.eq("id", id))
                .list();
    }
    @Override
    public List<Resena> listar() {
        return sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .list();
    }
}
