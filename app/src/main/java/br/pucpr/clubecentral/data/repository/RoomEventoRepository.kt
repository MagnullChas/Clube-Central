package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.data.local.EventoDao
import br.pucpr.clubecentral.model.Evento
import javax.inject.Inject

class RoomEventoRepository @Inject constructor(
    private val eventoDao: EventoDao
) : EventoRepository {

    override suspend fun listar(): List<Evento> {
        return eventoDao.listar()
    }

    override suspend fun inserir(evento: Evento) {
        eventoDao.inserir(evento)
    }

    override suspend fun atualizar(evento: Evento) {
        eventoDao.atualizar(evento)
    }

    override suspend fun excluir(evento: Evento) {
        eventoDao.excluir(evento)
    }
}