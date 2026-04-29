package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.data.network.ReparacionesVehiculoResponse
import javax.inject.Inject

class GetReparacionesPorMatriculaUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(matricula: String): Result<ReparacionesVehiculoResponse> {
        return reparacionesRepository.getReparacionesPorMatricula(matricula)
    }
}
