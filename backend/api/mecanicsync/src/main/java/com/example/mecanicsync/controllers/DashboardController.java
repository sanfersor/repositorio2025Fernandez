package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.DashboardDTO;
import com.example.mecanicsync.repositories.ClienteRepository;
import com.example.mecanicsync.repositories.ReparacionRepository;
import com.example.mecanicsync.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ReparacionRepository reparacionRepository;

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard() {
        long totalClientes = clienteRepository.count();
        long totalVehiculos = vehiculoRepository.count();
        long totalReparaciones = reparacionRepository.count();
        long pendientes = reparacionRepository.countByEstado("Pendiente");
        long enProceso = reparacionRepository.countByEstado("En proceso");
        long finalizadas = reparacionRepository.countByEstado("Finalizada");

        return new DashboardDTO(totalClientes, totalVehiculos, totalReparaciones, pendientes, enProceso, finalizadas);
    }

}
