package br.pucpr.clubecentral.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.clubecentral.data.local.AvisoDao
import br.pucpr.clubecentral.data.local.ClubeDao
import br.pucpr.clubecentral.data.repository.EventoRepository
import br.pucpr.clubecentral.model.Aviso
import br.pucpr.clubecentral.model.Clube
import br.pucpr.clubecentral.model.Evento
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubeViewModel @Inject constructor(
    private val clubeDao: ClubeDao,
    private val avisoDao: AvisoDao,
    private val eventoRepository: EventoRepository
) : ViewModel() {

    var clube by mutableStateOf<Clube?>(null)
        private set

    var eventos by mutableStateOf(emptyList<Evento>())
        private set

    var avisos by mutableStateOf(emptyList<Aviso>())
        private set

    private var clubeId = 0

    fun carregar(id: Int) {
        clubeId = id
        viewModelScope.launch {
            clube = clubeDao.buscarPorId(id)
            eventos = eventoRepository.listarPorClube(id)
            avisos = avisoDao.listarPorClube(id)
        }
    }

    fun salvarInformacoes(encontros: String, local: String, responsavel: String) {
        val atual = clube ?: return
        viewModelScope.launch {
            clubeDao.atualizar(
                atual.copy(
                    encontros = encontros.trim(),
                    local = local.trim(),
                    responsavel = responsavel.trim()
                )
            )
            carregar(clubeId)
        }
    }

    fun salvarAviso(aviso: Aviso?, quando: String, texto: String) {
        if (texto.isBlank()) return
        viewModelScope.launch {
            if (aviso == null) {
                avisoDao.inserir(Aviso(clubeId = clubeId, quando = quando.trim(), texto = texto.trim()))
            } else {
                avisoDao.atualizar(aviso.copy(quando = quando.trim(), texto = texto.trim()))
            }
            carregar(clubeId)
        }
    }

    fun excluirAviso(aviso: Aviso) {
        viewModelScope.launch {
            avisoDao.excluir(aviso)
            carregar(clubeId)
        }
    }
}
