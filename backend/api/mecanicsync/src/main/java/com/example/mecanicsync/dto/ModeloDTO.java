package com.example.mecanicsync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeloDTO {
    private Integer idModelo;
    private String modelo;
    private String tipoMotor;
    private Integer anioInicio;
    private Integer idMarca;
}

