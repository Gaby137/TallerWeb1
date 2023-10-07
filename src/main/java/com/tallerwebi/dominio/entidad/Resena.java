package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String descripcion;
    private int cantidadDeEstrellas;
    private Date created_at;

    @OneToOne(mappedBy = "resena")
    private UsuarioApunteResena usuarioResenaApunte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadDeEstrellas() {
        return cantidadDeEstrellas;
    }

    public void setCantidadDeEstrellas(int cantidadDeEstrellas) {
        this.cantidadDeEstrellas = cantidadDeEstrellas;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
