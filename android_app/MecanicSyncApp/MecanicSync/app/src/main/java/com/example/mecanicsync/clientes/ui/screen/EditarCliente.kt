package com.example.mecanicsync.clientes.ui.screen

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mecanicsync.clientes.ui.viewmodel.FormularioClienteViewModel
import com.example.mecanicsync.ui.theme.MecanicSyncTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCliente(
    navController: NavController,
    idCliente: Int?,
    formularioClienteViewModel: FormularioClienteViewModel = hiltViewModel()
) {
    val nombre by formularioClienteViewModel.nombre.collectAsStateWithLifecycle()

    val telefono by formularioClienteViewModel.telefono.collectAsStateWithLifecycle()
    val email by formularioClienteViewModel.email.collectAsStateWithLifecycle()

    val nombreError by formularioClienteViewModel.nombreError.collectAsStateWithLifecycle()
    val telefonoError by formularioClienteViewModel.telefonoError.collectAsStateWithLifecycle()

    val saveResult by formularioClienteViewModel.saveResult.collectAsStateWithLifecycle()
    val clienteLoaded by formularioClienteViewModel.clienteLoaded.collectAsStateWithLifecycle()

    //CARGAR DATOS SI ES EDICIÓN
    LaunchedEffect(idCliente, clienteLoaded) {
        if (idCliente != null && !clienteLoaded) {
            formularioClienteViewModel.cargarCliente(idCliente)
        }
    }

    // Manejar el resultado de la operación (éxito o fallo de la API)
    LaunchedEffect(saveResult) {
        saveResult?.let { result ->
            result.fold(
                onSuccess = {
                    // Navegación de éxito
                    formularioClienteViewModel.clearSaveResult()
                    navController.popBackStack()
                },
                onFailure = { error ->

                    println("Error al guardar cliente: ${error.message}")
                    formularioClienteViewModel.clearSaveResult()
                }
            )
        }
    }
    MecanicSyncTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (idCliente == null) "Nuevo Cliente" else "Editar Cliente") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.White,
                                contentDescription = "Cancelar"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF673AB7), // Purple40
                        titleContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(

                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite el scroll en pantallas pequeñas
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Campo Nombre del Cliente
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { formularioClienteViewModel.nombre.value = it },
                    label = { Text("Nombre Completo del Cliente") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    isError = nombreError != null,
                    supportingText = { if (nombreError != null) Text(nombreError!!) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // Campo Teléfono
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { formularioClienteViewModel.telefono.value = it },
                    label = { Text("Teléfono") },
                    leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                    isError = telefonoError != null,
                    supportingText = { if (telefonoError != null) Text(telefonoError!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { formularioClienteViewModel.email.value = it },
                    label = { Text("Email (Opcional)") },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Guardar
                Button(
                    onClick = {
                        if (idCliente == null) {
                            formularioClienteViewModel.crearCliente()
                        } else {
                            formularioClienteViewModel.actualizarCliente(idCliente)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                ) {
                    Text(
                        text = if (idCliente == null) "Guardar Cliente" else "Guardar Cambios",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}