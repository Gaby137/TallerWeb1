package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioUsuarioApunte")
public class RepositorioUsuarioApunteImpl implements RepositorioUsuarioApunte {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioApunteImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void registrar(UsuarioApunte usuarioApunte) {
        sessionFactory.getCurrentSession().save(usuarioApunte);
    }

    @Override
    public List<UsuarioApunte> obtenerApuntesPorIdUsuario(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UsuarioApunte.class)
                .createAlias("usuario", "a")
                .add(Restrictions.eq("a.id", id));
        return criteria.list();
    }
    @Override
    public List<UsuarioApunte> obtenerApuntesDeOtrosUsuarios(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UsuarioApunte.class)
                .createAlias("usuario", "u")
                .add(Restrictions.not(Restrictions.eq("u.id", id)));
        return criteria.list();
    }

}
