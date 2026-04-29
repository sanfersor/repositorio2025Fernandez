package com.example.mecanicsync.reparaciones.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import com.example.mecanicsync.reparaciones.ui.viewmodel.ReparacionesViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerReparaciones(
    navController: NavController,
    estado: String,
    reparacionesViewModel: ReparacionesViewModel = hiltViewModel()
) {
    val reparaciones by reparacionesViewModel.reparaciones.collectAsState()
    val isLoading = reparacionesViewModel.isLoading

    val filtradas = reparaciones.filter {
        it.estado.equals(estado, ignoreCase = true)
    }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Reparaciones $estado") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Volver atrás"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    )
                )
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

                    filtradas.isEmpty() -> {
                        Text(
                            "No hay reparaciones en estado \"$estado\"",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.padding(innerPadding)
                        ){
                            items(filtradas) { reparacion ->
                                ReparacionItem(reparacion, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReparacionItem(
    reparacion: Reparacion,
    navController: NavController,
    onEditClick: ((Reparacion) -> Unit)? = null,
    onDeleteClick: ((Int) -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier
            .padding(16.dp)) {

            Text("Nª ${reparacion.id_reparacion}",
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(6.dp))

            Text("Matrícula: ${reparacion.matricula}",
                fontWeight = FontWeight.Bold)
            Text("${reparacion.tipo_reparacion}")
            Text("${reparacion.estado}",fontWeight = FontWeight.Bold, color = Color.Blue)

            Spacer(modifier = Modifier.height(6.dp))

            Text("Inicio: ${reparacion.fecha_inicio} Fin: ${reparacion.fecha_fin ?: "Sin finalizar"}")


            Spacer(modifier = Modifier.height(6.dp))

            Text("Horas reales: ${reparacion.horas_reales}")
            Text("Importe total: ${reparacion.importe_total} €")
        }
        // Sección de Botones de Acción
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

        // Condición 1: Mostrar Botón Editar SOLO si onEditClick NO es null
            onEditClick?.let { onClick ->
                IconButton(onClick = { onClick(reparacion) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar Reparacion",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Condición 1: Mostrar Botón Eliminar SOLO si onDeleteClick NO es null
            onDeleteClick?.let { onClick ->
                IconButton(onClick = { onClick(reparacion.id_reparacion) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar Reparación",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}



