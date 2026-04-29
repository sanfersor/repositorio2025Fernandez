package com.example.mecanicsync.dashboard.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mecanicsync.clientes.ui.viewmodel.ClienteViewModel
import com.example.mecanicsync.dashboard.data.DashboardRepository
import com.example.mecanicsync.dashboard.data.network.DashboardResponse
import com.example.mecanicsync.vehiculos.ui.viewmodel.VehiculoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,

) : ViewModel() {

    var uiState by mutableStateOf<DashboardResponse?>(null)
    var isLoading by mutableStateOf(false)


    init {
        loadDashboard()
        viewModelScope.launch {

        }
    }

    fun loadDashboard() {
        viewModelScope.launch {
            isLoading = true
            try {
                uiState = dashboardRepository.getDashboard()
            } catch (e: Exception) {
                uiState = null
            } finally {
                isLoading = false
            }
        }
    }
}