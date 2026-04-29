package com.example.mecanicsync.controllers;

// DTO Imports actualizados (de .dto.output/input a .dto)
import com.example.mecanicsync.dto.ReparacionDTO;
import com.example.mecanicsync.dto.VehiculoDTO;
import com.example.mecanicsync.dto.VehiculoInputDTO;

// Entity Imports actualizados (de .entity a .entities)
import com.example.mecanicsync.entities.Cliente;
import com.example.mecanicsync.entities.Modelo;
import com.example.mecanicsync.entities.Reparacion;
import com.example.mecanicsync.entities.Vehiculo;

// Repository Imports actualizados (de .repository a .repositories)
import com.example.mecanicsync.repositories.ClienteRepository;
import com.example.mecanicsync.repositories.ModeloRepository;
import com.example.mecanicsync.repositories.ReparacionRepository;
import com.example.mecanicsync.repositories.VehiculoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/vehiculos")
public class VehiculoController {

    // Los tipos de campo se actualizan automáticamente con las nuevas importaciones
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private ReparacionRepository reparacionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @GetMapping
    public List<VehiculoDTO> getAll() {
        return vehiculoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VehiculoDTO getById(@PathVariable Integer id) {
        return vehiculoRepository.findById(id)
                .map(this::toDTOWithReparations)
                .orElse(null);
    }

    // GET /api/vehiculos/taller
    @GetMapping("/taller")
    public List<VehiculoDTO> getVehiculosInTaller() {
        // Asumiendo que vehiculoRepository tiene findVehiculosInTaller()
        return vehiculoRepository.findVehiculosInTaller().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/matricula/{matricula}")
    public VehiculoDTO getByMatricula(@PathVariable String matricula) {
        Vehiculo v = vehiculoRepository.findByMatricula(matricula);
        return v != null ? toDTO(v) : null;
    }

    @GetMapping("/cliente/{idCliente}")
    public List<VehiculoDTO> getByCliente(@PathVariable Integer idCliente) {
        // Asumiendo que vehiculoRepository tiene findByClienteIdCliente()
        return vehiculoRepository.findByClienteIdCliente(idCliente).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // METODO POST CORREGIDO PARA USAR VehiculoInputDTO
    @PostMapping
    public VehiculoDTO create(@RequestBody VehiculoInputDTO inputDTO) {
        // 1. Validar y Buscar las entidades por ID
        Cliente cliente = clienteRepository.findById(inputDTO.getId_cliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Modelo modelo = modeloRepository.findById(inputDTO.getId_modelo())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        // 2. Crear y configurar la Entidad JPA
        Vehiculo nuevoVehiculo = new Vehiculo();
        nuevoVehiculo.setCliente(cliente);
        nuevoVehiculo.setModelo(modelo);
        nuevoVehiculo.setMatricula(inputDTO.getMatricula());
        // El año se mapea correctamente desde el DTO de entrada
        nuevoVehiculo.setAnioMatriculacion(inputDTO.getAnio_matriculacion());

        // 3. Guardar y devolver DTO de salida
        return toDTO(vehiculoRepository.save(nuevoVehiculo));
    }

    @Transactional // Asegura que la operación de carga y guardado se haga en una sola sesión
    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> update(@PathVariable Integer id, @RequestBody VehiculoInputDTO inputDTO) {
        // 1. Cargar la entidad Vehiculo existente
        return vehiculoRepository.findById(id)
                .map(existingVehiculo -> {
                    // 2. Buscar las entidades relacionadas (Cliente y Modelo) por ID
                    Cliente cliente = clienteRepository.findById(inputDTO.getId_cliente())
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado para el ID: " + inputDTO.getId_cliente()));

                    Modelo modelo = modeloRepository.findById(inputDTO.getId_modelo())
                            .orElseThrow(() -> new RuntimeException("Modelo no encontrado para el ID: " + inputDTO.getId_modelo()));

                    // 3. Actualizar los campos de la entidad existente (el objeto 'existingVehiculo')
                    existingVehiculo.setCliente(cliente);
                    existingVehiculo.setModelo(modelo);
                    existingVehiculo.setMatricula(inputDTO.getMatricula());
                    existingVehiculo.setAnioMatriculacion(inputDTO.getAnio_matriculacion());

                    // 4. Guardar la entidad existente y devolver el DTO
                    return ResponseEntity.ok(toDTO(vehiculoRepository.save(existingVehiculo)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!vehiculoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vehiculoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // MÉTODO CORREGIDO: Ahora usa ReparacionDTO importado
    @GetMapping("/{id}/reparaciones")
    public ResponseEntity<List<ReparacionDTO>> obtenerReparacionesPorVehiculo(@PathVariable Integer id){
        // Asumiendo que reparacionRepository tiene findByVehiculo_Id(id)
        List<Reparacion> reparaciones = reparacionRepository.findByVehiculo_Id(id);

        if (reparaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Referencia a ReparacionDTO importada
        List<ReparacionDTO> dtos = reparaciones.stream()
                .map(ReparacionDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // MÉTODOS DE MAPEO (Usando el Builder y DTO de Salida)

    private VehiculoDTO toDTO(Vehiculo vehiculo) {
        // El DTO de salida (VehiculoDTO) solo incluye la información relevante
        return VehiculoDTO.builder()
                .idVehiculo(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .anioMatriculacion(vehiculo.getAnioMatriculacion())
                .modelo(vehiculo.getModelo() != null ? vehiculo.getModelo().getModelo() : null)
                .marca(vehiculo.getModelo() != null && vehiculo.getModelo().getMarca() != null
                        ? vehiculo.getModelo().getMarca().getMarca()
                        : null)
                .reparaciones(Collections.emptyList())
                .build();
    }

    private VehiculoDTO toDTOWithReparations(Vehiculo vehiculo) {
        // Mapear reparaciones: se asume que las reparaciones ya fueron cargadas
        List<ReparacionDTO> reparacionesDTO = vehiculo.getReparaciones().stream()
                .map(ReparacionDTO::new)
                .collect(Collectors.toList());

        return VehiculoDTO.builder()
                .idVehiculo(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .anioMatriculacion(vehiculo.getAnioMatriculacion())
                .modelo(vehiculo.getModelo() != null ? vehiculo.getModelo().getModelo() : null)
                .marca(vehiculo.getModelo() != null && vehiculo.getModelo().getMarca() != null
                        ? vehiculo.getModelo().getMarca().getMarca()
                        : null)
                .reparaciones(reparacionesDTO)
                .build();
    }
}