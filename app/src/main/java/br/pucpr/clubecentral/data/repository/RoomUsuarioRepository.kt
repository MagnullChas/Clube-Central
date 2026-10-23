package br.pucpr.clubecentral.data.repository

import androidx.room.withTransaction
import br.pucpr.clubecentral.data.local.AppDatabase
import br.pucpr.clubecentral.data.local.UsuarioDao
import br.pucpr.clubecentral.data.security.SenhaUtils
import br.pucpr.clubecentral.model.Usuario
import javax.inject.Inject

class RoomUsuarioRepository @Inject constructor(
    private val usuarioDao: UsuarioDao,
    private val database: AppDatabase
) : UsuarioRepository {

    override suspend fun garantirAdministrador() {
        val emailAdmin = "admin@gmail.com"

        // Não substitui uma conta que já exista.
        if (usuarioDao.buscarPorEmail(emailAdmin) != null) {
            return
        }

        val salt = SenhaUtils.gerarSalt()

        val administrador = Usuario(
            nome = "Administrador",
            email = emailAdmin,
            senhaHash = SenhaUtils.gerarHash("admin", salt),
            salt = salt,
            tipo = "ADMIN",
            sessaoAtiva = false
        )

        try {
            usuarioDao.inserir(administrador)
        } catch (_: android.database.sqlite.SQLiteConstraintException) {
            // Outra operação pode ter criado a conta simultaneamente.
        }
    }

    override suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String
    ): Result<Usuario> = runCatching {

        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        require(nomeLimpo.isNotBlank()) {
            "Informe seu nome."
        }

        require(emailLimpo.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS
            .matcher(emailLimpo).matches()) {
            "Informe um e-mail válido."
        }

        require(senha.length >= 4) {
            "A senha deve ter pelo menos 4 caracteres."
        }

        require(usuarioDao.buscarPorEmail(emailLimpo) == null) {
            "Este e-mail já está cadastrado."
        }

        val salt = SenhaUtils.gerarSalt()

        val usuario = Usuario(
            nome = nomeLimpo,
            email = emailLimpo,
            senhaHash = SenhaUtils.gerarHash(senha, salt),
            salt = salt,
            tipo = "USER"
        )

        usuarioDao.inserir(usuario)
        usuarioDao.buscarPorEmail(emailLimpo)
            ?: error("Não foi possível recuperar o usuário cadastrado.")
    }

    override suspend fun autenticar(
        email: String,
        senha: String
    ): Result<Usuario> = runCatching {

        garantirAdministrador()

        val emailLimpo = email.trim().lowercase()

        val usuario = usuarioDao.buscarPorEmail(emailLimpo)
            ?: error("E-mail ou senha incorretos.")

        require(
            SenhaUtils.verificar(
                senha,
                usuario.salt,
                usuario.senhaHash
            )
        ) {
            "E-mail ou senha incorretos."
        }

        database.withTransaction {
            usuarioDao.encerrarTodasSessoes()
            usuarioDao.ativarSessao(usuario.id)
        }

        usuario.copy(sessaoAtiva = true)
    }

    override suspend fun buscarSessaoAtiva(): Usuario? {
        return usuarioDao.buscarSessaoAtiva()
    }

    override suspend fun encerrarSessao() {
        database.withTransaction {
            usuarioDao.encerrarTodasSessoes()
        }
    }

    override suspend fun atualizarDadosPessoais(
        usuarioId: Int,
        nome: String,
        email: String
    ): Result<Usuario> = runCatching {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim().lowercase()

        require(nomeLimpo.isNotBlank()) {
            "Informe seu nome."
        }

        require(
            android.util.Patterns.EMAIL_ADDRESS
                .matcher(emailLimpo)
                .matches()
        ) {
            "Informe um e-mail válido."
        }

        database.withTransaction {
            val usuarioAtual = usuarioDao.buscarSessaoAtiva()
                ?: error("Nenhum usuário está autenticado.")

            require(usuarioAtual.id == usuarioId) {
                "Não é possível editar outra conta."
            }

            val usuarioComEmail =
                usuarioDao.buscarPorEmail(emailLimpo)

            require(
                usuarioComEmail == null ||
                        usuarioComEmail.id == usuarioId
            ) {
                "Este e-mail já está cadastrado."
            }

            usuarioDao.atualizarDadosPessoais(
                usuarioId = usuarioId,
                nome = nomeLimpo,
                email = emailLimpo
            )

            usuarioDao.buscarPorEmail(emailLimpo)
                ?: error("Não foi possível atualizar os dados.")
        }
    }
}
