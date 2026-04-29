package com.example.mecanicsync.controllers;

import com.example.mecanicsync.dto.ReparacionDTO;
import com.example.mecanicsync.dto.ReparacionesVehiculoDTO;
import com.example.mecanicsync.entities.Cliente;
import com.example.mecanicsync.entities.Reparacion;
import com.example.mecanicsync.entities.TipoReparacion;
import com.example.mecanicsync.entities.Vehiculo;
import com.example.mecanicsync.repositories.ReparacionRepository;
import com.example.mecanicsync.repositories.TipoReparacionRepository;
import com.example.mecanicsync.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/reparaciones")
public class ReparacionController {

    @Autowired
    private ReparacionRepository reparacionRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TipoReparacionRepository tipoReparacionRepository;

    //----------------------------------------------------------
    // GET ALL
    //----------------------------------------------------------
    @GetMapping
    public List<ReparacionDTO> getReparaciones() {
        return reparacionRepository.findAll()
                .stream()
                .map(ReparacionDTO::new)
                .toList();
    }

    //----------------------------------------------------------
    // GET BY ID
    //----------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ReparacionDTO> getById(@PathVariable Integer id) {
        return reparacionRepository.findById(id)
                .map(r -> ResponseEntity.ok(new ReparacionDTO(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    //----------------------------------------------------------
    // GET BY VEHICULO
    //----------------------------------------------------------
    @GetMapping("/vehiculo/{idVehiculo}")
    public List<ReparacionDTO> getByVehiculo(@PathVariable Integer idVehiculo) {
        return reparacionRepository.findByVehiculo_Id(idVehiculo)
                .stream()
                .map(ReparacionDTO::new)
                .toList();
    }

    //----------------------------------------------------------
    // GET BY ESTADO
    //----------------------------------------------------------
    @GetMapping("/estado/{estado}")
    public List<ReparacionDTO> getByEstado(@PathVariable String estado) {
        return reparacionRepository.findByEstado(estado)
                .stream()
                .map(ReparacionDTO::new)
                .toList();
    }
    //----------------------------------------------------------
// GET HISTÓRICO POR MATRÍCULA
//----------------------------------------------------------
    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<ReparacionesVehiculoDTO> getHistorialByMatricula(
            @PathVariable String matricula) {

        Vehiculo vehiculo = vehiculoRepository.findByMatricula(matricula);

        if (vehiculo == null) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = vehiculo.getCliente();

        List<ReparacionDTO> reparaciones = vehiculo.getReparaciones()
                .stream()
                .map(ReparacionDTO::new)
                .toList();

        ReparacionesVehiculoDTO dto = new ReparacionesVehiculoDTO(
                cliente.getIdCliente(),
                cliente.getCliente(),
                vehiculo.getId(),
                vehiculo.getMatricula(),
                vehiculo.getModelo().getModelo(),
                vehiculo.getModelo().getMarca().getMarca(),
                reparaciones
        );

        return ResponseEntity.ok(dto);
    }



    //----------------------------------------------------------
    // CREATE (POST)
    //----------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReparacionDTO dto) {

        // Validar ID vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getIdVehiculo())
                .orElse(null);

        if (vehiculo == null) {
            return ResponseEntity.badRequest().body("El vehículo no existe");
        }

        // Validar ID tipo reparación
        TipoReparacion tipo = tipoReparacionRepository.findById(dto.getIdTipoReparacion())
                .orElse(null);

        if (tipo == null) {
            return ResponseEntity.badRequest().body("El tipo de reparación no existe");
        }

        // Construir entidad desde DTO
        Reparacion nueva = new Reparacion();
        nueva.setVehiculo(vehiculo);
        nueva.setTipo(tipo);
        nueva.setDescripcionAdicional(dto.getDescripcionAdicional());
        nueva.setEstado(dto.getEstado());
        nueva.setFechaInicio(dto.getFechaInicio());
        nueva.setFechaFin(dto.getFechaFin());
        nueva.setHorasReales(dto.getHorasReales());
        nueva.setImporteTotal(dto.getImporteTotal());

        Reparacion guardada = reparacionRepository.save(nueva);

        return ResponseEntity.ok(new ReparacionDTO(guardada));
    }

    //----------------------------------------------------------
    // UPDATE (PUT)
    //----------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody ReparacionDTO dto) {

        return reparacionRepository.findById(id)
                .map(existing -> {

                    // Validar vehículo
                    Vehiculo vehiculo = vehiculoRepository.findById(dto.getIdVehiculo())
                            .orElse(null);
                    if (vehiculo == null) {
                        return ResponseEntity.badRequest().body("El vehículo no existe");
                    }

                    // Validar tipo
                    TipoReparacion tipo = tipoReparacionRepository.findById(dto.getIdTipoReparacion())
                            .orElse(null);
                    if (tipo == null) {
                        return ResponseEntity.badRequest().body("El tipo de reparación no existe");
                    }

                    // Actualizar campos
                    existing.setVehiculo(vehiculo);
                    existing.setTipo(tipo);
                    existing.setDescripcionAdicional(dto.getDescripcionAdicional());
                    existing.setEstado(dto.getEstado());
                    existing.setFechaInicio(dto.getFechaInicio());
                    existing.setFechaFin(dto.getFechaFin());
                    existing.setHorasReales(dto.getHorasReales());
                    existing.setImporteTotal(dto.getImporteTotal());

                    Reparacion updated = reparacionRepository.save(existing);

                    return ResponseEntity.ok(new ReparacionDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //----------------------------------------------------------
    // DELETE
    //----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        if (!reparacionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reparacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
