package com.example.mecanicsync.dashboard.ui.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mecanicsync.clientes.ui.viewmodel.ClienteViewModel
import com.example.mecanicsync.dashboard.ui.viewmodel.DashboardViewModel
import com.example.mecanicsync.navigation.VerClientes
import com.example.mecanicsync.navigation.VerClientesEnTaller
import com.example.mecanicsync.navigation.VerDatosUsuario
import com.example.mecanicsync.navigation.VerHistoricoReparacionesScreen
import com.example.mecanicsync.navigation.VerLogin
import com.example.mecanicsync.navigation.VerReparaciones
import com.example.mecanicsync.navigation.VerTodasLasReparacionesScreen
import com.example.mecanicsync.navigation.VerUsuarios
import com.example.mecanicsync.navigation.VerVehiculos
import com.example.mecanicsync.navigation.VerVehiculosEnTaller
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40
import com.example.mecanicsync.vehiculos.ui.viewmodel.VehiculoViewModel
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerHomeAdmin(
    navController: NavHostController,
    currentUserEmail: String,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    clienteViewModel: ClienteViewModel = hiltViewModel(),
    vehiculoViewModel: VehiculoViewModel = hiltViewModel()
) {
    // Estado para controlar la visibilidad del menú desplegable
    var menuExpanded by remember { mutableStateOf(false) }
    val uiState = dashboardViewModel.uiState
    val isLoading = dashboardViewModel.isLoading

    // Cada vez que se elimina un cliente o vehículo, recarga el dashboard
    LaunchedEffect(Unit) {
        clienteViewModel.deleteEvent.collect {
            dashboardViewModel.loadDashboard()
        }
    }

    LaunchedEffect(Unit) {
        vehiculoViewModel.deleteEvent.collect {
            dashboardViewModel.loadDashboard()
        }
    }

    // Cargar inicialmente
    LaunchedEffect(Unit) {
        dashboardViewModel.loadDashboard()
    }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Administrador") },
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

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            } else {
                uiState?.let { data ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text("En taller", style = MaterialTheme.typography.titleLarge)

                        Row(

                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            DashboardCard(
                                title = "Clientes",
                                value = data.clientesEnTaller.toString(),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.navigate(VerClientesEnTaller)
                                }
                            )

                            DashboardCard(
                                title = "Vehículos",
                                value = data.vehiculosEnTaller.toString(),
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate(VerVehiculosEnTaller) }
                            )
                        }
                        Text(
                            "Estado de reparaciones en taller",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            DashboardCard(
                                title = "Pendientes",
                                value = data.pendientes.toString(),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.navigate(VerReparaciones("pendiente"))

                                }
                            )

                            DashboardCard(
                                title = "En proceso",
                                value = data.enProceso.toString(),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.navigate(VerReparaciones("En Proceso"))

                                }
                            )

                            DashboardCard(
                                title = "Finalizadas",
                                value = data.finalizadas.toString(),
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    navController.navigate(VerReparaciones("Finalizada"))

                                }
                            )

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Accesos rápidos", style = MaterialTheme.typography.titleLarge)

                        QuickActionButton("Usuarios") {
                            navController.navigate(VerUsuarios)
                        }
                        QuickActionButton("Clientes") {
                            navController.navigate(VerClientes)
                        }
                        QuickActionButton("Vehiculos") {
                            navController.navigate(VerVehiculos)
                        }
                        QuickActionButton("Reparaciones") {
                            navController.navigate(VerTodasLasReparacionesScreen)
                        }
                        QuickActionButton("Historico de reparaciones") {
                            navController.navigate(VerHistoricoReparacionesScreen)
                        }


                    }
                }
            }
        }
    }
}


@Composable
fun QuickActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text)
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .then(
                if (onClick != null)
                    Modifier.clickable { onClick() }
                else
                    Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun UserActionMenu(
    navController: NavHostController,
    currentUserEmail: String
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Icono que activa el menú
    IconButton(onClick = { menuExpanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Menú de usuario",
            tint = Color.White
        )
    }

    // Menú desplegable
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
    ) {
        // 1. Opción: Ver datos de usuario
        DropdownMenuItem(
            text = { Text("Ver perfil") },
            onClick = {
                menuExpanded = false
                // Navega a la pantalla de datos de usuario (asumiendo que existe VerDatosUsuario)
                navController.navigate(VerDatosUsuario(currentUserEmail))
            }
        )
        //2. Opción: Sobre mi

        Box {
            DropdownMenuItem(
                text = { Text("Sobre mí") },
                onClick = {
                    menuExpanded = false

                    try {
                        val file = File(context.cacheDir, "cv.pdf")

                        // Copiar desde assets a cache
                        context.assets.open("cv.pdf").use { input ->
                            FileOutputStream(file).use { output ->
                                input.copyTo(output)
                            }
                        }

                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, "application/pdf")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }

                        context.startActivity(intent)

                    } catch (e: Exception) {
                        Toast.makeText(context, "No se pudo abrir el CV", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }


        // 3. Opción: Cerrar Sesión
        DropdownMenuItem(
            text = { Text("Cerrar Sesión") },
            onClick = {
                menuExpanded = false
                // Cierra sesión: navega al login y limpia la pila de navegación
                navController.navigate(VerLogin) {
                    // Limpia hasta el inicio (VerLogin)
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        )
    }
}