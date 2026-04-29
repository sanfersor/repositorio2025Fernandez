package com.example.mecanicsync.usuario.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.usuario.data.network.UsuarioResponse
import com.example.mecanicsync.usuario.domain.DeleteUsuarioUseCase
import com.example.mecanicsync.usuario.domain.CreateUsuarioUseCase
import com.example.mecanicsync.usuario.domain.GetUsuarioByIdUseCase
import com.example.mecanicsync.usuario.domain.UpdateUsuarioUseCase
import com.example.mecanicsync.usuario.ui.model.Usuario
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularioUsuarioViewModel @Inject constructor(
    private val getUsuarioByIdUseCase: GetUsuarioByIdUseCase,
    private val updateUsuarioUseCase: UpdateUsuarioUseCase,
    private val deleteUsuarioUseCase: DeleteUsuarioUseCase,
    private val createUsuarioUseCase: CreateUsuarioUseCase,

) : ViewModel() {

    var usuario by mutableStateOf("")
    var email by mutableStateOf("")
    var rol by mutableStateOf("")

    val listaRoles = listOf(
        "Administrador",
        "Mecánico",

    )
    // Estado de creación/actualización
    private val _estadoGuardado = MutableStateFlow<CreacionEstado>(CreacionEstado.Inicial)
    val estadoGuardado: StateFlow<CreacionEstado> = _estadoGuardado


    // -----------------------
    // Cargar usuario por ID
    // -----------------------
    fun cargarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            _estadoGuardado.value = CreacionEstado.Cargando
            try {
                val usuarioObtenido = getUsuarioByIdUseCase(idUsuario)
                usuario = usuarioObtenido.usuario
                email = usuarioObtenido.email
                rol = usuarioObtenido.rol
                _estadoGuardado.value = CreacionEstado.Inicial
            } catch (e: Exception) {
                _estadoGuardado.value = CreacionEstado.Fallo(e.message ?: "Error al cargar usuario")
            }
        }
    }

    // -----------------------
    // Actualizar usuario
    // -----------------------
    fun actualizarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            _estadoGuardado.value = CreacionEstado.Cargando
            val result = updateUsuarioUseCase(
                idUsuario,
                UsuarioResponse(
                    idUsuario = idUsuario,
                    usuario = usuario,
                    email = email,
                    rol = rol
                )
            )
            result.onSuccess {
                _estadoGuardado.value = CreacionEstado.Exito
            }.onFailure {
                _estadoGuardado.value = CreacionEstado.Fallo(it.message ?: "Error al actualizar usuario")
            }
        }
    }

    // -----------------------
    // Crear usuario
    // -----------------------
    fun crearUsuario() {
        viewModelScope.launch {
            _estadoGuardado.value = CreacionEstado.Cargando
            val result = createUsuarioUseCase(
                Usuario(
                    idUsuario = 0,
                    usuario = usuario,
                    email = email,
                    rol = rol
                )
            )
            result.onSuccess {
                _estadoGuardado.value = CreacionEstado.Exito
            }.onFailure {
                _estadoGuardado.value = CreacionEstado.Fallo(it.message ?: "Error al crear usuario")
            }
        }
    }

    // -----------------------
    // Limpiar estado
    // -----------------------
    fun limpiarEstado() {
        _estadoGuardado.value = CreacionEstado.Inicial
    }

}