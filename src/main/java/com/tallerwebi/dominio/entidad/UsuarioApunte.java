package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@Table(name = "usuario_apunte")
public class UsuarioApunte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "apuntes_id", referencedColumnName = "id")
    private Apunte apunte;
    @Enumerated(EnumType.STRING)
    private TipoDeAcceso tipoDeAcceso;

    public UsuarioApunte(Usuario usuario, Apunte apunte) {
        this.usuario = usuario;
        this.apunte = apunte;
    }

    public UsuarioApunte() {
    }

    public UsuarioApunte(Apunte apunte, TipoDeAcceso tipoDeAcceso) {
        this.apunte = apunte;
        this.tipoDeAcceso = tipoDeAcceso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Apunte getApunte() {
        return apunte;
    }

    public void setApunte(Apunte apunte) {
        this.apunte = apunte;
    }

    public TipoDeAcceso getTipoDeAcceso() {
        return tipoDeAcceso;
    }

    public void setTipoDeAcceso(TipoDeAcceso tipoDeAcceso) {
        this.tipoDeAcceso = tipoDeAcceso;
    }
}
