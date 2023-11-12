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
}
