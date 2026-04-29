package com.example.mecanicsync.repositories;

import com.example.mecanicsync.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // 1. Filtrar para encontrar clientes que tienen al menos un vehículo
    // con una reparación cuyo estado NO es 'Entregado'.
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.vehiculos v JOIN v.reparaciones r WHERE r.estado <> 'Entregado'")
    List<Cliente> findClientesInTaller();
    // 2. Contar la CANTIDAD de CLIENTES ÚNICOS con vehículos en taller.
    // DISTINCT cuenta cada cliente una sola vez, incluso si tiene varios vehículos en reparación.
    @Query("SELECT COUNT(DISTINCT c) FROM Cliente c JOIN c.vehiculos v JOIN v.reparaciones r WHERE r.estado <> 'Entregado'")
    Long countClientesInTaller();

    // 3. Contar la CANTIDAD de VEHÍCULOS ÚNICOS con reparaciones en taller.
    // DISTINCT cuenta cada vehículo una sola vez, incluso si tiene varias reparaciones pendientes.
    @Query("SELECT COUNT(DISTINCT v) FROM Cliente c JOIN c.vehiculos v JOIN v.reparaciones r WHERE r.estado <> 'Entregado'")
    Long countVehiculosInTaller();
}
