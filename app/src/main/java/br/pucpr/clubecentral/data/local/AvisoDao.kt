package br.pucpr.clubecentral.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.pucpr.clubecentral.model.Aviso

@Dao
interface AvisoDao {

    @Insert
    suspend fun inserir(aviso: Aviso)

    @Query("SELECT * FROM avisos WHERE clubeId = :clubeId ORDER BY id DESC")
    suspend fun listarPorClube(clubeId: Int): List<Aviso>

    @Update
    suspend fun atualizar(aviso: Aviso)

    @Delete
    suspend fun excluir(aviso: Aviso)
}
