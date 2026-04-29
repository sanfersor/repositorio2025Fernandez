package com.example.mecanicsync.reparaciones.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.reparaciones.ui.viewmodel.FormularioReparacionViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioReparacion(

    navController: NavController,
    idReparacion: Int? = null,
    formularioReparacionViewModel: FormularioReparacionViewModel = hiltViewModel()
) {
    val vehiculos by formularioReparacionViewModel.vehiculos.collectAsState()
    val tipos by formularioReparacionViewModel.tiposReparacion.collectAsState()
    val estadoCreacion by formularioReparacionViewModel.estadoCreacion.collectAsState()

    var descripcion by remember { mutableStateOf("") }

    var vehiculoDropdownExpanded by remember { mutableStateOf(false) }
    var tipoDropdownExpanded by remember { mutableStateOf(false) }
    var estadoDropdownExpanded by remember { mutableStateOf(false) }

    val vehiculoSeleccionado = formularioReparacionViewModel.vehiculoSeleccionado
    val tipoSeleccionado = formularioReparacionViewModel.tipoSeleccionado

    // --- Carga de datos existentes si es edición ---
    LaunchedEffect(idReparacion) {
        if (idReparacion != null) {
            formularioReparacionViewModel
                .cargarReparacionPorId(idReparacion)
                ?.let {
                    formularioReparacionViewModel.cargarReparacionExistente(it)
                    descripcion = it.descripcion_tipo ?: ""


                }
        }
    }

    LaunchedEffect(estadoCreacion) {
        when (estadoCreacion) {
            CreacionEstado.Exito -> navController.popBackStack()
            is CreacionEstado.Fallo -> formularioReparacionViewModel.limpiarEstado()
            else -> {}
        }
    }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (idReparacion == null) "Nueva Reparación"
                            else "Editar Reparación"
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Cancelar",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // -----------------------
                // VEHÍCULO (Spinner)
                // -----------------------
                DropdownField(
                    label = "Seleccionar Vehículo",
                    value = vehiculoSeleccionado?.matricula ?: "",
                    expanded = vehiculoDropdownExpanded,
                    enabled = idReparacion == null,
                    onExpandedChange = { vehiculoDropdownExpanded = it },
                    items = vehiculos.map { it.matricula },
                    onItemSelected = { index ->
                        formularioReparacionViewModel.seleccionarVehiculo(vehiculos[index])
                        vehiculoDropdownExpanded = false
                    }
                )

                Spacer(Modifier.height(16.dp))

                // Mostrar marca & modelo
                vehiculoSeleccionado?.let {
                    OutlinedTextField(
                        value = it.marca,
                        onValueChange = {},
                        label = { Text("Marca") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = it.modelo,
                        onValueChange = {},
                        label = { Text("Modelo") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))
                }

                // -----------------------
                // TIPO DE REPARACIÓN
                // -----------------------
                DropdownField(
                    label = "Tipo de Reparación",
                    value = tipoSeleccionado?.reparacion ?: "",
                    expanded = tipoDropdownExpanded,
                    enabled = idReparacion == null,
                    onExpandedChange = { if (idReparacion == null) tipoDropdownExpanded = it },
                    items = tipos.map { it.reparacion },
                    onItemSelected = { index ->
                        formularioReparacionViewModel.seleccionarTipo(tipos[index])
                        tipoDropdownExpanded = false
                    }
                )

                Spacer(Modifier.height(16.dp))

                // -----------------------
                // ESTADO (solo edición)
                // -----------------------
                if (idReparacion != null) {
                    DropdownField(
                        label = "Estado",
                        value = formularioReparacionViewModel.estado,
                        expanded = estadoDropdownExpanded,
                        enabled = true,
                        onExpandedChange = { estadoDropdownExpanded = it },
                        items = formularioReparacionViewModel.listaEstados,
                        onItemSelected = { index ->
                            formularioReparacionViewModel.estado =
                                formularioReparacionViewModel.listaEstados[index]
                            estadoDropdownExpanded = false
                        }
                    )

                    Spacer(Modifier.height(16.dp))
                }

                // -----------------------
                // DESCRIPCIÓN
                // -----------------------
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                        formularioReparacionViewModel.descripcionAdicional = it
                    },
                    label = { Text("Descripción adicional") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // -----------------------
                // FECHA INICIO
                // -----------------------
                OutlinedTextField(
                    value = formularioReparacionViewModel.fechaInicio.toString(),
                    onValueChange = {},
                    label = { Text("Fecha de inicio") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // -----------------------
                // FECHA FIN (solo edición)
                // -----------------------

                if (idReparacion != null) {
                    FechaFinField(
                        fechaFin = formularioReparacionViewModel.fechaFin,
                        onFechaSelected = { formularioReparacionViewModel.fechaFin = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (idReparacion != null) {

                    // HORAS REALES
                    OutlinedTextField(
                        value = formularioReparacionViewModel.horasReales?.toString() ?: "",
                        onValueChange = {
                            formularioReparacionViewModel.horasReales =
                                it.replace(",", ".").toDoubleOrNull()
                        },
                        label = { Text("Horas reales") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // IMPORTE TOTAL
                    OutlinedTextField(
                        value = formularioReparacionViewModel.importeTotal?.toString() ?: "",
                        onValueChange = {
                            formularioReparacionViewModel.importeTotal =
                                it.replace(",", ".").toDoubleOrNull()
                        },
                        label = { Text("Importe total (€)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }



                // -----------------------
                // BOTÓN GUARDAR
                // -----------------------
                Button(
                    onClick = {
                        if (idReparacion == null)
                            formularioReparacionViewModel.crearReparacion()
                        else
                            formularioReparacionViewModel.actualizarReparacion(idReparacion)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                ) {
                    Text(
                        text = if (idReparacion == null)
                            "Guardar Reparación"
                        else
                            "Guardar Cambios",
                        color = Color.White
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    expanded: Boolean,
    enabled: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<String>,
    onItemSelected: (Int) -> Unit,
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) onExpandedChange(!expanded) },
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaFinField(
    label: String = "Fecha de fin",
    fechaFin: LocalDate?,
    onFechaSelected: (LocalDate?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaFin
            ?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true }
    ) {
        OutlinedTextField(
            value = fechaFin?.toString() ?: "Sin finalizar",
            onValueChange = {},
            label = { Text(label) },
            readOnly = false,
            enabled = false, // importante
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (fechaFin != null) {
                    IconButton(onClick = { onFechaSelected(null) }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Limpiar fecha"
                        )
                    }
                }
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val nuevaFecha = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onFechaSelected(nuevaFecha)
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
