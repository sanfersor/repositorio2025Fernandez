package com.example.mecanicsync.repositories;


import com.example.mecanicsync.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Integer> {

    List<Modelo> findByMarcaIdMarca(Integer idMarca);
}
