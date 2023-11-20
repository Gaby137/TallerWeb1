package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Carrera;
import com.tallerwebi.dominio.entidad.MateriaCarrera;
import com.tallerwebi.dominio.entidad.TipoDeAcceso;
import com.tallerwebi.dominio.entidad.UsuarioApunte;
import com.tallerwebi.dominio.iRepositorio.RepositorioMateriaCarrera;
import com.tallerwebi.dominio.iRepositorio.RepositorioUsuarioApunte;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioMateriaCarrera")
public class RepositorioMateriaCarreraImpl implements RepositorioMateriaCarrera {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMateriaCarreraImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void registrar(MateriaCarrera materiaCarrera) {

        sessionFactory.getCurrentSession().save(materiaCarrera);
    }

    @Override
    public List<MateriaCarrera> obtenerTodasLasMaterias() {
        return sessionFactory.getCurrentSession()
                .createCriteria(MateriaCarrera.class)
                .list();

    }


}
