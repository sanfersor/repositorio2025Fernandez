package com.example.mecanicsync.clientes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.clientes.domain.CreateClienteUseCase
import com.example.mecanicsync.clientes.domain.GetClienteByIdUseCase
import com.example.mecanicsync.clientes.domain.UpdateClienteUseCase
import com.example.mecanicsync.clientes.ui.model.Cliente
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FormularioClienteViewModel @Inject constructor(
    private val createClienteUseCase: CreateClienteUseCase,
    private val updateClienteUseCase: UpdateClienteUseCase,
    private val getClienteByIdUseCase: GetClienteByIdUseCase

) : ViewModel() {

    // Campos
    val nombre = MutableStateFlow("")
    val telefono = MutableStateFlow("")
    val email = MutableStateFlow("")

    // Errores
    val nombreError = MutableStateFlow<String?>(null)
    val telefonoError = MutableStateFlow<String?>(null)

    // Resultado de guardar
    private val _saveResult = MutableStateFlow<Result<Unit>?>(null)
    val saveResult: StateFlow<Result<Unit>?> = _saveResult

    // Estado para indicar si ya cargó el cliente en modo edición
    private val _clienteLoaded = MutableStateFlow(false)
    val clienteLoaded: StateFlow<Boolean> = _clienteLoaded


    // ----------------------------------------------------------------------------------------
    //  CARGAR CLIENTES EN MODO EDICION
    // ----------------------------------------------------------------------------------------
    fun cargarCliente(idCliente: Int) {
        viewModelScope.launch {
            try {
                val cliente = getClienteByIdUseCase(idCliente)

                // Rellenamos los campos del formulario
                nombre.value = cliente.cliente ?: ""
                telefono.value = cliente.telefono ?: ""
                email.value = cliente.email ?: ""

                _clienteLoaded.value = true

            } catch (e: Exception) {
                println("Error cargando cliente: ${e.message}")
            }
        }
    }

    // ----------------------------------------------------------------------------------------
    //  VALIDAR FORMULARIO
    // ----------------------------------------------------------------------------------------
    private fun validarFormulario(): Boolean {
        var valido = true

        nombreError.value = if (nombre.value.isBlank()) {
            valido = false
            "El nombre es obligatorio"
        } else null

        telefonoError.value = if (telefono.value.length < 9) {
            valido = false
            "Teléfono inválido"
        } else null

        return valido
    }


    // ----------------------------------------------------------------------------------------
    //  CREAR CLIENTE NUEVO
    // ----------------------------------------------------------------------------------------
    fun crearCliente() {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val cliente = Cliente(
                cliente = nombre.value,
                telefono = telefono.value,
                email = email.value,
            )

            val result = createClienteUseCase(cliente)
            _saveResult.value = result.map { }
        }
    }


    // ----------------------------------------------------------------------------------------
    //  ACTUALIZAR CLIENTE EXISTENTE
    // ----------------------------------------------------------------------------------------
    fun actualizarCliente(idCliente: Int) {
        if (!validarFormulario()) return

        viewModelScope.launch {

            // ClienteResponse es el que tu API espera en el update
            val clienteResponse = ClienteResponse(
                id_cliente = idCliente,
                cliente = nombre.value,
                telefono = telefono.value,
                email = email.value,
                direccion = null,
                fechaAlta = null
            )

            val result = updateClienteUseCase(
                idCliente = idCliente,
                clienteResponse = clienteResponse
            )

            _saveResult.value = result
        }
    }


    // ----------------------------------------------------------------------------------------
    // LIMPIAR ESTADO TRAS GUARDAR
    // ----------------------------------------------------------------------------------------
    fun clearSaveResult() {
        _saveResult.value = null
    }

}
