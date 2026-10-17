
package br.pucpr.clubecentral.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil_preferencias")
data class PerfilPreferencias(
    @PrimaryKey
    val id: Int = 1,

    val aparencia: String = "Sistema",

    val idioma: String = "Português",

    val notificacoesAtivas: Boolean = true
)
