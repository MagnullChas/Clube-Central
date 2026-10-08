package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.data.local.EventoDao
import br.pucpr.clubecentral.model.Evento

class EventoRepository(
    private val eventoDao: EventoDao
) {

    suspend fun listar(): List<Evento> {
        return eventoDao.listar()
    }

    suspend fun inserir(evento: Evento) {
        eventoDao.inserir(evento)
    }

    suspend fun atualizar(evento: Evento) {
        eventoDao.atualizar(evento)
    }

    suspend fun excluir(evento: Evento) {
        eventoDao.excluir(evento)
    }
}