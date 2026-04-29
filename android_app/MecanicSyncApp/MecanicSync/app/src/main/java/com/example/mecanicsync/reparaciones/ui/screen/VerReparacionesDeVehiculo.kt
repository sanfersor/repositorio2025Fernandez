package com.example.mecanicsync.reparaciones.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mecanicsync.reparaciones.ui.model.Reparacion
import com.example.mecanicsync.reparaciones.ui.viewmodel.ReparacionesViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerReparacionesDeVehiculo(
    idVehiculo: Int,
    navController: NavController,
    reparacionesViewModel: ReparacionesViewModel= hiltViewModel()
) {
    val reparaciones by reparacionesViewModel.reparacionesVehiculo.collectAsState()


    LaunchedEffect(idVehiculo) {
        reparacionesViewModel.getReparacionesVehiculo(idVehiculo)
    }
    MecanicSyncTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Reparaciones del vehículo") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(reparaciones) { rep ->
                    ReparacionVehiculoItem(rep, navController= navController)
                }
            }
        }
    }

}


@Composable
fun ReparacionVehiculoItem(
    reparacion: Reparacion,
    navController: NavController
){
    Card ( modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {


        Column  {
            Text(
                text = reparacion.matricula,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 3.dp)
            )

            Text(
                text = reparacion.estado,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 3.dp)
            )
            Text(
                text = reparacion.fecha_inicio,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 3.dp)
            )
            reparacion.fecha_fin?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(all = 3.dp)
                )
            }
            Text(
                text = reparacion.horas_reales.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 3.dp)
            )
            Text(
                text = reparacion.importe_total.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(all = 3.dp)
            )

        }
    }
}



