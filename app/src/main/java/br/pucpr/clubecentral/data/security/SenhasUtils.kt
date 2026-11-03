
package br.pucpr.clubecentral.data.security

import java.security.MessageDigest
import java.security.SecureRandom

object SenhaUtils {

    fun gerarSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

    fun gerarHash(senha: String, salt: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest("$salt:$senha".toByteArray(Charsets.UTF_8))

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }

    fun verificar(senha: String, salt: String, hashSalvo: String): Boolean {
        val hashCalculado = gerarHash(senha, salt)

        return MessageDigest.isEqual(
            hashCalculado.toByteArray(Charsets.UTF_8),
            hashSalvo.toByteArray(Charsets.UTF_8)
        )
    }
}
