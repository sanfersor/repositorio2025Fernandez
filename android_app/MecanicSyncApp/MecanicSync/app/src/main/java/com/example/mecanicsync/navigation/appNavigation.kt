package com.example.mecanicsync.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mecanicsync.clientes.ui.screen.FormularioCliente
import com.example.mecanicsync.clientes.ui.screen.VerClientes
import com.example.mecanicsync.clientes.ui.screen.VerClientesEnTaller

import com.example.mecanicsync.dashboard.ui.screen.VerHomeAdmin
import com.example.mecanicsync.dashboard.ui.screen.VerHomeMecanico
import com.example.mecanicsync.login.ui.screen.VerLogin
import com.example.mecanicsync.reparaciones.ui.screen.FormularioReparacion
import com.example.mecanicsync.reparaciones.ui.screen.VerHistoricoReparacionesScreen
import com.example.mecanicsync.reparaciones.ui.screen.VerReparaciones
import com.example.mecanicsync.reparaciones.ui.screen.VerReparacionesDeVehiculo
import com.example.mecanicsync.reparaciones.ui.screen.VerTodasLasReparacionesScreen
import com.example.mecanicsync.usuario.ui.screen.FormularioUsuario
import com.example.mecanicsync.usuario.ui.screen.VerDatosUsuarioScreen
import com.example.mecanicsync.usuario.ui.screen.VerUsuarios
import com.example.mecanicsync.vehiculos.ui.screen.FormularioVehiculo
import com.example.mecanicsync.vehiculos.ui.screen.VerVehiculos
import com.example.mecanicsync.vehiculos.ui.screen.VerVehiculosEnTaller

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = VerLogin) {

        composable<VerLogin> {
            VerLogin(navController)
        }

        composable<VerHomeAdmin> { backStackEntry ->
            val args = backStackEntry.toRoute<VerHomeAdmin>()
            VerHomeAdmin(navController = navController, currentUserEmail = args.email)
        }

        composable<VerHomeMecanico> { backStackEntry ->
            val arg = backStackEntry.toRoute<VerHomeMecanico>()
            VerHomeMecanico(navController = navController, currentUserEmail = arg.email)
        }
        composable<VerUsuarios> {
            VerUsuarios(navController)
        }

        composable<AgregarUsuario> {
            FormularioUsuario(navController, null)
        }

        composable<EditarUsuario> { backStackEntry ->
            val args = backStackEntry.toRoute<EditarUsuario>()
            FormularioUsuario(
                navController = navController,
                idUsuario = args.idUsuario
            )
        }


        composable<VerDatosUsuario> { backStackEntry ->
            val args = backStackEntry.toRoute<VerDatosUsuario>()
            VerDatosUsuarioScreen(navController)
        }

        composable<VerReparaciones> { backStackEntry ->
            val args = backStackEntry.toRoute<VerReparaciones>()
            VerReparaciones(navController, args.estado)
        }

        composable<VerReparacionesDeVehiculo> { backStackEntry ->
            val args = backStackEntry.toRoute<VerReparacionesDeVehiculo>()
            VerReparacionesDeVehiculo(args.idVehiculo, navController)
        }
        composable<VerTodasLasReparacionesScreen> {
            VerTodasLasReparacionesScreen(navController)
        }

        composable<VerHistoricoReparacionesScreen> {
            VerHistoricoReparacionesScreen(navController)
        }

        composable<VerClientes> {
            VerClientes(navController)
        }

        composable<VerClientesEnTaller> {
            VerClientesEnTaller(navController)
        }

        composable<VerVehiculosEnTaller> {
            VerVehiculosEnTaller(navController)
        }

        composable<AgregarCliente> {
            FormularioCliente(navController = navController, idCliente = null)
        }


        composable<EditarCliente> { backStackEntry ->
            val args = backStackEntry.toRoute<EditarCliente>()
            FormularioCliente(navController = navController, idCliente = args.idCliente)
        }


        composable<VerVehiculos> {
            VerVehiculos(navController)
        }

        composable<AgregarVehiculo> {
            FormularioVehiculo(navController)
        }

        composable<EliminarVehiculo> {
            val args = it.toRoute<EliminarVehiculo>()
            EliminarVehiculo(args.idVehiculo)
        }

        // EDITAR VEHÍCULO — SOLO PASAMOS EL ID
        composable<EditarVehiculo> { backStackEntry ->
            val args = backStackEntry.toRoute<EditarVehiculo>()
            FormularioVehiculo(
                navController = navController,
                vehiculoExistenteId = args.idVehiculo
            )
        }

        composable<AgregarReparacion> {
            FormularioReparacion(navController, idReparacion = null)
        }

        composable<EditarReparacion> { backStackEntry ->
            val args = backStackEntry.toRoute<EditarReparacion>()
            FormularioReparacion(navController, idReparacion = args.idReparacion)
        }

    }
}

