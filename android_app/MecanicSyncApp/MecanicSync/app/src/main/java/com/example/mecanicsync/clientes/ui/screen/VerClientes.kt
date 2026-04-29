@file:Suppress("UNCHECKED_CAST")

package com.example.mecanicsync.clientes.ui.screen

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
import com.example.mecanicsync.clientes.ui.model.Cliente
import com.example.mecanicsync.clientes.ui.viewmodel.ClienteViewModel
import com.example.mecanicsync.navigation.AgregarCliente
import com.example.mecanicsync.navigation.EditarCliente
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerClientes(
    navController: NavController,
    clienteViewModel: ClienteViewModel = hiltViewModel()
) {
    val cliente by clienteViewModel.cliente.collectAsState()
    var idClienteAEliminar by remember { mutableStateOf<Int?>(null) }


    LaunchedEffect(true) {
        // Esta función se ejecuta la primera vez que la pantalla se compone
        clienteViewModel.cargarClientes()
    }
    //Si se elimina, recarga la lista
    LaunchedEffect(Unit) {
        clienteViewModel.deleteEvent.collect {
            clienteViewModel.cargarClientes()
        }

    }

    val onEditCliente:  (Cliente) -> Unit = { cliente ->
        navController.navigate(EditarCliente(cliente.id_cliente))
    }

    val onDeleteCliente:  (Int) -> Unit = { id ->

        idClienteAEliminar = id
    }

    MecanicSyncTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Clientes") },
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
                    navController.navigate(AgregarCliente)
                })
            }

            ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(cliente) { cliente ->
                    ClienteItem(
                        cliente = cliente,
                        navController = navController,
                        onEditClick = onEditCliente,
                        onDeleteClick = onDeleteCliente
                    )
                }
            }

            //Dialogo de confirmacion
            idClienteAEliminar?.let{id ->
                AlertDialog(
                    onDismissRequest = {idClienteAEliminar= null},
                    title = {Text("Eliminar Cliente")},
                    text = {Text("¿Seguro que deseas eliminar este cliente?")},
                    confirmButton = {
                        TextButton(onClick = {
                            clienteViewModel.eliminarCliente(id)
                            idClienteAEliminar = null
                        }) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { idClienteAEliminar = null }) {
                            Text("Cancelar")
                        }
                    }

                )
            }
        }
    }
}

@Composable
fun ClienteItem(
    cliente: Cliente,
    navController: NavController,
    onEditClick: ((Cliente) -> Unit)? = null,
    onDeleteClick: ((Int) -> Unit)? = null

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
               verticalAlignment = Alignment.CenterVertically
        ) {

            // Sección de Datos del Cliente (ocupa el espacio restante)
            Column(
                modifier = Modifier.weight(1f) // Esto empuja los iconos a la derecha
            ) {
                Text(
                    text = cliente.cliente,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(all = 3.dp)
                )

                Text(
                    text = cliente.telefono,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(all = 3.dp)
                )
                Text(
                    text = cliente.email,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(all = 3.dp)
                )

            }
            // Sección de Botones de Acción
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                // Condición 1: Mostrar Botón Editar SOLO si onEditClick NO es null
                onEditClick?.let { onClick ->
                    IconButton(onClick = { onClick(cliente) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar Cliente",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Condición 2: Mostrar Botón Eliminar SOLO si onDeleteClick NO es null
                onDeleteClick?.let { onClick ->
                    IconButton(onClick = { onClick(cliente.id_cliente) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar Cliente",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Purple40,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Añadir elemento" // Descripción genérica para reutilización
        )
    }
}





