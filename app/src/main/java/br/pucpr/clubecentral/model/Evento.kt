package br.pucpr.clubecentral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos")
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val data: String,
    val descricao: String
)