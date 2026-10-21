
package br.pucpr.clubecentral.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,

    val email: String,

    val senhaHash: String,

    val salt: String,

    val tipo: String = "USER",

    val sessaoAtiva: Boolean = false
)
