package br.pucpr.clubecentral.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.pucpr.clubecentral.model.Clube

@Dao
interface ClubeDao {

    @Insert
    suspend fun inserir(clube: Clube)

    @Query("SELECT * FROM clubes ORDER BY nome")
    suspend fun listar(): List<Clube>

    @Query("SELECT * FROM clubes WHERE id = :id")
    suspend fun buscarPorId(id: Int): Clube?

    @Update
    suspend fun atualizar(clube: Clube)

    @Delete
    suspend fun excluir(clube: Clube)
}