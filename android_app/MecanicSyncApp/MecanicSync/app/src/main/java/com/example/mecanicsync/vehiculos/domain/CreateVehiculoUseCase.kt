package com.example.mecanicsync.vehiculos.domain


import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.data.network.VehiculoRequest
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import javax.inject.Inject

class CreateVehiculoUseCase @Inject constructor(
    private val vehiculoRepository: VehiculoRepository
) {
    // Acepta el nuevo VehiculoRequest para mayor claridad
    suspend operator fun invoke(request: VehiculoRequest): Result<Vehiculo>{
        return vehiculoRepository.createVehiculo(request)
    }
}