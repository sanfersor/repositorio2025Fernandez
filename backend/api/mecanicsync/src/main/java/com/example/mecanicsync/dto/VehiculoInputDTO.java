package com.example.mecanicsync.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoInputDTO {

    // Estos nombres de campo coinciden directamente con el JSON de Android
    private Integer id_cliente;
    private Integer id_modelo;
    private String matricula;
    private Integer anio_matriculacion;
}
