package com.example.mecanicsync.dto;

import com.example.mecanicsync.entities.Reparacion;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReparacionDTO {

    @JsonProperty("id_reparacion")
    private Integer idReparacion;

    @JsonProperty("id_vehiculo")
    private Integer idVehiculo;

    @JsonProperty("matricula")
    private String matricula;

    private String marca;     // nombre marca
    private String modelo;    // nombre modelo

    @JsonProperty("id_tipo")
    private Integer idTipoReparacion;

    @JsonProperty("reparacion")
    private String nombreTipoReparacion;
    @JsonProperty("descripcion")
    private String descripcionTipoReparacion;


    @JsonProperty("descripcion_adicional")
    private String descripcionAdicional;

    private String estado;

    @JsonProperty("fecha_inicio")
    private LocalDate fechaInicio;

    @JsonProperty("fecha_fin")
    private LocalDate fechaFin;

    @JsonProperty("horas_reales")
    private Double horasReales;

    @JsonProperty("importe_total")
    private Double importeTotal;

    // Constructor desde entidad
    public ReparacionDTO(Reparacion r) {
        this.idReparacion = r.getIdReparacion();
        this.idVehiculo = r.getVehiculo().getId();
        this.matricula = r.getVehiculo().getMatricula();

        // ACCESO CORRECTO: Vehiculo → Modelo → Marca
        this.marca = r.getVehiculo().getModelo().getMarca().getMarca();
        this.modelo = r.getVehiculo().getModelo().getModelo();

        this.idTipoReparacion = r.getTipo().getId();
        this.nombreTipoReparacion = r.getTipo().getReparacion();
        this.descripcionTipoReparacion = r.getTipo().getDescripcion();

        this.descripcionAdicional = r.getDescripcionAdicional();
        this.estado = r.getEstado();
        this.fechaInicio = r.getFechaInicio();
        this.fechaFin = r.getFechaFin();
        this.horasReales = r.getHorasReales();
        this.importeTotal = r.getImporteTotal();
    }
}