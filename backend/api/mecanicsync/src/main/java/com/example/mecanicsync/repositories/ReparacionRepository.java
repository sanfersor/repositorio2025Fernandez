package com.example.mecanicsync.repositories;

import com.example.mecanicsync.entities.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {

    // Metodo que ya tenías: Obtener reparaciones de un vehículo específico
    List<Reparacion> findByVehiculo_Id(Integer idVehiculo);

    // Metodo que ya tenías: Obtener reparaciones por estado
    List<Reparacion> findByEstado(String estado);

    // Contadores para el Dashboard:

    // 1. Contar el total de todas las reparaciones
    long count();

    // 2. Contar por estado
    long countByEstado(String estado);
}