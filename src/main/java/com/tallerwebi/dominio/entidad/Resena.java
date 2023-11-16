package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

@Entity
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @NotEmpty(message = "La descripcion no puede estar en blanco.")
    private String descripcion;
    @Min(value = 1, message = "La calificación debe ser al menos 1.")
    @Max(value = 5, message = "La calificación no puede ser mayor de 5.")
    private int cantidadDeEstrellas;
    private Date created_at;

    @OneToOne(mappedBy = "resena", cascade = CascadeType.ALL)
    private UsuarioApunteResena usuarioResenaApunte;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resena resena = (Resena) obj;
        return Objects.equals(id, resena.id);
    }

    public Resena(int cantidadDeEstrellas) {
        this.cantidadDeEstrellas=cantidadDeEstrellas;
    }
    public Resena(){

    }

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

    public UsuarioApunteResena getUsuarioResenaApunte() {
        return this.usuarioResenaApunte;
    }

    public void setUsuarioResenaApunte(UsuarioApunteResena usuarioResenaApunte) {
        this.usuarioResenaApunte = usuarioResenaApunte;
    }
}
