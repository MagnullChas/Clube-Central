package br.pucpr.clubecentral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "avisos")
data class Aviso(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clubeId: Int,
    val quando: String,
    val texto: String
)
