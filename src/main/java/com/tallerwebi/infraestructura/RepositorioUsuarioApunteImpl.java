package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.Usuario;
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

    public List<UsuarioApunte> obtenerUsuarioPorIdDeApunte(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT ua FROM UsuarioApunte ua " +
                "WHERE ua.apunte.id = :apunteId";

        Query<UsuarioApunte> query = session.createQuery(jpql, UsuarioApunte.class);
        query.setParameter("apunteId", id);

        return query.getResultList();
    }

    public TipoDeAcceso obtenerTipoDeAccesoPorIdsDeUsuarioYApunte(Long idUsuario, Long idApunte) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT ua.tipoDeAcceso FROM UsuarioApunte ua " +
                "WHERE ua.usuario.id = :userId AND ua.apunte.id = :apunteId";

        Query<TipoDeAcceso> query = session.createQuery(jpql, TipoDeAcceso.class);
        query.setParameter("userId", idUsuario);
        query.setParameter("apunteId", idApunte);

        return query.getResultList().get(0);
    }

}
