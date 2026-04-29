package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.ClienteDTO;
import com.example.mecanicsync.dto.DashboardDTO;
import com.example.mecanicsync.dto.ReparacionDTO;
import com.example.mecanicsync.dto.VehiculoDTO;
import com.example.mecanicsync.entities.Cliente;
import com.example.mecanicsync.entities.Vehiculo;
import com.example.mecanicsync.repositories.ClienteRepository;
import com.example.mecanicsync.repositories.ReparacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/clientes")

public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired // <-- ¡Añade esto!
    private ReparacionRepository reparacionRepository;

    @GetMapping
    // GET /api/clientes → devuelve todos los clientes
    public List<ClienteDTO> getAll() {
        return clienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    //----------------------------------------------------------
    // GET CLIENTE BY ID (sin vehículos)
    //----------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Integer id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(toDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }
    //----------------------------------------------------------
    // GET VEHÍCULOS DE UN CLIENTE
    //----------------------------------------------------------
    @GetMapping("/{id}/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getVehiculosByCliente(@PathVariable Integer id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    List<VehiculoDTO> vehiculosDTO = cliente.getVehiculos().stream()
                            .map(this::vehiculoToDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(vehiculosDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // Nuevo metodo para obtener clientes con reparaciones que NO están 'Entregado'
    // GET /api/clientes/taller
    @GetMapping("/taller")
    public List<ClienteDTO> getClientesInTaller() {
        return clienteRepository.findClientesInTaller().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    @GetMapping("/{id}/vehiculos/reparaciones")
    public ResponseEntity<List<ReparacionDTO>> getReparacionesByCliente(@PathVariable Integer id) {

        // Buscar el cliente
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        // Obtener todos los vehículos del cliente
        List<Vehiculo> vehiculos = cliente.getVehiculos();

        // Obtener todas las reparaciones de esos vehículos
        List<ReparacionDTO> reparacionesDTO = vehiculos.stream()
                .flatMap(vehiculo -> vehiculo.getReparaciones().stream())
                .map(ReparacionDTO::new)
                .collect(Collectors.toList());

        if (reparacionesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reparacionesDTO);
    }
    // GET /api/clientes/dash
    @GetMapping("/dash")
    public DashboardDTO getDashboard() {

        // Usamos los nuevos métodos del repositorio
        Long clientesCount = clienteRepository.countClientesInTaller();
        Long vehiculosCount = clienteRepository.countVehiculosInTaller();
        long totalReparaciones = reparacionRepository.count();
        long pendientes = reparacionRepository.countByEstado("Pendiente");
        long enProceso = reparacionRepository.countByEstado("En proceso");
        long finalizadas = reparacionRepository.countByEstado("Finalizada");
        return DashboardDTO.builder()
                .clientesEnTaller(clientesCount)
                .vehiculosEnTaller(vehiculosCount)
                .totalReparaciones(totalReparaciones)
                .pendientes(pendientes)
                .enProceso(enProceso)
                .finalizadas(finalizadas)
                .build();
    }


    @PostMapping
    // @RequestBody convierte el JSON recibido en un objeto Cliente
    public Cliente create(@RequestBody Cliente cliente) {

        return clienteRepository.save(cliente);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable Integer id,
            @RequestBody ClienteDTO clienteDTO) {

        return clienteRepository.findById(id)
                .map(existingCliente -> {

                    // ACTUALIZAR SOLO CAMPOS PERMITIDOS
                    existingCliente.setCliente(clienteDTO.getCliente());
                    existingCliente.setTelefono(clienteDTO.getTelefono());
                    existingCliente.setEmail(clienteDTO.getEmail());
                    existingCliente.setDireccion(clienteDTO.getDireccion());

                    Cliente actualizado = clienteRepository.save(existingCliente);

                    // Convertir nuevamente a DTO
                    ClienteDTO actualizadoDTO = toDTO(actualizado);

                    return ResponseEntity.ok(actualizadoDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    // Metodo para mapear Cliente -> ClienteDTO
    private ClienteDTO toDTO(Cliente cliente) {

        // Mapear la lista de Vehiculos del Cliente a una lista de VehiculoDTO
        List<VehiculoDTO> vehiculosDTO = cliente.getVehiculos().stream()
                .map(this::vehiculoToDTO)
                .collect(Collectors.toList());

        return ClienteDTO.builder()
                .idCliente(cliente.getIdCliente())
                .cliente(cliente.getCliente())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .direccion(cliente.getDireccion())
                .fechaAlta(cliente.getFechaAlta())
                // Ahora pasamos la lista mapeada, que será vacía si el cliente no tiene vehículos.
                .vehiculos(vehiculosDTO)
                .build();
    }

    // Mapear Vehiculo -> VehiculoDTO
    private VehiculoDTO vehiculoToDTO(Vehiculo vehiculo) {
        return VehiculoDTO.builder()
                .idVehiculo(vehiculo.getId())
                .matricula(vehiculo.getMatricula())
                .anioMatriculacion(vehiculo.getAnioMatriculacion())
                .modelo(vehiculo.getModelo().getModelo())
                .marca(vehiculo.getModelo().getMarca().getMarca())
                .build();
    }
}
