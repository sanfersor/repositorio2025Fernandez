package com.example.mecanicsync.vehiculos.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.clientes.ui.screen.AddFloatingActionButton
import com.example.mecanicsync.navigation.AgregarVehiculo
import com.example.mecanicsync.navigation.EditarVehiculo
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40
import com.example.mecanicsync.vehiculos.ui.model.Vehiculo
import com.example.mecanicsync.vehiculos.ui.viewmodel.VehiculoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerVehiculos(
    navController: NavController,
    vehiculoViewModel: VehiculoViewModel = hiltViewModel()
){
    val vehiculos by vehiculoViewModel.vehiculo.collectAsState()
    var idVehiculoAEliminar by remember { mutableStateOf<Int?>(null) }

    // Cargar vehículos al inicio
    LaunchedEffect(true) {
        vehiculoViewModel.cargarVehiculos()
    }
    LaunchedEffect(Unit) {
        vehiculoViewModel.deleteEvent.collect {
            vehiculoViewModel.cargarVehiculos()
        }
    }


    val onEditVehiculo: (Vehiculo) -> Unit = { vehiculo ->
        navController.navigate(EditarVehiculo(vehiculo.id_vehiculo))
    }

    val onDeleteVehiculo:  (Int) -> Unit = { id ->
        idVehiculoAEliminar = id
    }

    MecanicSyncTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Vehículos") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                AddFloatingActionButton(onClick = { navController.navigate(AgregarVehiculo) })
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(vehiculos) { vehiculo ->
                    VehiculoItem(
                        vehiculo,
                        navController = navController,
                        onEditClick = onEditVehiculo,
                        onDeleteClick = onDeleteVehiculo
                    )
                }
            }

            // Diálogo de confirmación
            idVehiculoAEliminar?.let { id ->
                AlertDialog(
                    onDismissRequest = { idVehiculoAEliminar = null },
                    title = { Text("Eliminar vehículo") },
                    text = { Text("¿Seguro que deseas eliminar este vehículo?") },
                    confirmButton = {
                        TextButton(onClick = {
                            vehiculoViewModel.eliminarVehiculo(id)
                            idVehiculoAEliminar = null
                        }) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { idVehiculoAEliminar = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun VehiculoItem(
    vehiculo: Vehiculo,
    navController: NavController,
    onEditClick: ((Vehiculo) -> Unit)? = null,
    onDeleteClick: ((Int) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),

        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f) // Esto empuja los iconos a la derecha
            ) {
                Text(
                    text = "Matrícula: ${vehiculo.matricula}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 3.dp)
                )
                Text(
                    text = "Marca: ${vehiculo.marca}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(all = 1.dp)

                )
                Text(
                    text = "Modelo: ${vehiculo.modelo}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(all = 1.dp)
                )

                Text(
                    text = "Año: ${vehiculo.anioMatriculacion}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(all = 3.dp)
                )


            }
            // Sección de Botones de Acción
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                // Condición 1: Mostrar Botón Editar SOLO si onEditClick NO es null
                onEditClick?.let { onClick ->
                    IconButton(onClick = { onClick(vehiculo) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Vehiculo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Condición 2: Mostrar Botón Eliminar SOLO si onDeleteClick NO es null
                onDeleteClick?.let { onClick ->
                    IconButton(onClick = { onClick(vehiculo.id_vehiculo) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar Vehiculo",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


