package com.example.mecanicsync.usuario.ui.screen

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import com.example.mecanicsync.clientes.ui.screen.AddFloatingActionButton
import com.example.mecanicsync.navigation.AgregarUsuario
import com.example.mecanicsync.navigation.EditarUsuario
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40
import com.example.mecanicsync.usuario.ui.model.Usuario
import com.example.mecanicsync.usuario.ui.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerUsuarios(
    navController: NavController,
    usuariosViewModel: UsuariosViewModel = hiltViewModel()
) {
    val usuarios by usuariosViewModel.usuarios.collectAsState()
    var idUsuarioAEliminar by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        usuariosViewModel.cargarUsuarios()
    }

    LaunchedEffect(Unit) {
        usuariosViewModel.deleteEvent.collect {
            usuariosViewModel.cargarUsuarios()
        }
    }

    val onEditUsuario: (Usuario) -> Unit = { usuario ->
        navController.navigate(EditarUsuario(usuario.idUsuario))
    }

    val onDeleteUsuario: (Int) -> Unit = { id ->
        idUsuarioAEliminar = id
    }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Usuarios") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
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
                AddFloatingActionButton {
                    navController.navigate(AgregarUsuario)
                }
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(usuarios) { usuario ->
                    UsuarioItem(
                        usuario = usuario,
                        onEditClick = onEditUsuario,
                        onDeleteClick = onDeleteUsuario
                    )
                }
            }

            //  Diálogo eliminar
            idUsuarioAEliminar?.let { id ->
                AlertDialog(
                    onDismissRequest = { idUsuarioAEliminar = null },
                    title = { Text("Eliminar Usuario") },
                    text = { Text("¿Seguro que deseas eliminar este usuario?") },
                    confirmButton = {
                        TextButton(onClick = {
                            usuariosViewModel.eliminarUsuario(id)
                            idUsuarioAEliminar = null
                        }) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { idUsuarioAEliminar = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun UsuarioItem(
    usuario: Usuario,
    onEditClick: (Usuario) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = usuario.usuario,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(text = usuario.email)
                Text(text = "Rol: ${usuario.rol}")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { onEditClick(usuario) }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Editar Usuario",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { onDeleteClick(usuario.idUsuario) }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar Usuario",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
