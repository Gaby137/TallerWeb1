package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Apunte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pathArchivo;
    private String nombre;
    private String descripcion;
    private int precio;
    private Date created_at;
    private Date updated_at;
    private boolean cienPuntosPorBuenPromedioDeResenas;
    private boolean sePuedeComprar;
    private boolean activo;

    @OneToMany(mappedBy = "apunte")
    private List<UsuarioApunte> relacionesUsuarioApunte = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name="materia_id",nullable = true)
    private Materia materia;


    public Apunte(String pathArchivo, String nombre, String descripcion, int precio, Date created_at, Date updated_at) {
        this.pathArchivo = pathArchivo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio=precio;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Apunte(String nombre) {
        this.nombre = nombre;
    }

    public Apunte() {

    }

    public Apunte(String apunte, Long id) {
        this.nombre = apunte;
        this.id = id;
    }

    public Apunte(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public List<UsuarioApunte> getRelacionesUsuarioApunte() {
        return relacionesUsuarioApunte;
    }

    public void setRelacionesUsuarioApunte(List<UsuarioApunte> relacionesUsuarioApunte) {
        this.relacionesUsuarioApunte = relacionesUsuarioApunte;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public boolean isCienPuntosPorBuenPromedioDeResenas() {
        return cienPuntosPorBuenPromedioDeResenas;
    }

    public void setCienPuntosPorBuenPromedioDeResenas(boolean cienPuntosPorBuenPromedioDeResenas) {
        this.cienPuntosPorBuenPromedioDeResenas = cienPuntosPorBuenPromedioDeResenas;
    }

    public boolean isSePuedeComprar() {
        return sePuedeComprar;
    }

    public void setSePuedeComprar(boolean sePuedeComprar) {
        this.sePuedeComprar = sePuedeComprar;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
