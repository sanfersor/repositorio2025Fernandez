package com.example.mecanicsync.vehiculos.domain

import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.data.network.VehiculoRequest
import javax.inject.Inject



class UpdateVehiculoUseCase @Inject constructor(
    private val vehiculoRepository: VehiculoRepository
) {
    suspend operator fun invoke(
        idVehiculo: Int,
        request: VehiculoRequest // <-- Recibe el objeto completo
    ): Result<Unit> {
        return try {
            vehiculoRepository.updateVehiculo(
                idVehiculo,
                request
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}