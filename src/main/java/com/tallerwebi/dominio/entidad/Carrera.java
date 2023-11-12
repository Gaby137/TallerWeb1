package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Date created_at;
    private Date updated_at;

    @OneToMany(mappedBy = "carrera")
    private List<MateriaCarrera> relacionesMateriaCarrera = new ArrayList<>();


    public Carrera() {

    }

    public Carrera(String descripcion, Date created_at, Date updated_at) {
        this.descripcion = descripcion;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
