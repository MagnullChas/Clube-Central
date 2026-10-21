package br.pucpr.clubecentral.di

import br.pucpr.clubecentral.data.repository.RoomEventoRepository
import br.pucpr.clubecentral.data.repository.EventoRepository
import br.pucpr.clubecentral.data.repository.PerfilRepository
import br.pucpr.clubecentral.data.repository.RoomPerfilRepository
import br.pucpr.clubecentral.data.repository.UsuarioRepository
import br.pucpr.clubecentral.data.repository.RoomUsuarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEventoRepository(
        repository: RoomEventoRepository
    ): EventoRepository {
        return repository
    }

    @Provides
    @Singleton
    fun providePerfilRepository(
        repository: RoomPerfilRepository
    ): PerfilRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideUsuarioRepository(
        repository: RoomUsuarioRepository
    ): UsuarioRepository {
        return repository
    }
}