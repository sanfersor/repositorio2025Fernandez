package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.LoginRequest;
import com.example.mecanicsync.entities.Usuario;
import com.example.mecanicsync.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());

        if (usuario != null) {
            // Comprobar contraseña (en producción usar hashing)
            if (usuario.getPassword().equals(request.getPassword())) {
                response.put("success", true);
                response.put("role", usuario.getRol().getRol()); // Devuelve "Administrador" o "Mecánico"
                response.put("usuario", usuario.getUsuario());
            } else {
                response.put("success", false);
                response.put("message", "Contraseña incorrecta");
            }
        } else {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
        }

        return ResponseEntity.ok(response);
    }
}
