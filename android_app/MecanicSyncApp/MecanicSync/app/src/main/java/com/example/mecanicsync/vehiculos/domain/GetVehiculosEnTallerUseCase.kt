package com.example.mecanicsync.vehiculos.domain

import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import javax.inject.Inject

class GetVehiculosEnTallerUseCase @Inject constructor(
    private val vehiculoRepository: VehiculoRepository
) {
    suspend operator fun invoke(): Result<List<Vehiculo>> {
        return try {
            Result.success(vehiculoRepository.getVehiculosEnTaller())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}