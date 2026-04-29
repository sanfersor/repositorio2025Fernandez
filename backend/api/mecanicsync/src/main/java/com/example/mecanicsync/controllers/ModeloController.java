package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.ModeloDTO;
import com.example.mecanicsync.entities.Modelo;
import com.example.mecanicsync.repositories.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/modelos")
public class ModeloController {
    @Autowired
    private ModeloRepository modeloRepository;

    @GetMapping("/marca/{idMarca}")
    // Devuelve todos los modelos de una marca concreta
    public List<ModeloDTO> getByMarca(@PathVariable Integer idMarca) {
        return modeloRepository.findByMarcaIdMarca(idMarca)
                .stream()
                .map(m -> new ModeloDTO(
                        m.getIdModelo(),
                        m.getModelo(),
                        m.getTipoMotor(),
                        m.getAnioInicio(),
                        m.getMarca().getIdMarca()
                )).toList();
    }
}
