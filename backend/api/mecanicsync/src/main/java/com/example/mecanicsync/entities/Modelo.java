package com.example.mecanicsync.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="modelos")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idModelo;


    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;
    private String modelo;
    private String tipoMotor;

    @Column(name = "anio_inicio")
    private Integer anioInicio;

    @OneToMany(mappedBy = "modelo")
    private List<Vehiculo> vehiculos;
}
