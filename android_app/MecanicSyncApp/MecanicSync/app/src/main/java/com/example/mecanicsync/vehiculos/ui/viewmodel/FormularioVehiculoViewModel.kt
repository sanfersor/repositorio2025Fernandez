package com.example.mecanicsync.vehiculos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.marca.data.network.MarcaResponse
import com.example.mecanicsync.modelo.data.network.ModeloResponse
import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.data.network.VehiculoRequest
import com.example.mecanicsync.vehiculos.domain.CreateVehiculoUseCase
import com.example.mecanicsync.vehiculos.domain.UpdateVehiculoUseCase
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormularioVehiculoViewModel @Inject constructor(
    private val vehiculoRepository: VehiculoRepository,
    private val createVehiculoUseCase: CreateVehiculoUseCase,
    private val updateVehiculoUseCase: UpdateVehiculoUseCase
) : ViewModel() {

    // Datos para los dropdowns
    private val _clientes = MutableStateFlow<List<ClienteResponse>>(emptyList())
    val clientes = _clientes.asStateFlow()

    private val _marcas = MutableStateFlow<List<MarcaResponse>>(emptyList())
    val marcas = _marcas.asStateFlow()

    private val _modelos = MutableStateFlow<List<ModeloResponse>>(emptyList())
    val modelos = _modelos.asStateFlow()

    // Estado de creación/actualización
    private val _estadoCreacion = MutableStateFlow<CreacionEstado>(CreacionEstado.Inicial)
    val estadoCreacion = _estadoCreacion.asStateFlow()

    private val _vehiculoInicial = MutableStateFlow<Vehiculo?>(null)
    val vehiculoInicial = _vehiculoInicial.asStateFlow()

    init {
        cargarClientes()
        cargarMarcas()
    }

    fun cargarClientes() = viewModelScope.launch {
        vehiculoRepository.getClientes().onSuccess {
            _clientes.value = it
        }
    }

    fun cargarMarcas() = viewModelScope.launch {
        vehiculoRepository.getMarcas().onSuccess {
            _marcas.value = it
        }
    }

    fun cargarModelosPorMarca(idMarca: Int) = viewModelScope.launch {
        vehiculoRepository.getModelosPorMarca(idMarca).onSuccess {
            _modelos.value = it
        }
    }

    fun limpiarModelos() {
        _modelos.value = emptyList()
    }

    suspend fun cargarVehiculoPorId(idVehiculo: Int): Vehiculo {
        val result = vehiculoRepository.getVehiculoPorId(idVehiculo)
        return if (result.isSuccess) {
            result.getOrThrow() // devuelve el Vehiculo
        } else {
            throw result.exceptionOrNull() ?: Exception("Error desconocido")
        }
    }



    fun guardarVehiculo(
        idCliente: Int,
        idModelo: Int,
        matricula: String,
        anioMatriculacion: Int,
        vehiculoExistenteId: Int? = null
    ) = viewModelScope.launch {
        _estadoCreacion.value = CreacionEstado.Cargando

        // Creamos el objeto de petición una sola vez
        val request = VehiculoRequest(idCliente, idModelo, matricula, anioMatriculacion)

        val result = try {
            if (vehiculoExistenteId == null) {
                createVehiculoUseCase(request)
            } else {
                updateVehiculoUseCase( // Usamos la versión optimizada
                    idVehiculo = vehiculoExistenteId,
                    request = request // Pasamos el objeto
                )
            }
        } catch (e: Exception) {
            Result.failure<Unit>(e)
        }

        _estadoCreacion.value = if (result.isSuccess) CreacionEstado.Exito
        else CreacionEstado.Fallo(result.exceptionOrNull()?.message)
    }
    fun iniciarEdicion(idVehiculo: Int) = viewModelScope.launch {
        // 1. Cargar el Vehículo
        val result = vehiculoRepository.getVehiculoPorId(idVehiculo)
        result.onSuccess { vehiculo ->
            _vehiculoInicial.value = vehiculo

            // 2. Pre-cargar los Modelos de la Marca asociada
            // Asumiendo que puedes obtener el id_marca a partir del vehiculo o su modelo
            // Necesitarías una función en el repo/API que te dé el id_marca dado un id_modelo,
            // o que tu objeto Vehiculo devuelto ya lo traiga.
            // Si no tienes el id_marca, esta parte se complica.
            // Si vehiculo.marca (nombre) y marcas (lista) están listos, puedes buscar el id:
            val marcaId = marcas.value.find { it.nombreMarca == vehiculo.marca }?.idMarca
            if (marcaId != null) {
                cargarModelosPorMarca(marcaId)
            }
        }.onFailure {
            // Manejar error
        }
    }

    fun limpiarEstadoCreacion() {
        _estadoCreacion.value = CreacionEstado.Inicial
    }
}
