package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Resena;
import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository("repositorioUsuarioApunteResena")
public class RepositorioUsuarioApunteResenaImpl implements RepositorioUsuarioApunteResena {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioUsuarioApunteResenaImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardar(UsuarioApunteResena usuarioApunteResena) {
        sessionFactory.getCurrentSession().save(usuarioApunteResena);
    }

    @Override
    public List<Resena> obtenerResenasPorIdApunte(Long idApunte) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT uar.resena FROM UsuarioApunteResena uar WHERE uar.apunte.id = :apunteId";
        TypedQuery<Resena> query = session.createQuery(hql, Resena.class);
        query.setParameter("apunteId", idApunte);
        List<Resena> resenas = query.getResultList();

        return resenas;
    }


    @Override
    public void registrar(UsuarioApunteResena usuarioApunteResena) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrar'");
    }


    @Override
    public List<UsuarioApunteResena> obtenerResenasPorUsuario(Long idUsuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerResenasPorUsuario'");
    }


    @Override
    public List<UsuarioApunteResena> obtenerResenasPorApunte(Long idApunte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerResenasPorApunte'");
    }
}
