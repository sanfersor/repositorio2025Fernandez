package com.example.mecanicsync.navigation


import kotlinx.serialization.Serializable


@Serializable
object VerLogin

@Serializable
data class VerDatosUsuario(val email: String)
@Serializable
data class VerHomeAdmin(val email: String)

@Serializable
object VerUsuarios

@Serializable
object AgregarUsuario

@Serializable
data class EditarUsuario(val idUsuario: Int)


@Serializable
data class VerHomeMecanico(val email: String)


@Serializable
data class VerReparaciones(val estado: String)

@Serializable
object VerTodasLasReparacionesScreen

@Serializable
object VerHistoricoReparacionesScreen


@Serializable
data class VerReparacionesDeVehiculo(val idVehiculo: Int)

@Serializable
object VerClientes

@Serializable
// La edición requiere el ID del cliente
data class EditarCliente(val idCliente: Int)


@Serializable
object AgregarCliente

@Serializable
object VerClientesEnTaller

@Serializable
object VerVehiculosEnTaller

@Serializable
object VerVehiculos

@Serializable
object AgregarVehiculo
@Serializable
// La eliminación puede requerir el ID del cliente para la acción
data class EliminarVehiculo(val idVehiculo: Int)

@Serializable
data class EditarVehiculo(val idVehiculo: Int)

@Serializable
object AgregarReparacion

@Serializable
data class EditarReparacion(val idReparacion: Int)
