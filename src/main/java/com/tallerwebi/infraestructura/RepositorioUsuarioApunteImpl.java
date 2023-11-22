package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository("repositorioUsuarioApunte")
public class RepositorioUsuarioApunteImpl implements RepositorioUsuarioApunte {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioApunteImpl(SessionFactory sessionFactory) {
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
                "WHERE ua.usuario.id != :userId";

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

        System.out.println("Ejecutando consulta para idUsuario=" + idUsuario + " e idApunte=" + idApunte);

        List<TipoDeAcceso> resultados = query.getResultList();

        if (!resultados.isEmpty()) {
            return resultados.get(0);
        } else {
            System.out.println("No se encontró una relación entre el usuario y el apunte.");
            return null;
        }
    }

    @Override
    public void eliminarRelacionUsuarioApuntePorId(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "DELETE FROM UsuarioApunte ua " +
                "WHERE ua.id = :usuarioApunteId";

        Query query = session.createQuery(jpql);
        query.setParameter("usuarioApunteId", id);

        query.executeUpdate();
    }

    @Override
    public List<UsuarioApunte> obtenerRelacionesUsuarioApuntePorIdDeApunte(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT ua FROM UsuarioApunte ua " +
                "WHERE ua.apunte.id = :apunteId";

        Query<UsuarioApunte> query = session.createQuery(jpql, UsuarioApunte.class);
        query.setParameter("apunteId", id);

        return query.getResultList();
    }
    @Override
    public boolean existeRelacionUsuarioApunteLeerAsociadaAIdDeApunte(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT COUNT(ua) > 0 FROM UsuarioApunte ua " +
                "WHERE ua.apunte.id = :apunteId " +
                "AND ua.tipoDeAcceso = :tipoAcceso";

        Query<Boolean> query = session.createQuery(jpql, Boolean.class);
        query.setParameter("apunteId", id);
        query.setParameter("tipoAcceso", TipoDeAcceso.LEER);

        return query.getSingleResult();
    }

    @Override
    public boolean existeRelacionUsuarioApunteEditar(Long idUsuario, Long idApunte) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT COUNT(ua) > 0 FROM UsuarioApunte ua " +
                "WHERE ua.usuario.id = :usuarioId " +
                "AND ua.apunte.id = :apunteId " +
                "AND ua.tipoDeAcceso = :tipoAcceso";

        Query<Boolean> query = session.createQuery(jpql, Boolean.class);
        query.setParameter("usuarioId", idUsuario);
        query.setParameter("apunteId", idApunte);
        query.setParameter("tipoAcceso", TipoDeAcceso.EDITAR);

        return query.getSingleResult();
    }


}
