package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Materia;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioMateria")
public class RepositorioMateriaImpl implements RepositorioMateria {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMateriaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void registrarMateria(Materia materia) {
        sessionFactory.getCurrentSession().save(materia);
    }

    @Override
    public Materia obtenerMateria(Long id) {
        return (Materia) sessionFactory.getCurrentSession().createCriteria(Materia.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void modificarMateria(Materia materia) {
        sessionFactory.getCurrentSession().update(materia);
    }

    @Override
    public void eliminarMateria(Materia materia) {
        sessionFactory.getCurrentSession().delete(materia);
    }
}
