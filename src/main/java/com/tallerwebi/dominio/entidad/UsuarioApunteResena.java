package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@Table(name = "usuario_apunte_resena")
public class UsuarioApunteResena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resena_id", referencedColumnName = "id")
    private Resena resena;

    @ManyToOne
    @JoinColumn(name = "apunte_id", referencedColumnName = "id")
    private Apunte apunte;
    
    public UsuarioApunteResena() {};
    
    public UsuarioApunteResena(Usuario usuario, Resena resena, Apunte apunte) {
    	this.usuario = usuario;
    	this.resena = resena;
    	this.apunte = apunte;
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

    public Resena getResena() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }

    public Apunte getApunte() {
        return apunte;
    }

    public void setApunte(Apunte apunte) {
        this.apunte = apunte;
    }
}