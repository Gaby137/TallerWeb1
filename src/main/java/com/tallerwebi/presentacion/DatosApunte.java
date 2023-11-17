package com.tallerwebi.presentacion;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class DatosApunte {
    private MultipartFile pathArchivo;
    @NotEmpty(message = "El nombre no puede estar en blanco.")
    private String nombre;
    @NotEmpty(message = "La descripcion no puede estar en blanco.")
    private String descripcion;
    @Min(value = 0, message = "El precio no puede ser menor que 0.")
    private int precio;
    private Long idMateria;
    public DatosApunte(MultipartFile pathArchivo, String nombre, String descripcion, int precio) {
        this.pathArchivo = pathArchivo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio=precio;
    }

    public DatosApunte(){

    }
    public MultipartFile getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(MultipartFile pathArchivo) {
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

    public Long getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Long idMateria) {
        this.idMateria = idMateria;
    }
}
