package com.example.mecanicsync.core.di

import com.example.mecanicsync.clientes.data.ClienteRepository
import com.example.mecanicsync.clientes.data.network.ClienteClient
import com.example.mecanicsync.dashboard.data.DashboardRepository
import com.example.mecanicsync.dashboard.data.network.DashboardClient
import com.example.mecanicsync.utils.Constantes
import com.example.mecanicsync.login.data.network.LoginClient
import com.example.mecanicsync.reparaciones.data.ReparacionesRepository
import com.example.mecanicsync.reparaciones.data.network.ReparacionesCliente
import com.example.mecanicsync.tiporeparacion.data.TipoReparacionCliente
import com.example.mecanicsync.tiporeparacion.data.TipoReparacionRepository
import com.example.mecanicsync.usuario.data.network.UsuarioClient
import com.example.mecanicsync.vehiculos.data.VehiculoRepository
import com.example.mecanicsync.vehiculos.data.network.VehiculoCliente
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLoginClient(retrofit: Retrofit): LoginClient =
        retrofit.create(LoginClient::class.java)

    @Provides
    @Singleton
    fun provideDashboardClient(retrofit: Retrofit): DashboardClient {
        return retrofit.create(DashboardClient::class.java)
    }
    @Provides
    @Singleton
    fun provideDashboardRepository(
        dashboardClient: DashboardClient
    ): DashboardRepository = DashboardRepository(dashboardClient)

    @Provides
    @Singleton
    fun provideClienteClient(retrofit: Retrofit): ClienteClient {
        return retrofit.create(ClienteClient::class.java)
    }

    @Provides
    @Singleton
    fun provideClienteRepository(
        clienteClient: ClienteClient,

    ): ClienteRepository = ClienteRepository(
        clienteClient,

    )

    @Provides
    @Singleton
    fun provideReparacionesClient(retrofit: Retrofit): ReparacionesCliente {
        return retrofit.create(ReparacionesCliente::class.java)
    }
    @Provides
    @Singleton
    fun provideReparacionesRepository(
        reparacionesCliente: ReparacionesCliente
    ): ReparacionesRepository = ReparacionesRepository(reparacionesCliente)

    @Provides
    @Singleton
    fun provideVehiculoClient(retrofit: Retrofit): VehiculoCliente {
        return retrofit.create(VehiculoCliente::class.java)
    }
    @Provides
    @Singleton
    fun provideVehiculoRepository(
        vehiculoCliente: VehiculoCliente
    ): VehiculoRepository = VehiculoRepository(vehiculoCliente)

    @Provides
    @Singleton
    fun provideTipoReparacionCliente(retrofit: Retrofit): TipoReparacionCliente {
        return retrofit.create(TipoReparacionCliente::class.java)
    }

    @Provides
    @Singleton
    fun provideTipoReparacionRepository(
        tipoReparacionCliente: TipoReparacionCliente
    ): TipoReparacionRepository = TipoReparacionRepository(tipoReparacionCliente)

    @Provides
    @Singleton
    fun provideUsuarioClient(retrofit: Retrofit): UsuarioClient {
        return retrofit.create(UsuarioClient::class.java)
    }

}

