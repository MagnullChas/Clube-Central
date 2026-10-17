package br.pucpr.clubecentral.di

import android.content.Context
import br.pucpr.clubecentral.data.local.AppDatabase
import br.pucpr.clubecentral.data.local.EventoDao
import br.pucpr.clubecentral.data.local.PerfilDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideEventoDao(
        database: AppDatabase
    ): EventoDao {
        return database.eventoDao()
    }

    @Provides
    fun providePerfilDao(
        database: AppDatabase
    ): PerfilDao {
        return database.perfilDao()
    }
}