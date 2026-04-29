package com.example.mecanicsync.repositories;

import com.example.mecanicsync.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolIgnoreCase(String rol);
}
