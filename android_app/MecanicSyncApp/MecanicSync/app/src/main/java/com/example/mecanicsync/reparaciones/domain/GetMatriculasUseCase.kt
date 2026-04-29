package com.example.mecanicsync.reparaciones.domain

import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import javax.inject.Inject

class GetMatriculasUseCase @Inject constructor(
    private val vehiculoRepository: VehiculoRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        val res = vehiculoRepository.getVehiculos()
        return if (res.isSuccessful) {
            Result.success(res.body()!!.map { it.matricula })
        } else Result.failure(Exception("Error cargando matrículas"))
    }
}
