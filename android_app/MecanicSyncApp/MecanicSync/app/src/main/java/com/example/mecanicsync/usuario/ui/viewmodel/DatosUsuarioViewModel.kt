package com.example.mecanicsync.usuario.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.mecanicsync.usuario.domain.GetDatosUsuarioUseCase
import com.example.mecanicsync.usuario.ui.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatosUsuarioViewModel @Inject constructor(
    private val getDatosUsuarioUseCase: GetDatosUsuarioUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userEmail: String = checkNotNull(savedStateHandle["email"])
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarUsuario(userEmail)
    }

    fun cargarUsuario(email: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val result = getDatosUsuarioUseCase(email)
            result.fold(
                onSuccess = { _usuario.value = it },
                onFailure = { _error.value = it.message ?: "Error desconocido" }
            )
            _loading.value = false
        }
    }
}