package com.example.mecanicsync.reparaciones.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.reparaciones.domain.DeleteReparacionUseCase
import com.example.mecanicsync.reparaciones.domain.GetReparacionUseCase
import com.example.mecanicsync.reparaciones.domain.GetReparacionesVehiculoUseCase
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReparacionesViewModel @Inject constructor(
    private val getReparacionUseCase: GetReparacionUseCase,
    private val getReparacionesVehiculoUseCase: GetReparacionesVehiculoUseCase,
    private val deleteReparacionUseCase: DeleteReparacionUseCase,

) : ViewModel()
{

    private val _reparaciones = MutableStateFlow<List<Reparacion>>(emptyList())
    val reparaciones: StateFlow<List<Reparacion>> = _reparaciones

    var isLoading by mutableStateOf(false)
    private val _reparacionesVehiculo = MutableStateFlow<List<Reparacion>>(emptyList())
    val reparacionesVehiculo: StateFlow<List<Reparacion>> = _reparacionesVehiculo

    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message


    init {
        cargarReparaciones()
    }

    fun cargarReparaciones() {
        viewModelScope.launch {
            try {
                val result = getReparacionUseCase()
                result.fold(
                    onSuccess = { listaReparaciones ->

                        // ORDEN PERSONALIZADO POR ESTADO
                        val ordenadas = listaReparaciones.sortedBy { reparacion ->
                            when (reparacion.estado) {
                                "Pendiente" -> 1
                                "En proceso" -> 2
                                "Finalizada" -> 3
                                "Entregado" -> 4
                                else -> 5
                            }
                        }

                        _reparaciones.value = ordenadas
                    },
                    onFailure = { error -> _message.value = error.message }
                )

            } catch (e: Exception) {
                _message.value = e.message
            }
        }

    }
    fun clearMessage() {
        _message.value = null
    }
    fun getReparacionesVehiculo(idVehiculo: Int) {
        viewModelScope.launch {
            try {
                val lista = getReparacionesVehiculoUseCase(idVehiculo)
                _reparacionesVehiculo.value = lista
            } catch (e: Exception) {
                println("ERROR: ${e.message}")
            }
        }
    }
    fun eliminarReparacion(id: Int){
        viewModelScope.launch {
            deleteReparacionUseCase(id)
            _deleteEvent.emit(Unit)
        }
    }
}
