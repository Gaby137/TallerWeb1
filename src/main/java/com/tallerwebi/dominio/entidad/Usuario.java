package com.tallerwebi.dominio.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
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
    private Boolean queAparezcaPopUpDeCodigoCreador;
    private String fotoPerfil;
    private Double latitud;
    private Double longitud;
    private String codigoDeCreador;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> flagsDeParticipacionContinua;
    private Date created_at;
    private Date updated_at;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<UsuarioApunte> relacionesUsuarioApunte = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private UsuarioApunteResena usuarioResenaApunte;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, int puntos, String email, String password, Rol rol, Boolean activo,
            String fotoPerfil, Double latitud, Double longitud, String codigoDeCreador, Date created_at,
            Date updated_at) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puntos = puntos;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.fotoPerfil = fotoPerfil;
        this.latitud = latitud;
        this.longitud = longitud;
        this.flagsDeParticipacionContinua = new HashSet<>();
        this.codigoDeCreador = codigoDeCreador;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Usuario(Long id) {
        this.id = id;
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

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Set<String> getFlagsDeParticipacionContinua() {
        return flagsDeParticipacionContinua;
    }

    public void setFlagsDeParticipacionContinua(Set<String> flagsDeParticipacionContinua) {
        this.flagsDeParticipacionContinua = flagsDeParticipacionContinua;
    }

    public String getCodigoDeCreador() {
        return codigoDeCreador;
    }

    public void setCodigoDeCreador(String codigoDeCreador) {
        this.codigoDeCreador = codigoDeCreador;
    }

    public Boolean getQueAparezcaPopUpDeCodigoCreador() {
        return queAparezcaPopUpDeCodigoCreador;
    }

    public void setQueAparezcaPopUpDeCodigoCreador(Boolean queAparezcaPopUpDeCodigoCreador) {
        this.queAparezcaPopUpDeCodigoCreador = queAparezcaPopUpDeCodigoCreador;
    }
}
