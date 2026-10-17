
package br.pucpr.clubecentral.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.clubecentral.data.repository.PerfilRepository
import br.pucpr.clubecentral.model.PerfilPreferencias
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repository: PerfilRepository
) : ViewModel() {

    var nome by mutableStateOf("Alex Martins")
        private set

    var email by mutableStateOf("alex.martins@universidade.edu")
        private set

    var aparencia by mutableStateOf("Sistema")
        private set

    var idioma by mutableStateOf("Português")
        private set

    var notificacoesAtivas by mutableStateOf(true)
        private set

    var carregando by mutableStateOf(true)
        private set

    init {
        carregar()
    }

    private fun carregar() {
        viewModelScope.launch {
            try {
                val preferencias = repository.buscar()
                    ?: PerfilPreferencias().also {
                        repository.salvar(it)
                    }

                aparencia = preferencias.aparencia
                idioma = preferencias.idioma
                notificacoesAtivas = preferencias.notificacoesAtivas

                if (repository.buscar() == null) {
                    repository.salvar(preferencias)
                }
            } finally {
                carregando = false
            }
        }
    }

    private fun salvarPreferencias() {
        val preferencias = PerfilPreferencias(
            aparencia = aparencia,
            idioma = idioma,
            notificacoesAtivas = notificacoesAtivas
        )

        viewModelScope.launch {
            repository.salvar(preferencias)
        }
    }

    fun alterarAparencia(novaAparencia: String) {
        if (novaAparencia !in listOf("Sistema", "Claro", "Escuro")) {
            return
        }

        aparencia = novaAparencia
        salvarPreferencias()
    }

    fun alterarIdioma(novoIdioma: String) {
        if (novoIdioma !in listOf("Português", "English", "Español")) {
            return
        }

        idioma = novoIdioma
        salvarPreferencias()
    }

    fun alterarNotificacoes(ativas: Boolean) {
        notificacoesAtivas = ativas
        salvarPreferencias()
    }

    fun alterarDadosPessoais(novoNome: String, novoEmail: String) {
        if (novoNome.isBlank() || novoEmail.isBlank()) {
            return
        }

        nome = novoNome.trim()
        email = novoEmail.trim()
    }
}
