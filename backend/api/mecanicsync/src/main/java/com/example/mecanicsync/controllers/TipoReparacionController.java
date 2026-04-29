package com.example.mecanicsync.controllers;

import com.example.mecanicsync.entities.TipoReparacion;
import com.example.mecanicsync.repositories.TipoReparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class TipoReparacionController {

    @Autowired
    private TipoReparacionRepository tipoReparacionRepository;

    @GetMapping("/tipos_reparacion")
    public List<TipoReparacion> getTiposReparacion() {
        return tipoReparacionRepository.findAll();
    }
}
