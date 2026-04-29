package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import javax.inject.Inject

class DeleteReparacionUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(idReparacion: Int): Result<Unit>{
        return reparacionesRepository.deleteReparacion(idReparacion)
    }
}