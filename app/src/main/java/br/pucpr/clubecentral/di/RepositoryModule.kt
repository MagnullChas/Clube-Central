package br.pucpr.clubecentral.di

import br.pucpr.clubecentral.data.repository.RoomEventoRepository
import br.pucpr.clubecentral.data.repository.EventoRepository
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
}