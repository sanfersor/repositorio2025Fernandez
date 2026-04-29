package com.example.mecanicsync.vehiculos.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.vehiculos.domain.DeleteVehiculoUseCase
import com.example.mecanicsync.vehiculos.domain.GetVehiculosUseCase
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiculoViewModel @Inject constructor(
    private val getVehiculosUseCase: GetVehiculosUseCase,
    private val deleteVehiculoUseCase: DeleteVehiculoUseCase,



): ViewModel()
{
    private val _vehiculo = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculo: StateFlow<List<Vehiculo>> = _vehiculo

    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message



    init{
        cargarVehiculos()
    }

    fun cargarVehiculos() = viewModelScope.launch {
        try {
            val result = getVehiculosUseCase()
            result.fold(
                onSuccess = { _vehiculo.value = it },
                onFailure = { _message.value = it.message }
            )
        } catch (e: Exception) {
            _message.value = e.message
        }
    }


    fun eliminarVehiculo(id: Int) {
        viewModelScope.launch {
            deleteVehiculoUseCase(id)
            _deleteEvent.emit(Unit)   // Evento de un solo uso
        }
    }
    fun clearMessage() {
        _message.value = null
    }
}