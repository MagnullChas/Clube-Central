package br.pucpr.clubecentral.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.pucpr.clubecentral.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE sessaoAtiva = 1 LIMIT 1")
    suspend fun buscarSessaoAtiva(): Usuario?

    @Query("UPDATE usuarios SET sessaoAtiva = 0")
    suspend fun encerrarTodasSessoes()

    @Query("UPDATE usuarios SET sessaoAtiva = 1 WHERE id = :usuarioId")
    suspend fun ativarSessao(usuarioId: Int)

    @Query("UPDATE usuarios SET sessaoAtiva = 0 WHERE id = :usuarioId")
    suspend fun encerrarSessao(usuarioId: Int)

    @Query("UPDATE usuarios SET nome = :nome, email = :email WHERE id = :usuarioId")
    suspend fun atualizarDadosPessoais(
        usuarioId: Int,
        nome: String,
        email: String
    ): Int
}
