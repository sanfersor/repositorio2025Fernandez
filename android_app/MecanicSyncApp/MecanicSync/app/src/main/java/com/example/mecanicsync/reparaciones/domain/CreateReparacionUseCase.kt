package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.data.network.ReparacionesResponse
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import javax.inject.Inject

class CreateReparacionUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(
        reparacion: ReparacionesResponse
    ): Result<Reparacion> {
        return reparacionesRepository.createReparacion(reparacion)
    }
}

