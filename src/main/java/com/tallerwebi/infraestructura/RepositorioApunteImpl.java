package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Apunte;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioApunte;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
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
            .add(Restrictions.between("created_at", desde, hasta)).addOrder(Order.desc("created_at"));
        return criteria.list();
    }

    @Override
    public List<Apunte> filtrar(Long idCarrera, Long idMateria) {
        Session session = sessionFactory.getCurrentSession();
        if (idCarrera == 0 && idMateria == 0){
            String jpql = "SELECT apunte FROM Apunte apunte";
            Query<Apunte> query = session.createQuery(jpql, Apunte.class);
            return query.getResultList();
        }else if (idCarrera != 0 && idMateria == 0){
            String jpql = "SELECT apunte FROM Apunte apunte " +
                    "JOIN apunte.materia materia " +
                    "JOIN materia.relacionesMateriaCarrera relacion " +
                    "WHERE relacion.carrera.id = :idCarrera";

            Query<Apunte> query = session.createQuery(jpql, Apunte.class);
            query.setParameter("idCarrera", idCarrera);
            return query.getResultList();
        }else {
            // Obtener todos los apuntes relacionados con una carrera y materia específicas
            String jpql = "SELECT apunte FROM Apunte apunte " +
                    "JOIN apunte.materia materia " +
                    "JOIN materia.relacionesMateriaCarrera relacionCarrera " +
                    "WHERE relacionCarrera.carrera.id = :idCarrera AND materia.id = :idMateria";

            Query<Apunte> query = session.createQuery(jpql, Apunte.class);
            query.setParameter("idCarrera", idCarrera);
            query.setParameter("idMateria", idMateria);
            return query.getResultList();
        }
    }
}
