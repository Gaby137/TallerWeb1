package com.tallerwebi.presentacion;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class DatosMateria {
    @NotEmpty(message = "El nombre de la materia no puede estar en blanco.")
    @Pattern(regexp = "^[A-Z].*", message = "Debe empezar con una letra mayúscula.")
    private String descripcion;

    private Long idCarrera;

    public DatosMateria(){

    }
    public DatosMateria(String descripcion, Long idCarrera) {

        this.descripcion = descripcion;
        this.idCarrera = idCarrera;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Long idCarrera) {
        this.idCarrera = idCarrera;
    }
}
