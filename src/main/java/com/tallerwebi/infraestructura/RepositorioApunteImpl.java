package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("repositorioApunte")
public class RepositorioApunteImpl implements RepositorioApunte {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioApunteImpl(SessionFactory sessionFactory) {
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
    public Apunte obtenerApuntePorIdResena(Long idResena) {
        return (Apunte) sessionFactory.getCurrentSession().createQuery(
                "select a " +
                        "from Apunte a " +
                        "join usuarioApunteResena uar on uar.apunte_id = a.id " +
                        "where uar.resena_id = :idResena ",
                Apunte.class)
                .setParameter("idResena", idResena)
                .uniqueResult();
    }

    
    @Override
    public List<UsuarioApunteResena> getListadoDeResenasConSusUsuariosPorIdApunte(Long idApunte) {
        return sessionFactory.getCurrentSession().createCriteria(UsuarioApunteResena.class)
            .add(Restrictions.eq("apunte.id", idApunte))
            .list();
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

    @Override
    public List<Apunte> obtenerApuntesEntreFechas(Date desde, Date hasta) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Apunte.class)
            .add(Restrictions.between("created_at", desde, hasta));
        return criteria.list();
    }
}
