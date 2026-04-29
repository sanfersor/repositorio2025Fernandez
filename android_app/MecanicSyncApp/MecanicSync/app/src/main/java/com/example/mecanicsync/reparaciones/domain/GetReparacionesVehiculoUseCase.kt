package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import javax.inject.Inject

class GetReparacionesVehiculoUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(idVehiculo: Int): List<Reparacion> {
        return reparacionesRepository.getReparacionesVehiculo(idVehiculo)
    }
}