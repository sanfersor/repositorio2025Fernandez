package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.data.network.ReparacionesResponse
import javax.inject.Inject

class UpdateReparacionesUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(
        idReparacion: Int,
        reparacionesResponse: ReparacionesResponse
    ) : Result<Unit>{
        return try {
            reparacionesRepository.updateReparacion(idReparacion, reparacionesResponse)
        }catch (e: Exception){
            Result.failure(e)
        }

    }
}