package com.example.mecanicsync.usuario.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.usuario.domain.DeleteUsuarioUseCase
import com.example.mecanicsync.usuario.domain.GetUsuarioUseCase
import com.example.mecanicsync.usuario.ui.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuariosViewModel @Inject constructor(
    private val getUsuarioUseCase: GetUsuarioUseCase,
    private val deleteUsuarioUseCase: DeleteUsuarioUseCase
): ViewModel()
{
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios
    private val _deleteEvent = MutableSharedFlow<Unit>()
    val deleteEvent = _deleteEvent.asSharedFlow()

    init{
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            getUsuarioUseCase().fold(
                onSuccess = { _usuarios.value = it },
                onFailure = { println("Error cargando usuarios: ${it.message}") }
            )
        }
    }
    fun eliminarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            deleteUsuarioUseCase(idUsuario).onSuccess {
                _deleteEvent.emit(Unit)
            }
        }
    }

}