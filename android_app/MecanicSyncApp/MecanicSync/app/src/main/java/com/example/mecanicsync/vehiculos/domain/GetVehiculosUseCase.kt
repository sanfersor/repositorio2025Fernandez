package com.example.mecanicsync.vehiculos.domain

import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import javax.inject.Inject




class GetVehiculosUseCase @Inject constructor(private val vehiculoRepository: VehiculoRepository) {
    suspend operator fun invoke(): Result<List<Vehiculo>>{
        return try{
            val response = vehiculoRepository.getVehiculos()
            if(response.isSuccessful)
                Result.success(response.body()!!.map{it.toModel()})
            else
                Result.failure((Exception("Error en la petición:${response.code()}")))
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}