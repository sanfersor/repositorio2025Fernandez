package com.example.mecanicsync.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarcaConModelosDTO {
    private Integer idMarca;
    private String marca;
    private List<ModeloDTO> modelos;
}
