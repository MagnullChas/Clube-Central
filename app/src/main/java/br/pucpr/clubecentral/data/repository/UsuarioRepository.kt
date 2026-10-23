
package br.pucpr.clubecentral.data.repository

import br.pucpr.clubecentral.model.Usuario

interface UsuarioRepository {

    suspend fun garantirAdministrador()

    suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String
    ): Result<Usuario>

    suspend fun autenticar(
        email: String,
        senha: String
    ): Result<Usuario>

    suspend fun buscarSessaoAtiva(): Usuario?

    suspend fun encerrarSessao()

    suspend fun atualizarDadosPessoais(
        usuarioId: Int,
        nome: String,
        email: String
    ): Result<Usuario>
}
