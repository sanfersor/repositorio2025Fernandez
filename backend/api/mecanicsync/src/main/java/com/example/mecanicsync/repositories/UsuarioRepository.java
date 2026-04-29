package com.example.mecanicsync.repositories;

import com.example.mecanicsync.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Ejemplo: buscar por email
    Usuario findByEmail(String email);
}