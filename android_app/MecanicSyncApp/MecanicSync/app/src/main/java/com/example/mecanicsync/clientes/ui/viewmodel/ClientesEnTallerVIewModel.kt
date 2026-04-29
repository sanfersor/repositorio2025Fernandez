package com.example.mecanicsync.clientes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.clientes.domain.GetClientesEnTallerUseCase
import com.example.mecanicsync.clientes.ui.model.Cliente
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesEnTallerVIewModel @Inject constructor(
    private val getClientesEnTallerUseCase: GetClientesEnTallerUseCase
): ViewModel()
{
    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes = _clientes.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        cargarClientesEnTaller()
        }
    fun cargarClientesEnTaller() {
        viewModelScope.launch {
            val result = getClientesEnTallerUseCase()
            result.fold(
                onSuccess = { _clientes.value = it },
                onFailure = { _message.value = it.message }
            )
        }
    }

    fun clearMessage() { _message.value = null }
}