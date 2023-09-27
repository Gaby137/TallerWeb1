package com.tallerwebi.presentacion;

import java.util.Date;

public class DatosApunte {

    private String pathArchivo;
    private String nombre;
    private String descripcion;
    public DatosApunte(String pathArchivo, String nombre, String descripcion) {
        this.pathArchivo = pathArchivo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public DatosApunte(){

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



}
