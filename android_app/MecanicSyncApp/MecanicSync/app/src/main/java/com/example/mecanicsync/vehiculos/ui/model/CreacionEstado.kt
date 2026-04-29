package com.example.mecanicsync.vehiculos.ui.model

sealed class CreacionEstado {
    object Inicial : CreacionEstado()
    object Cargando : CreacionEstado()
    object Exito : CreacionEstado()
    data class Fallo(val mensaje: String?) : CreacionEstado()
}