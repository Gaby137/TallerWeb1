package com.tallerwebi.presentacion;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class DatosApunte {
    @NotEmpty(message = "El path no puede estar en blanco.")
    private String pathArchivo;
    @NotEmpty(message = "El nombre no puede estar en blanco.")
    private String nombre;
    @NotEmpty(message = "La descripcion no puede estar en blanco.")
    private String descripcion;
    @Min(value = 0, message = "El precio no puede ser menor que 0.")
    private int precio;
    public DatosApunte(String pathArchivo, String nombre, String descripcion,  int precio) {
        this.pathArchivo = pathArchivo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio=precio;
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

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
