package com.tallerwebi.presentacion;

public class DatosApunte {

    private String rutaArchivo;
    private String titulo;
    private String descripcion;

    private int precio;
    public DatosApunte(String rutaArchivo, String titulo, String descripcion, int precio) {
        this.rutaArchivo = rutaArchivo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public DatosApunte(){

    }
    public String getPathArchivo() {
        return rutaArchivo;
    }

    public void setPathArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getNombre() {
        return titulo;
    }

    public void setNombre(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
