package com.tallerwebi.presentacion;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class DatosCarrera {
    @NotEmpty(message = "El nombre de la carrera no puede estar en blanco.")
    @Pattern(regexp = "^[A-Z].*", message = "Debe empezar con una letra mayúscula.")
    private String descripcion;

    public DatosCarrera(){

    }
    public DatosCarrera(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
