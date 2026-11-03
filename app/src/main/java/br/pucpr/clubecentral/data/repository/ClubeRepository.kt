package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.model.Clube

interface ClubeRepository {

    suspend fun listar(): List<Clube>

    suspend fun inserir(clube: Clube)

    suspend fun atualizar(clube: Clube)

    suspend fun excluir(clube: Clube)
}