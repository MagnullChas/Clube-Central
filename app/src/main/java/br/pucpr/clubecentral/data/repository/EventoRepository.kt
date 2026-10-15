package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.model.Evento

interface EventoRepository {

    suspend fun listar(): List<Evento>

    suspend fun inserir(evento: Evento)

    suspend fun atualizar(evento: Evento)

    suspend fun excluir(evento: Evento)
}