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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public List<MateriaCarrera> getRelacionesMateriaCarrera() {
        return relacionesMateriaCarrera;
    }

    public void setRelacionesMateriaCarrera(List<MateriaCarrera> relacionesMateriaCarrera) {
        this.relacionesMateriaCarrera = relacionesMateriaCarrera;
    }
}
