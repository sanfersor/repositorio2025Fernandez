package com.example.mecanicsync.repositories;

import com.example.mecanicsync.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    List<Marca> findAll();
}
