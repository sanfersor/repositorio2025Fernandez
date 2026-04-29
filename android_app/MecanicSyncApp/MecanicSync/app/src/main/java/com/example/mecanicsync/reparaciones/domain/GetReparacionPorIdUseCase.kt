package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import javax.inject.Inject

class GetReparacionPorIdUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(id: Int): Result<Reparacion> {
        return try {
          reparacionesRepository.getReparacionById(id)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


