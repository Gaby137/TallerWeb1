package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
@Table(name = "materia_carrera")
public class MateriaCarrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "materia_id", referencedColumnName = "id")
    private Materia materia;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrera_id", referencedColumnName = "id")
    private Carrera carrera;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }
}
