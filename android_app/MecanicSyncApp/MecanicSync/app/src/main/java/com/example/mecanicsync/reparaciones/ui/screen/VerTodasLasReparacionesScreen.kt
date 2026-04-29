package com.example.mecanicsync.reparaciones.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.clientes.ui.screen.AddFloatingActionButton
import com.example.mecanicsync.navigation.AgregarReparacion
import com.example.mecanicsync.navigation.EditarReparacion
import com.example.mecanicsync.reparaciones.ui.model.Reparacion

import com.example.mecanicsync.reparaciones.ui.viewmodel.ReparacionesViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerTodasLasReparacionesScreen(
    navController: NavController,
    reparacionesViewModel: ReparacionesViewModel = hiltViewModel()
) {
    val reparaciones by reparacionesViewModel.reparaciones.collectAsState()
    val isLoading = reparacionesViewModel.isLoading
    var idReparacionEliminar by remember { mutableStateOf<Int?>(null)}

    LaunchedEffect(true) {
        reparacionesViewModel.cargarReparaciones()
    }
    //Si se elimina, se recarga la lista
    LaunchedEffect(Unit) {
        reparacionesViewModel.deleteEvent.collect {
            reparacionesViewModel.cargarReparaciones()
        }
    }
    val onEditReparacion:  (Reparacion) -> Unit = { reparacion ->
        navController.navigate(EditarReparacion(reparacion.id_reparacion))
    }
    val onDeleteReparacion:  (Int) -> Unit = { id ->

        idReparacionEliminar = id
    }

    MecanicSyncTheme{
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Todas las reparaciones") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Volver a Inicio"
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
                AddFloatingActionButton(onClick = {
                    // Lógica de navegación específica para esta pantalla
                    navController.navigate(AgregarReparacion)
                })
            }

        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    reparaciones.isEmpty() -> {
                        Text(
                            "No hay reparaciones registradas",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            items(reparaciones) { reparaciones ->
                                ReparacionItem(
                                    reparacion = reparaciones,
                                    navController = navController,
                                    onEditClick = onEditReparacion,
                                    onDeleteClick = onDeleteReparacion)
                            }
                        }
                        //Dialogo de confirmación
                        idReparacionEliminar?.let { id ->
                            AlertDialog(
                                onDismissRequest = {idReparacionEliminar = null},
                                title = {Text("Eliminar Reparación")},
                                text = {Text("¿Seguro que deseas eliminar esta reparación?")},
                                confirmButton = {
                                    TextButton(onClick = {
                                        reparacionesViewModel.eliminarReparacion(id)
                                        idReparacionEliminar = null
                                    }) {
                                        Text("Eliminar", color = Color.Red)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { idReparacionEliminar = null }) {
                                        Text("Cancelar")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

