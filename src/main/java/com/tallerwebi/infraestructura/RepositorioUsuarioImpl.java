package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.iRepositorio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioUsuario")
public class RepositorioUsuarioImpl implements RepositorioUsuario {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario buscarUsuario(String email, String password) {

        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public Usuario buscarUsuario(String email) {
        final Session session = sessionFactory.getCurrentSession();
        return (Usuario) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void guardar(Usuario usuario) {
        sessionFactory.getCurrentSession().save(usuario);
    }

    @Override
    public Usuario buscar(String email) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class) /*el create criteria selecciona la tabla*/
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public void modificar(Usuario usuario) {
        sessionFactory.getCurrentSession().update(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return (Usuario) sessionFactory.getCurrentSession().createCriteria(Usuario.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<Usuario> buscarPorIdATodosLosUsuariosMenosAlUsuarioActual(Long id) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT u FROM Usuario u " +
                "WHERE u.id <> :usuarioId";

        Query<Usuario> query = session.createQuery(jpql, Usuario.class);
        query.setParameter("usuarioId", id);

        return query.getResultList();
    }

    @Override
    public boolean existeCodigoCreadorEnLaBaseDeDatos(String codigoCreador) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT u FROM Usuario u " +
                "WHERE u.codigoDeCreador = :codigoCreador";

        Query<Usuario> query = session.createQuery(jpql, Usuario.class);
        query.setParameter("codigoCreador", codigoCreador);

        return query.setMaxResults(1).uniqueResult() != null;
    }

    @Override
    public Usuario buscarUsuarioPorCodigoCreador(String codigoCreador) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT u FROM Usuario u " +
                "WHERE u.codigoDeCreador = :codigoCreador";

        Query<Usuario> query = session.createQuery(jpql, Usuario.class);
        query.setParameter("codigoCreador", codigoCreador);

        return query.setMaxResults(1).uniqueResult();
    }
    @Override
    public boolean obtenerEstadoPopupPorIdUsuario(Long idUsuario) {
        Session session = sessionFactory.getCurrentSession();

        String jpql = "SELECT u.queAparezcaPopUpDeCodigoCreador FROM Usuario u " +
                "WHERE u.id = :idUsuario";

        Query<Boolean> query = session.createQuery(jpql, Boolean.class);
        query.setParameter("idUsuario", idUsuario);

        Boolean estadoPopup = query.uniqueResult();

        return estadoPopup != null ? estadoPopup : false;
    }

}
