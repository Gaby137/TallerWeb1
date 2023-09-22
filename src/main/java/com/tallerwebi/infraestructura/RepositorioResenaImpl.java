package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioResena;
import com.tallerwebi.dominio.Resena;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
    public void borrar(Resena resena) {
        sessionFactory.getCurrentSession().delete(resena);
    }

    @Override
    public List<Resena> buscar(Long id) {
        return sessionFactory.getCurrentSession().createCriteria(Resena.class)
                .add(Restrictions.eq("id", id))
                .list();
    }
}
