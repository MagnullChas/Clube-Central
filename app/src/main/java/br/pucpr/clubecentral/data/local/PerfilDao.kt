package br.pucpr.clubecentral.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.pucpr.clubecentral.model.PerfilPreferencias

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil_preferencias WHERE id = 1")
    suspend fun buscar(): PerfilPreferencias?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(preferencias: PerfilPreferencias)
}
