package com.example.mecanicsync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReparacionesVehiculoDTO {

    private Integer idCliente;
    private String nombreCliente;

    private Integer idVehiculo;
    private String matricula;
    private String modelo;
    private String marca;

    private List<ReparacionDTO> reparaciones;
}

