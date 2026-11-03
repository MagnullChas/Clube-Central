package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.data.local.PerfilDao
import br.pucpr.clubecentral.model.PerfilPreferencias
import javax.inject.Inject

class RoomPerfilRepository @Inject constructor(
    private val perfilDao: PerfilDao
) : PerfilRepository {

    override suspend fun buscar(): PerfilPreferencias? {
        return perfilDao.buscar()
    }

    override suspend fun salvar(preferencias: PerfilPreferencias) {
        perfilDao.salvar(preferencias)
    }
}
