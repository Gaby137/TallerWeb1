package com.tallerwebi.presentacion;

public class DatosResena {

    private String descripcion;

    private int calificacion;


    public DatosResena(){

    }

    public DatosResena(String descripcion, int calificacion){
        this.descripcion=descripcion;
        this.calificacion=calificacion;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
