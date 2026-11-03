package br.pucpr.clubecentral.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clubes")
data class Clube (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val membros: Int,
    val eventos: Int,
    val imagem: String,
    @ColumnInfo(defaultValue = "''")
    val encontros: String = "",
    @ColumnInfo(defaultValue = "''")
    val local: String = "",
    @ColumnInfo(defaultValue = "''")
    val responsavel: String = ""
)