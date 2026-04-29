package com.example.mecanicsync.vehiculos.domain

import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import javax.inject.Inject

class DeleteVehiculoUseCase @Inject constructor(
    private val vehiculoRepository: VehiculoRepository
) {
    suspend operator fun invoke(idVehiculo: Int): Result<Unit>{
        return vehiculoRepository.deleteVehiculo(idVehiculo)
    }
}