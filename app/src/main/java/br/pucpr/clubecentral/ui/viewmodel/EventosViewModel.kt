package br.pucpr.clubecentral.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.clubecentral.data.local.AppDatabase
import br.pucpr.clubecentral.data.repository.EventoRepository
import br.pucpr.clubecentral.model.Evento
import kotlinx.coroutines.launch

class EventosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EventoRepository

    var nome by mutableStateOf("")
        private set

    var data by mutableStateOf("")
        private set

    var descricao by mutableStateOf("")
        private set

    var eventos by mutableStateOf(emptyList<Evento>())
        private set

    var eventoEditando by mutableStateOf<Evento?>(null)
        private set

    init {
        val dao = AppDatabase
            .getDatabase(application)
            .eventoDao()

        repository = EventoRepository(dao)
        carregar()
    }

    fun alterarNome(novoNome: String) {
        nome = novoNome
    }

    fun alterarData(novaData: String) {
        data = novaData
    }

    fun alterarDescricao(novaDescricao: String) {
        descricao = novaDescricao
    }

    fun adicionar() {
        val novoNome = nome.trim()
        val novaData = data.trim()
        val novaDescricao = descricao.trim()

        if (novoNome.isBlank() || novaData.isBlank()) {
            return
        }

        viewModelScope.launch {

            val novoEvento = Evento(
                nome = novoNome,
                data = novaData,
                descricao = novaDescricao
            )

            repository.inserir(novoEvento)

            limparCampos()
            carregar()
        }
    }

    fun iniciarEdicao(evento: Evento) {
        eventoEditando = evento

        nome = evento.nome
        data = evento.data
        descricao = evento.descricao
    }

    fun salvarEdicao() {
        val evento = eventoEditando ?: return

        val novoNome = nome.trim()
        val novaData = data.trim()
        val novaDescricao = descricao.trim()

        if (novoNome.isBlank() || novaData.isBlank()) {
            return
        }

        viewModelScope.launch {

            val eventoAtualizado = evento.copy(
                nome = novoNome,
                data = novaData,
                descricao = novaDescricao
            )

            repository.atualizar(eventoAtualizado)

            limparCampos()
            carregar()
        }
    }

    fun cancelarEdicao() {
        eventoEditando = null
        limparCampos()
    }

    fun excluir(evento: Evento) {
        viewModelScope.launch {
            repository.excluir(evento)
            carregar()
        }
    }

    private fun limparCampos() {
        nome = ""
        data = ""
        descricao = ""
        eventoEditando = null
    }

    private fun carregar() {
        viewModelScope.launch {
            eventos = repository.listar()
        }
    }
}