package com.tallerwebi.presentacion;

public class DatosPuntaje {

    private String usuario;
    private int puntos;

    public DatosPuntaje(String usuario, int puntos) {
        this.usuario = usuario;
        this.puntos = puntos;
    }

    public DatosPuntaje(String string, String string2, String string3) {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
