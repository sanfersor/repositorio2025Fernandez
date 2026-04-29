package com.example.mecanicsync.vehiculos.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.mecanicsync.clientes.data.network.ClienteResponse
import com.example.mecanicsync.marca.data.network.MarcaResponse
import com.example.mecanicsync.modelo.data.network.ModeloResponse
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado
import com.example.mecanicsync.vehiculos.ui.viewmodel.FormularioVehiculoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioVehiculo(
    navController: NavController,
    vehiculoExistenteId: Int? = null,
    formularioVehiculoViewModel: FormularioVehiculoViewModel = hiltViewModel()
) {
    val clientes by formularioVehiculoViewModel.clientes.collectAsState()
    val marcas by formularioVehiculoViewModel.marcas.collectAsState()
    val modelos by formularioVehiculoViewModel.modelos.collectAsState()
    val estadoCreacion by formularioVehiculoViewModel.estadoCreacion.collectAsState()

    // Estados locales
    var matricula by remember { mutableStateOf("") }
    var anioMatriculacion by remember { mutableStateOf("") }

    var clienteSeleccionado by remember { mutableStateOf<ClienteResponse?>(null) }
    var marcaSeleccionada by remember { mutableStateOf<MarcaResponse?>(null) }
    var modeloSeleccionado by remember { mutableStateOf<ModeloResponse?>(null) }

    var clienteDropdownExpanded by remember { mutableStateOf(false) }
    var marcaDropdownExpanded by remember { mutableStateOf(false) }
    var modeloDropdownExpanded by remember { mutableStateOf(false) }

    val vehiculoInicialVM by formularioVehiculoViewModel.vehiculoInicial.collectAsState() // <-- Observar el VM

    // Se ejecuta cuando el ID del vehículo cambia, O cuando la lista de clientes o marcas terminan de cargar
    LaunchedEffect(vehiculoExistenteId) {
        if (vehiculoExistenteId != null) {
            formularioVehiculoViewModel.iniciarEdicion(vehiculoExistenteId) // <-- Llama al VM
        }
    }
    // Esta variable de control ahora solo se ocupará del CLIENTE y la MARCA
    var primaryDataInitialized by remember { mutableStateOf(false) }
// Esta variable de control se ocupará SOLO del MODELO
    var modelDataInitialized by remember { mutableStateOf(false) }


// 1. LAUNCHED EFFECT para CLIENTE y MARCA (Se ejecuta cuando el Vehículo y las listas primarias están listas)
    LaunchedEffect(vehiculoInicialVM, clientes, marcas) {
        val vehiculo = vehiculoInicialVM

        // Solo inicializa Cliente y Marca si el vehículo y sus listas están cargadas y no hemos terminado
        if (vehiculo != null && clientes.isNotEmpty() && marcas.isNotEmpty() && !primaryDataInitialized) {

            // Rellenar campos simples
            matricula = vehiculo.matricula
            anioMatriculacion = vehiculo.anioMatriculacion.toString()

            // ASIGNAR CLIENTE
            clienteSeleccionado = clientes.find { it.id_cliente == vehiculo.id_cliente }

            // ASIGNAR MARCA
            marcaSeleccionada = marcas.find { it.nombreMarca == vehiculo.marca }


            if (clienteSeleccionado != null && marcaSeleccionada != null) {
                primaryDataInitialized = true

            }
        }
    }


// 2. LAUNCHED EFFECT para MODELO (Se ejecuta solo cuando la lista de modelos cambia)
    LaunchedEffect(modelos) {
        val vehiculo = vehiculoInicialVM // Usamos el vehículo que ya fue cargado

        // Solo inicializa el Modelo si:
        // a) El vehículo fue cargado (estamos en modo edición).
        // b) La lista 'modelos' está llena (la carga asíncrona terminó).
        // c) Aún no hemos inicializado el modelo.
        if (vehiculo != null && modelos.isNotEmpty() && !modelDataInitialized) {

            // ASIGNAR MODELO
            modeloSeleccionado = modelos.find { it.idModelo == vehiculo.id_modelo }

            // Marcamos como finalizado
            modelDataInitialized = true
        }
    }
    // ----------------------------------------------------
    // Manejo de resultado de creación/actualización
    // ----------------------------------------------------
    LaunchedEffect(estadoCreacion) {
        when (estadoCreacion) {
            CreacionEstado.Exito -> navController.popBackStack()
            is CreacionEstado.Fallo -> formularioVehiculoViewModel.limpiarEstadoCreacion()
            else -> {}
        }
    }

    // ----------------------------------------------------
    // UI
    // ----------------------------------------------------
    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (vehiculoExistenteId == null) "Nuevo Vehículo" else "Editar Vehículo") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Cancelar"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF673AB7),
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

                // CLIENTE
                VehiculoDropdownField(
                    label = "Seleccionar Cliente *",
                    value = clienteSeleccionado?.cliente ?: "",
                    expanded = clienteDropdownExpanded,
                    onExpandedChange = { clienteDropdownExpanded = it },
                    items = clientes.map { it.cliente },
                    onItemSelected = { index ->
                        clienteSeleccionado = clientes[index]
                        clienteDropdownExpanded = false
                    },
                    leadingIcon = Icons.Filled.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                // MARCA
                VehiculoDropdownField(
                    label = "Seleccionar Marca *",
                    value = marcaSeleccionada?.nombreMarca ?: "",
                    expanded = marcaDropdownExpanded,
                    onExpandedChange = { marcaDropdownExpanded = it },
                    items = marcas.map { it.nombreMarca },
                    onItemSelected = { index ->
                        val nuevaMarca = marcas[index]

                        // Solo si la marca cambia, se resetea el modelo
                        if (nuevaMarca.idMarca != marcaSeleccionada?.idMarca) {
                            marcaSeleccionada = nuevaMarca
                            modeloSeleccionado = null // <-- ¡IMPORTANTE! Limpiar el modelo
                            formularioVehiculoViewModel.limpiarModelos() // <-- Opcional, pero recomendable
                            formularioVehiculoViewModel.cargarModelosPorMarca(nuevaMarca.idMarca)
                        }
                        marcaDropdownExpanded = false
                    },
                    leadingIcon = Icons.Filled.Category
                )

                Spacer(modifier = Modifier.height(16.dp))

                // MODELO
                val modeloEnabled = marcaSeleccionada != null && modelos.isNotEmpty()
                VehiculoDropdownField(
                    label = "Seleccionar Modelo *",
                    value = modeloSeleccionado?.nombreModelo ?: "",
                    expanded = modeloDropdownExpanded,
                    onExpandedChange = { if (modeloEnabled) modeloDropdownExpanded = it },
                    items = modelos.map { it.nombreModelo },
                    onItemSelected = { index ->
                        modeloSeleccionado = modelos[index]
                        modeloDropdownExpanded = false
                    },
                    leadingIcon = Icons.Filled.DirectionsCar,
                    enabled = modeloEnabled
                )

                Spacer(modifier = Modifier.height(16.dp))

                // MATRÍCULA
                OutlinedTextField(
                    value = matricula,
                    onValueChange = { matricula = it.uppercase() },
                    label = { Text("Matrícula *") },
                    leadingIcon = { Icon(Icons.Filled.Numbers, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // AÑO
                OutlinedTextField(
                    value = anioMatriculacion,
                    onValueChange = {
                        if (it.length <= 4 && it.all { c -> c.isDigit() }) anioMatriculacion = it
                    },
                    label = { Text("Año de Matriculación *") },
                    leadingIcon = { Icon(Icons.Filled.Today, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                val isFormValid = clienteSeleccionado != null &&
                        marcaSeleccionada != null &&
                        modeloSeleccionado != null &&
                        matricula.isNotBlank() &&
                        anioMatriculacion.length == 4 && anioMatriculacion.toIntOrNull() != null

                Button(
                    onClick = {
                        if (isFormValid) {
                            formularioVehiculoViewModel.guardarVehiculo(
                                idCliente = clienteSeleccionado!!.id_cliente,
                                idModelo = modeloSeleccionado!!.idModelo,
                                matricula = matricula,
                                anioMatriculacion = anioMatriculacion.toInt(),
                                vehiculoExistenteId = vehiculoExistenteId
                            )
                        }
                    },
                    enabled = isFormValid && estadoCreacion != CreacionEstado.Cargando,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                ) {
                    if (estadoCreacion == CreacionEstado.Cargando) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Guardar Vehículo",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


// ----------------------------------------------------
// Componente Reutilizable para el Dropdown (Spinner)
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculoDropdownField(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<String>,
    onItemSelected: (Int) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean = true
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // TextField que simula el Spinner
        OutlinedTextField(
            value = value,
            onValueChange = { /* No se permite escribir */ },
            label = { Text(label) },
            readOnly = true, // Evita la edición directa
            leadingIcon = { Icon(leadingIcon, contentDescription = null) },
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Desplegar",
                    modifier = Modifier.clickable { if (enabled) onExpandedChange(true) }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (enabled) onExpandedChange(true) }, // Toda la caja es clickeable
            shape = RoundedCornerShape(12.dp),
            enabled = enabled
        )

        // DropdownMenu que aparece al clickear el TextField
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.width(IntrinsicSize.Max).width(300.dp) // Ajustar el ancho
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