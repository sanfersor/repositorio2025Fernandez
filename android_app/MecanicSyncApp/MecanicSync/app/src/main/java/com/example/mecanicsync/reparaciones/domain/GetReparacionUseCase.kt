package com.example.mecanicsync.reparaciones.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import java.time.LocalDateTime
import javax.inject.Inject

class GetReparacionUseCase @Inject constructor(
    private val reparacionesRepository: ReparacionesRepository
) {
    suspend operator fun invoke(): Result<List<Reparacion>> {
        return try {
            val response = reparacionesRepository.getReparaciones()

            if (!response.isSuccessful)
                return Result.failure(Exception("Error: ${response.code()}"))

            val body = response.body()?.map { it.toModel() } ?: emptyList()
            Result.success(body)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
