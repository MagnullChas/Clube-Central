package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.model.PerfilPreferencias

interface PerfilRepository {

    suspend fun buscar(): PerfilPreferencias?

    suspend fun salvar(preferencias: PerfilPreferencias)
}
