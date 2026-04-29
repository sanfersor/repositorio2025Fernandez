package com.example.mecanicsync.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CrossOrigin("*")
@Entity
@Table(name="clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    private String cliente;
    private String telefono;
    private String email;
    private String direccion;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @OneToMany(
            mappedBy = "cliente",cascade = CascadeType.ALL,orphanRemoval = true
    )
    private List<Vehiculo> vehiculos;
}
