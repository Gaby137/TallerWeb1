package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private int puntos;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    private Boolean activo = false;
    private String fotoPerfil;
    private Date created_at;
    private Date updated_at;
    @OneToMany(mappedBy = "usuario")
    private List<UsuarioApunte> relacionesUsuarioApunte = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private UsuarioApunteResena usuarioResenaApunte;

    public Usuario(){}

    public Usuario(String nombre, String apellido, int puntos, String email, String password, Rol rol, Boolean activo, String fotoPerfil, Date created_at, Date updated_at) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntos = puntos;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.fotoPerfil = fotoPerfil;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public boolean activo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public List<UsuarioApunte> getRelacionesUsuarioApunte() {
        return relacionesUsuarioApunte;
    }

    public void setRelacionesUsuarioApunte(List<UsuarioApunte> relacionesUsuarioApunte) {
        this.relacionesUsuarioApunte = relacionesUsuarioApunte;
    }

    public UsuarioApunteResena getUsuarioResenaApunte() {
        return usuarioResenaApunte;
    }

    public void setUsuarioResenaApunte(UsuarioApunteResena usuarioResenaApunte) {
        this.usuarioResenaApunte = usuarioResenaApunte;
    }
}
