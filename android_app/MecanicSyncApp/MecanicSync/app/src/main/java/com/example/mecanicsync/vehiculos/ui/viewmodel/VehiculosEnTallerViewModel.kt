package com.example.mecanicsync.vehiculos.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.vehiculos.domain.GetVehiculosEnTallerUseCase
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiculosEnTallerViewModel @Inject constructor(
    private val getVehiculosEnTallerUseCase: GetVehiculosEnTallerUseCase
): ViewModel()
{
    private val _vehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculos: StateFlow<List<Vehiculo>> = _vehiculos
    var isLoading by mutableStateOf(false)

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init{
        cargarVehiculosEnTaller()
    }

    private fun cargarVehiculosEnTaller() {
        viewModelScope.launch {
            try {
                val result = getVehiculosEnTallerUseCase()
                result.fold(
                    onSuccess = { listaVehiculos -> _vehiculos.value = listaVehiculos },
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
}