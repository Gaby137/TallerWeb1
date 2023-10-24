package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
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

    public List<UsuarioApunte> obtenerApuntesPorIdUsuario(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT ua FROM UsuarioApunte ua " +
                "WHERE ua.usuario.id = :userId";

        Query<UsuarioApunte> query = session.createQuery(jpql, UsuarioApunte.class);
        query.setParameter("userId", id);

        return query.getResultList();
    }
    public List<UsuarioApunte> obtenerApuntesDeOtrosUsuarios(Long userId) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT ua FROM UsuarioApunte ua " +
                "WHERE ua.usuario.id != :userId " +
                "AND ua.apunte.id NOT IN (SELECT ua2.apunte.id FROM UsuarioApunte ua2 WHERE ua2.usuario.id = :userId)";

        Query<UsuarioApunte> query = session.createQuery(jpql, UsuarioApunte.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Override
    public List<UsuarioApunte> obtenerUsuarioPorIdDeApunte(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(UsuarioApunte.class)
                .createAlias("apunte", "a")
                .add(Restrictions.eq("a.id", id));
        return criteria.list();
    }

}
