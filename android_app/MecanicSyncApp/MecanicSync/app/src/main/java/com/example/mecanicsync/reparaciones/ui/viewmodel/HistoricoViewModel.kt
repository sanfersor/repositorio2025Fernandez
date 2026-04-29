package com.example.mecanicsync.reparaciones.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.reparaciones.data.network.ReparacionesVehiculoResponse
import com.example.mecanicsync.reparaciones.domain.GetReparacionesPorMatriculaUseCase
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricoReparacionesViewModel @Inject constructor(
    private val getReparacionesPorMatriculaUseCase: GetReparacionesPorMatriculaUseCase
) : ViewModel() {

    var nombreCliente by mutableStateOf<String?>(null)
        private set

    var vehiculoInfo by mutableStateOf<ReparacionesVehiculoResponse?>(null)
        private set

    var reparaciones by mutableStateOf<List<Reparacion>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun buscarPorMatricula(matricula: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            nombreCliente = null
            vehiculoInfo = null
            reparaciones = emptyList()

            getReparacionesPorMatriculaUseCase(matricula.trim())
                .fold(
                    onSuccess = { body ->
                        nombreCliente = body.nombreCliente
                        vehiculoInfo = body
                        reparaciones = body.reparaciones.map { it.toModel() }
                    },
                    onFailure = { e ->
                        error = e.message ?: "Error desconocido"
                    }
                )

            isLoading = false
        }
    }
}