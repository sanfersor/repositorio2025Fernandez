package com.example.mecanicsync.dto;

import com.example.mecanicsync.entities.Vehiculo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDTO {

    // PROPIEDADES DE SALIDA (Output)
    private Integer idVehiculo;
    private String matricula;
    private Integer anioMatriculacion;
    private String modelo; // Nombre del modelo
    private String marca;  // Nombre de la marca
    private List<ReparacionDTO> reparaciones;

    // CONSTRUCTOR LIGERO (para listados generales)
    // USADO EN: VehiculoController.toDTO(Vehiculo v)
    public VehiculoDTO(Vehiculo v) {
        this.idVehiculo = v.getId();
        this.matricula = v.getMatricula();
        // **CORRECCIÓN CLAVE:** Extrae el año de la entidad
        this.anioMatriculacion = v.getAnioMatriculacion();
        this.modelo = v.getModelo() != null ? v.getModelo().getModelo() : null;
        this.marca = (v.getModelo() != null && v.getModelo().getMarca() != null)
                ? v.getModelo().getMarca().getMarca()
                : null;
        this.reparaciones = Collections.emptyList();
    }

    // CONSTRUCTOR COMPLETO (para endpoints de detalle)
    // USADO EN: VehiculoController.toDTOWithReparations(Vehiculo v)
    public VehiculoDTO(Vehiculo v, boolean includeReparaciones) {
        this(v); // Llama al constructor ligero para los campos principales
        if (includeReparaciones && v.getReparaciones() != null) {
            this.reparaciones = v.getReparaciones().stream()
                    .map(ReparacionDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.reparaciones = Collections.emptyList();
        }
    }

}