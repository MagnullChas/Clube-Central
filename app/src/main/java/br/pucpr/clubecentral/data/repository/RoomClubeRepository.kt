package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.data.local.ClubeDao
import br.pucpr.clubecentral.model.Clube
import javax.inject.Inject

class RoomClubeRepository @Inject constructor(
    private val clubeDao: ClubeDao
) : ClubeRepository {

    override suspend fun listar(): List<Clube> {
        return clubeDao.listar()
    }

    override suspend fun inserir(clube: Clube) {
        clubeDao.inserir(clube)
    }

    override suspend fun atualizar(clube: Clube) {
        clubeDao.atualizar(clube)
    }

    override suspend fun excluir(clube: Clube) {
        clubeDao.excluir(clube)
    }
}