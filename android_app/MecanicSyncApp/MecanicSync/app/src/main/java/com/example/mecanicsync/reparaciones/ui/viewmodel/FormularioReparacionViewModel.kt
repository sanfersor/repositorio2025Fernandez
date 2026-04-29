package com.example.mecanicsync.reparaciones.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.reparaciones.data.network.ReparacionesResponse
import com.example.mecanicsync.reparaciones.domain.CreateReparacionUseCase
import com.example.mecanicsync.reparaciones.domain.GetReparacionPorIdUseCase
import com.example.mecanicsync.reparaciones.domain.UpdateReparacionesUseCase
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import com.example.mecanicsync.tiporeparacion.domain.GetTipoReparacionUseCase
import com.example.mecanicsync.tiporeparacion.ui.model.TipoReparacion
import com.example.mecanicsync.vehiculos.domain.GetVehiculosUseCase
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class FormularioReparacionViewModel @Inject constructor(
    private val getVehiculosUseCase: GetVehiculosUseCase,
    private val getTipoReparacionUseCase: GetTipoReparacionUseCase,
    private val createReparacionUseCase: CreateReparacionUseCase,
    private val updateReparacionesUseCase: UpdateReparacionesUseCase,
    private val getReparacionPorIdUseCase: GetReparacionPorIdUseCase
) : ViewModel() {

    // --- Vehículos ---
    private val _vehiculos = MutableStateFlow<List<Vehiculo>>(emptyList())
    val vehiculos = _vehiculos.asStateFlow()

    // --- Tipos de reparación ---
    private val _tiposReparacion = MutableStateFlow<List<TipoReparacion>>(emptyList())
    val tiposReparacion = _tiposReparacion.asStateFlow()

    // --- Formulario ---
    var vehiculoSeleccionado by mutableStateOf<Vehiculo?>(null)
    var tipoSeleccionado by mutableStateOf<TipoReparacion?>(null)
    var descripcionAdicional by mutableStateOf("")
    var fechaInicio by mutableStateOf(LocalDate.now())
    var fechaFin by mutableStateOf<LocalDate?>(null)
    var estado by mutableStateOf("Pendiente")
    var horasReales by mutableStateOf<Double?>(null)
    var importeTotal by mutableStateOf<Double?>(null)


    val listaEstados = listOf(
        "Pendiente",
        "En proceso",
        "Finalizada",
        "Entregado"
    )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // --- Estado de creación/actualización ---
    private val _estadoCreacion = MutableStateFlow<CreacionEstado>(CreacionEstado.Inicial)
    val estadoCreacion = _estadoCreacion.asStateFlow()


    init {
        cargarVehiculos()
        cargarTiposReparacion()
    }

    // -----------------------------
    // Cargar datos
    // -----------------------------
    fun cargarVehiculos() = viewModelScope.launch {
        try {
            val result = getVehiculosUseCase()
            result.fold(
                onSuccess = { _vehiculos.value = it },
                onFailure = { _message.value = it.message }
            )
        } catch (e: Exception) {
            _message.value = e.message
        }
    }

    fun cargarTiposReparacion() = viewModelScope.launch {
        try {
            val result = getTipoReparacionUseCase()
            result.fold(
                onSuccess = { _tiposReparacion.value = it },
                onFailure = { _message.value = it.message }
            )
        } catch (e: Exception) {
            _message.value = e.message
        }
    }

    fun seleccionarVehiculo(vehiculo: Vehiculo) {
        vehiculoSeleccionado = vehiculo
    }

    fun seleccionarTipo(tipo: TipoReparacion) {
        tipoSeleccionado = tipo
    }

    // =========================================================
    //  CREAR REPARACIÓN
    // =========================================================
    fun crearReparacion() = viewModelScope.launch {
        val vehiculo = vehiculoSeleccionado
        val tipo = tipoSeleccionado

        if (vehiculo == null || tipo == null) {
            _estadoCreacion.value =
                CreacionEstado.Fallo("Vehículo y tipo obligatorios")
            return@launch
        }

        val request = ReparacionesResponse(
            id_reparacion = 0, // backend genera
            id_vehiculo = vehiculo.id_vehiculo,
            matricula = vehiculo.matricula,
            marca = vehiculo.marca,
            modelo = vehiculo.modelo,
            id_tipo = tipo.idTipo,
            tipo_reparacion = tipo.reparacion,
            descripcion_tipo = tipo.descripcion,
            descripcion_adicional = descripcionAdicional,
            estado = estado,
            fecha_inicio = fechaInicio.toString(),
            fecha_fin = null,
            horas_reales = null,
            importe_total = null
        )

        val result = createReparacionUseCase(request)

        _estadoCreacion.value =
            if (result.isSuccess) CreacionEstado.Exito
            else CreacionEstado.Fallo(result.exceptionOrNull()?.message ?: "Error")
    }

    // =========================================================
    //  ACTUALIZAR REPARACIÓN
    // =========================================================
    fun actualizarReparacion(idReparacion: Int) = viewModelScope.launch {
        val vehiculo = vehiculoSeleccionado
        val tipo = tipoSeleccionado

        if (vehiculo == null || tipo == null) {
            _estadoCreacion.value =
                CreacionEstado.Fallo("Vehículo y tipo obligatorios")
            return@launch
        }

        val request = ReparacionesResponse(
            id_reparacion = idReparacion,
            id_vehiculo = vehiculo.id_vehiculo,
            matricula = vehiculo.matricula,
            marca = vehiculo.marca,
            modelo = vehiculo.modelo,
            id_tipo = tipo.idTipo,
            tipo_reparacion = tipo.reparacion,
            descripcion_tipo = tipo.descripcion,
            descripcion_adicional = descripcionAdicional,
            estado = estado,
            fecha_inicio = fechaInicio.toString(),
            fecha_fin = fechaFin?.toString(),
            horas_reales = horasReales,
            importe_total = importeTotal
        )

        val result = updateReparacionesUseCase(idReparacion, request)

        _estadoCreacion.value =
            if (result.isSuccess) CreacionEstado.Exito
            else CreacionEstado.Fallo(result.exceptionOrNull()?.message ?: "Error")
    }

    // =========================================================
    // Cargar reparación existente (edición)
    // =========================================================
    fun cargarReparacionExistente(reparacion: Reparacion) {
        vehiculoSeleccionado = Vehiculo(
            id_vehiculo = reparacion.id_vehiculo,
            matricula = reparacion.matricula,
            marca = reparacion.marca,
            modelo = reparacion.modelo
        )

        tipoSeleccionado = TipoReparacion(
            idTipo = reparacion.idTipo ?: 0,
            reparacion = reparacion.tipo_reparacion ?: "",
            descripcion = reparacion.descripcion_tipo
        )

        descripcionAdicional = reparacion.descripcion_adicional ?: ""
        estado = reparacion.estado
        fechaInicio = LocalDate.parse(reparacion.fecha_inicio)
        fechaFin = reparacion.fecha_fin?.let { LocalDate.parse(it) }
        horasReales = reparacion.horas_reales
        importeTotal = reparacion.importe_total

    }

    suspend fun cargarReparacionPorId(id: Int): Reparacion? {
        return getReparacionPorIdUseCase(id).getOrNull()
    }

    fun limpiarEstado() {
        _estadoCreacion.value = CreacionEstado.Inicial
    }
}
