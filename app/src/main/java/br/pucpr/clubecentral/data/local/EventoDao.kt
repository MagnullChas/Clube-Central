package br.pucpr.clubecentral.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.pucpr.clubecentral.model.Evento

@Dao
interface EventoDao {

    @Insert
    suspend fun inserir(evento: Evento)

    @Query("SELECT * FROM eventos ORDER BY nome")
    suspend fun listar(): List<Evento>

    @Update
    suspend fun atualizar(evento: Evento)

    @Delete
    suspend fun excluir(evento: Evento)

}