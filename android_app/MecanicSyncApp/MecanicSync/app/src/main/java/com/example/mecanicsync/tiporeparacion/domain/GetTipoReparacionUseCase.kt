package com.example.mecanicsync.tiporeparacion.domain

import com.example.mecanicsync.tiporeparacion.data.TipoReparacionRepository
import com.example.mecanicsync.tiporeparacion.ui.model.TipoReparacion
import javax.inject.Inject

class GetTipoReparacionUseCase @Inject constructor(private val tipoReparacionRepository: TipoReparacionRepository) {
    suspend operator fun invoke(): Result<List<TipoReparacion>> =
        try {
            val response = tipoReparacionRepository.getTiposReparacion()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toModel() })
            } else {
                Result.failure(Exception("Respuesta vacía o inválida"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
