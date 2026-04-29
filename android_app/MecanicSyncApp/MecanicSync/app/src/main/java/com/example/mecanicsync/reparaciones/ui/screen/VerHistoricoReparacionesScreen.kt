package com.example.mecanicsync.reparaciones.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.navigation.VerHistoricoReparacionesScreen
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import com.example.mecanicsync.reparaciones.ui.viewmodel.HistoricoReparacionesViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerHistoricoReparacionesScreen(
    navController: NavController,
    historicoReparacionesViewModel: HistoricoReparacionesViewModel = hiltViewModel()
) {
    var matricula by remember { mutableStateOf("") }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Histórico de reparaciones") },
                    navigationIcon = {

                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White,
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
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = matricula,
                    onValueChange = { matricula = it },
                    label = { Text("Matrícula") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { historicoReparacionesViewModel.buscarPorMatricula(matricula) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar")
                }

                Spacer(Modifier.height(12.dp))

                if (historicoReparacionesViewModel.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    return@Column
                }

                historicoReparacionesViewModel.error?.let { err ->
                    Text(err, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                }

                historicoReparacionesViewModel.nombreCliente?.let { nombre ->
                    Text("Cliente: $nombre", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                historicoReparacionesViewModel.vehiculoInfo?.let { v ->
                    Text(
                        "Vehículo: ${v.marca ?: ""} ${v.modelo ?: ""} — ${v.matricula}",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (historicoReparacionesViewModel.reparaciones.isEmpty()) {
                    Text(
                        "No hay reparaciones para la matrícula indicada",
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(historicoReparacionesViewModel.reparaciones) { rep ->
                            ReparacionHistoricoItem(rep)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ReparacionHistoricoItem(rep: Reparacion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Reparación #${rep.id_reparacion}", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Tipo: ${rep.tipo_reparacion}", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text("Inicio: ${rep.fecha_inicio}")
            Text("Fin: ${rep.fecha_fin ?: "—"}")
            Spacer(Modifier.height(6.dp))
            Text("Estado: ${rep.estado}")
            Spacer(Modifier.height(8.dp))
            EstadoBarra(rep.estado)
            Spacer(Modifier.height(8.dp))
            Text("Importe: ${rep.importe_total ?: 0.0} €")
        }
    }
}

@Composable
fun EstadoBarra(estado: String) {
    val pasos = listOf("Pendiente", "En proceso", "Finalizada", "Entregado")

    val indiceActual = when (estado.lowercase()) {
        "pendiente", "recepcion", "recepción" -> 0
        "en proceso", "reparacion", "reparación", "diagnostico", "diagnóstico" -> 1
        "finalizado", "finalizada" -> 2
        "entregado" -> 3
        else -> 0
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        pasos.forEachIndexed { index, paso ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Punto
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                index < indiceActual -> Color(0xFF4CAF50) // completado
                                index == indiceActual -> Color(0xFFFFC107) // actual
                                else -> Color(0xFFBDBDBD) // pendiente
                            }
                        )
                )
                Spacer(Modifier.height(4.dp))
                Text(paso, fontSize = 12.sp, textAlign = TextAlign.Center)
            }

            if (index < pasos.size - 1) {
                // Línea entre pasos
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f)
                        .background(
                            if (index < indiceActual) Color(0xFF4CAF50)
                            else Color(0xFFBDBDBD)
                        )
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
