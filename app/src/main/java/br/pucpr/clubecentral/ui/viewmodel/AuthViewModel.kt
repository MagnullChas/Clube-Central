package br.pucpr.clubecentral.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.pucpr.clubecentral.data.repository.UsuarioRepository
import br.pucpr.clubecentral.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UsuarioRepository
) : ViewModel() {

    var carregando by mutableStateOf(false)
        private set

    var mensagem by mutableStateOf<String?>(null)
        private set

    var usuarioAutenticado by mutableStateOf<Usuario?>(null)
        private set

    init {
        viewModelScope.launch {
            carregando = true

            try {
                repository.garantirAdministrador()
                usuarioAutenticado = repository.buscarSessaoAtiva()
            } catch (_: Exception) {
                mensagem = "Não foi possível inicializar os usuários."
            } finally {
                carregando = false
            }
        }
    }

    fun entrar(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            mensagem = "Preencha o e-mail e a senha."
            return
        }

        viewModelScope.launch {
            carregando = true
            mensagem = null

            try {
                repository.autenticar(email, senha)
                    .onSuccess { usuario ->
                        usuarioAutenticado = usuario
                    }
                    .onFailure { erro ->
                        mensagem = erro.message
                            ?: "Não foi possível entrar."
                    }
            } finally {
                carregando = false
            }
        }
    }

    fun cadastrar(
        nome: String,
        email: String,
        senha: String,
        confirmarSenha: String
    ) {
        if (
            nome.isBlank() ||
            email.isBlank() ||
            senha.isBlank() ||
            confirmarSenha.isBlank()
        ) {
            mensagem = "Preencha todos os campos."
            return
        }

        if (senha != confirmarSenha) {
            mensagem = "As senhas não coincidem."
            return
        }

        viewModelScope.launch {
            carregando = true
            mensagem = null

            try {
                repository.cadastrar(nome, email, senha)
                    .onSuccess { usuario ->
                        // Entra automaticamente após o cadastro.
                        repository.autenticar(usuario.email, senha)
                            .onSuccess { autenticado ->
                                usuarioAutenticado = autenticado
                            }
                            .onFailure { erro ->
                                mensagem = erro.message
                                    ?: "Conta criada, mas não foi possível entrar."
                            }
                    }
                    .onFailure { erro ->
                        mensagem = erro.message
                            ?: "Não foi possível criar a conta."
                    }
            } finally {
                carregando = false
            }
        }
    }

    fun limparMensagem() {
        mensagem = null
    }

    fun sair() {
        viewModelScope.launch {
            carregando = true

            try {
                repository.encerrarSessao()
                usuarioAutenticado = null
                mensagem = null
            } catch (_: Exception) {
                mensagem = "Não foi possível encerrar a sessão."
            } finally {
                carregando = false
            }
        }
    }

    fun atualizarDadosPessoais(
        nome: String,
        email: String,
        onResultado: (Boolean) -> Unit
    ) {
        val usuarioAtual = usuarioAutenticado

        if (usuarioAtual == null) {
            mensagem = "Nenhum usuário está autenticado."
            onResultado(false)
            return
        }

        viewModelScope.launch {
            carregando = true
            mensagem = null

            try {
                repository.atualizarDadosPessoais(
                    usuarioId = usuarioAtual.id,
                    nome = nome,
                    email = email
                ).onSuccess { usuarioAtualizado ->
                    // Atualiza o usuário na memória do aplicativo.
                    usuarioAutenticado = usuarioAtualizado
                    onResultado(true)
                }.onFailure { erro ->
                    mensagem = erro.message
                        ?: "Não foi possível atualizar os dados."

                    onResultado(false)
                }
            } finally {
                carregando = false
            }
        }
    }
}
