package com.example.mecanicsync.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Integer idCliente;
    private String cliente;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaAlta;
    private List<VehiculoDTO> vehiculos; // lista de vehículos del cliente
}