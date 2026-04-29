package com.example.mecanicsync.dashboard.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mecanicsync.navigation.VerHistoricoReparacionesScreen
import com.example.mecanicsync.navigation.VerTodasLasReparacionesScreen
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerHomeMecanico(
    navController: NavHostController,
    currentUserEmail: String,
) {
    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Mecánico") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = Color.White
                    ),
                    actions = {

                        UserActionMenu(navController = navController, currentUserEmail = currentUserEmail)
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Tareas", style = MaterialTheme.typography.titleLarge)

                // Botón para ir a todas las Reparaciones
                QuickActionButton("Reparaciones") {
                    navController.navigate(VerTodasLasReparacionesScreen)
                }

                // Botón para ir al Histórico de Reparaciones
                QuickActionButton("Historico de reparaciones") {
                    navController.navigate(VerHistoricoReparacionesScreen)
                }
            }
        }
    }
}