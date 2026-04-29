package com.example.mecanicsync.usuario.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import com.example.mecanicsync.reparaciones.ui.screen.DropdownField
import com.example.mecanicsync.ui.theme.MecanicSyncTheme
import com.example.mecanicsync.ui.theme.Purple40
import com.example.mecanicsync.usuario.ui.viewmodel.FormularioUsuarioViewModel
import com.example.mecanicsync.vehiculos.ui.model.CreacionEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioUsuario(
    navController: NavController,
    idUsuario: Int?,
    viewModel: FormularioUsuarioViewModel = hiltViewModel()
) {
    val estado by viewModel.estadoGuardado.collectAsState()
    var rolExpanded by remember { mutableStateOf(false) }

    // Cargar usuario al entrar
    LaunchedEffect(idUsuario) {
        if (idUsuario != null) {
            viewModel.cargarUsuario(idUsuario)
        }
    }


    // Manejar resultado
    LaunchedEffect(estado) {
        when (estado) {
            CreacionEstado.Exito -> navController.popBackStack()
            is CreacionEstado.Fallo -> viewModel.limpiarEstado()
            else -> {}
        }
    }

    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(if (idUsuario == null) "Nuevo Usuario" else "Editar Usuario")
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
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

                // USUARIO
                OutlinedTextField(
                    value = viewModel.usuario,
                    onValueChange = { viewModel.usuario = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // EMAIL
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // ROL (ÚNICO DROPDOWN)
                DropdownField(
                    label = "Rol",
                    value = viewModel.rol,
                    expanded = rolExpanded,
                    enabled = true,
                    onExpandedChange = { rolExpanded = it },
                    items = viewModel.listaRoles,
                    onItemSelected = { index ->
                        viewModel.rol = viewModel.listaRoles[index]
                        rolExpanded = false
                    }
                )

                Spacer(Modifier.height(32.dp))

                // GUARDAR
                Button(
                    onClick = {
                        if (idUsuario == null) {
                            viewModel.crearUsuario()
                        } else {
                            viewModel.actualizarUsuario(idUsuario)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40)
                ) {
                    Text("Guardar Cambios", color = Color.White)
                }
            }
        }
    }
}
