package com.example.mecanicsync.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private long clientesEnTaller;
    private long vehiculosEnTaller;
    private long totalReparaciones;
    private long pendientes;
    private long enProceso;
    private long finalizadas;
}
