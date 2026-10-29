package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.pucpr.clubecentral.ui.viewmodel.AuthViewModel

private val BordaPerfil = Color(0xFFD2D2D2)
private val TextoSecundario = Color(0xFF777777)

@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val usuario = authViewModel.usuarioAutenticado

    var mostrarFoto by remember { mutableStateOf(false) }

    // Campos de nome e e-mail (já começam preenchidos)
    var nome by remember(usuario?.nome) { mutableStateOf(usuario?.nome ?: "") }
    var email by remember(usuario?.email) { mutableStateOf(usuario?.email ?: "") }

    // Campos de senha
    var senhaAtual by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }

    // Controla de qual seção vem a mensagem de erro/sucesso
    var ultimaAcao by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf<String?>(null) }

    val emailValido = android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val dadosAlterados = nome.trim() != (usuario?.nome ?: "") ||
            email.trim() != (usuario?.email ?: "")
    val confirmacaoDiferente = confirmar.isNotEmpty() && confirmar != novaSenha

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = "CONTA PESSOAL",
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                color = TextoSecundario,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Seu perfil",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        CartaoPerfil(
            nome = usuario?.nome ?: "Usuário",
            email = usuario?.email ?: "",
            onAlterarFoto = { mostrarFoto = true }
        )

        // ---------- Nome e e-mail ----------
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TituloSecao("DADOS PESSOAIS")

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Mensagem(
                erro = if (ultimaAcao == "dados") authViewModel.mensagem else null,
                sucesso = if (ultimaAcao == "dados") sucesso else null
            )

            Button(
                onClick = {
                    ultimaAcao = "dados"
                    sucesso = null
                    authViewModel.limparMensagem()
                    authViewModel.atualizarDadosPessoais(
                        nome = nome.trim(),
                        email = email.trim()
                    ) { ok ->
                        if (ok) sucesso = "Dados atualizados."
                    }
                },
                enabled = nome.isNotBlank() && emailValido && dadosAlterados,
                shape = RoundedCornerShape(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Salvar alterações")
            }
        }
    }

    if (mostrarFoto) {
        AlertDialog(
            onDismissRequest = { mostrarFoto = false },
            title = { Text("Foto de perfil") },
            text = { Text("A seleção de uma imagem será implementada em uma próxima etapa.") },
            confirmButton = {
                TextButton(onClick = { mostrarFoto = false }) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
private fun Mensagem(erro: String?, sucesso: String?) {
    if (erro != null) {
        Text(
            text = erro,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    } else if (sucesso != null) {
        Text(
            text = sucesso,
            color = Color(0xFF2E7D32),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun CartaoPerfil(
    nome: String,
    email: String,
    onAlterarFoto: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.8.dp, BordaPerfil)
            .padding(horizontal = 12.dp, vertical = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(color = Color(0xFFE8E8E8), shape = CircleShape)
                .border(width = 1.dp, color = Color(0xFFAAAAAA), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(34.dp),
                tint = Color.Black
            )
        }

        Text(text = nome, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Text(text = email, fontSize = 14.sp, color = TextoSecundario)

        TextButton(onClick = onAlterarFoto) {
            Text(text = "Alterar foto")
        }
    }
}

@Composable
private fun TituloSecao(titulo: String) {
    Text(
        text = titulo,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        fontWeight = FontWeight.SemiBold
    )
}