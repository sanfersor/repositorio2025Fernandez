package com.example.mecanicsync.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Integer idUsuario;
    private String usuario;
    private String email;
    private String rol; // Nombre del rol
}