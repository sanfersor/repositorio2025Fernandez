package com.example.mecanicsync.repositories;


import com.example.mecanicsync.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.reparaciones r WHERE r.estado <> 'Entregado'")
    List<Vehiculo> findVehiculosInTaller();
    // Buscar vehículo por matrícula
    Vehiculo findByMatricula(String matricula);

    // Buscar vehículos de un cliente
    List<Vehiculo> findByClienteIdCliente(Integer idCliente);
}
