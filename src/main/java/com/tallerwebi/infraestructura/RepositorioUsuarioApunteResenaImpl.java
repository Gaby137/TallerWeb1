package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.UsuarioApunteResena;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunteResena;
 
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
