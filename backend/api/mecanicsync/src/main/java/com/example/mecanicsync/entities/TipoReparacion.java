package com.example.mecanicsync.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tipos_reparacion")
public class TipoReparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipo;

    private String reparacion;
    private String descripcion;

    @Column(name = "horas_estimadas")
    private Double horasEstimadas;

    @Column(name = "precio_hora")
    private Double precioHora;

    @Column(name = "precio_materiales")
    private Double precioMateriales;

    @OneToMany(mappedBy = "tipo")
    @JsonIgnore
    private List<Reparacion> reparaciones;

    public Integer getId() {
        return idTipo;
    }
}