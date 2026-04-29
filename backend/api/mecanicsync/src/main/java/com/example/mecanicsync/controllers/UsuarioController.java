package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.LoginRequest;
import com.example.mecanicsync.dto.UsuarioDTO;
import com.example.mecanicsync.entities.Rol;
import com.example.mecanicsync.entities.Usuario;
import com.example.mecanicsync.repositories.UsuarioRepository;
import com.example.mecanicsync.repositories.RolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    // Convertir Usuario a UsuarioDTO
    private UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .usuario(usuario.getUsuario())
                .email(usuario.getEmail())
                .rol(usuario.getRol().getRol())
                .build();
    }

    // Listar todos los usuarios
    @GetMapping
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(toDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> getByEmail(@PathVariable String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(usuario));
    }
    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @PathVariable Integer id,
            @RequestBody UsuarioDTO dto
    ) {
        return usuarioRepository.findById(id)
                .map(usuario -> {

                    usuario.setUsuario(dto.getUsuario());
                    usuario.setEmail(dto.getEmail());

                    if (dto.getRol() != null) {
                        Rol rol = rolRepository.findByRolIgnoreCase(dto.getRol())
                                .orElseThrow(() ->
                                        new RuntimeException("Rol no encontrado: " + dto.getRol())
                                );
                        usuario.setRol(rol);
                    }

                    Usuario actualizado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(toDTO(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint de login básico
    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
        if (usuario == null || !usuario.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        return ResponseEntity.ok(toDTO(usuario));
    }
    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO dto) {

        // Validar email duplicado
        if (usuarioRepository.findByEmail(dto.getEmail()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build(); // 409
        }

        // Buscar rol
        Rol rol = rolRepository.findByRolIgnoreCase(dto.getRol())
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado: " + dto.getRol())
                );

        // Crear entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(rol);


        usuario.setPassword("1234");


        Usuario guardado = usuarioRepository.save(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toDTO(guardado));
    }

}
