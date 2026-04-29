package com.example.mecanicsync.controllers;


import com.example.mecanicsync.dto.MarcaConModelosDTO;
import com.example.mecanicsync.dto.MarcaDTO;
import com.example.mecanicsync.dto.ModeloDTO;
import com.example.mecanicsync.entities.Marca;
import com.example.mecanicsync.repositories.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/marcas")
public class MarcaController {
    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    // GET /api/marcas → lista todas las marcas
    public List<MarcaDTO> getAll() {
        return marcaRepository.findAll()
                .stream()
                .map(m -> new MarcaDTO(m.getIdMarca(), m.getMarca()))
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<MarcaDTO> getById(@PathVariable Integer id) {
        return marcaRepository.findById(id)
                .map(marca -> ResponseEntity.ok(toDTO(marca)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/con-modelos")
    public List<MarcaConModelosDTO> getAllWithModelos() {
        return marcaRepository.findAll()
                .stream()
                .map(m -> MarcaConModelosDTO.builder()
                        .idMarca(m.getIdMarca())
                        .marca(m.getMarca())
                        .modelos(m.getModelos().stream()
                                .map(mod -> new ModeloDTO(mod.getIdModelo(), mod.getModelo(), mod.getTipoMotor(), mod.getAnioInicio(), m.getIdMarca()))
                                .toList())
                        .build())
                .toList();
    }
    @GetMapping("/{id}/con-modelos")
    public ResponseEntity<MarcaConModelosDTO> getByIdWithModelos(@PathVariable Integer id) {
        return marcaRepository.findById(id)
                .map(marca -> {
                    MarcaConModelosDTO dto = MarcaConModelosDTO.builder()
                            .idMarca(marca.getIdMarca())
                            .marca(marca.getMarca())
                            .modelos(marca.getModelos().stream()
                                    .map(mod -> ModeloDTO.builder()
                                            .idModelo(mod.getIdModelo())
                                            .modelo(mod.getModelo())
                                            .tipoMotor(mod.getTipoMotor())
                                            .anioInicio(mod.getAnioInicio())
                                            .idMarca(marca.getIdMarca())
                                            .build())
                                    .toList())
                            .build();
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Convertir Marca a MarcaDTO
    private MarcaDTO toDTO(Marca marca) {
        return MarcaDTO.builder()
                .idMarca(marca.getIdMarca())
                .marca(marca.getMarca())
                .build();
    }
}
