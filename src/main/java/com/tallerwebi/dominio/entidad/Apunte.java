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
    private String rutaArchivo;
    private String titulo;
    private String descripcion;
    private int precio;
    private Date fecha_creacion;
    private Date fecha_actualizacion;

    @OneToMany(mappedBy = "apunte")
    private List<UsuarioApunte> relacionesUsuarioApunte = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "materia_id", referencedColumnName = "id")
    private Materia materia;


    public Apunte(String rutaArchivo, String titulo, String descripcion, int precio, Date fecha_creacion, Date fecha_actualizacion) {
        this.rutaArchivo = rutaArchivo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fecha_creacion = fecha_creacion;
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public Apunte(String titulo) {
        this.titulo = titulo;
    }

    public Apunte() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathArchivo() {
        return rutaArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.rutaArchivo = pathArchivo;
    }

    public String getNombre() {
        return titulo;
    }

    public void setNombre(String nombre) {
        this.titulo = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getCreated_at() {
        return fecha_creacion;
    }

    public void setCreated_at(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Date getUpdated_at() {
        return fecha_actualizacion;
    }

    public void setUpdated_at(Date fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
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
}
