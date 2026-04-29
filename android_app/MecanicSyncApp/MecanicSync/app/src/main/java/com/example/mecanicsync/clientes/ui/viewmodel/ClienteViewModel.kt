package com.example.mecanicsync.clientes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.clientes.domain.DeleteClienteUseCase
import com.example.mecanicsync.clientes.domain.GetClienteUseCase
import com.example.mecanicsync.clientes.ui.model.Cliente
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val getClienteUseCase: GetClienteUseCase,
    private val deleteClienteUseCase: DeleteClienteUseCase
): ViewModel()
{
    private val _cliente = MutableStateFlow<List<Cliente>>(emptyList())
    val cliente: StateFlow<List<Cliente>> = _cliente
    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message


    init {
        cargarClientes()
    }
    fun cargarClientes(){
        viewModelScope.launch {
            try {
                val result = getClienteUseCase()
                result.fold(
                    onSuccess = { listaCliente -> _cliente.value = listaCliente },
                    onFailure = { error -> _message.value = error.message }
                )

            } catch (e: Exception) {
                _message.value = e.message
            }
        }

    }
    fun eliminarCliente(id: Int) {
        viewModelScope.launch {
            deleteClienteUseCase(id)
            _deleteEvent.emit(Unit)
        }
    }
    fun clearMessage() {
        _message.value = null
    }
}