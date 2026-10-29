package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.pucpr.clubecentral.ui.viewmodel.AuthViewModel
import br.pucpr.clubecentral.ui.viewmodel.PerfilViewModel

private val FundoPerfil = Color(0xFFFAFAFA)
private val BordaPerfil = Color(0xFFD2D2D2)
private val TextoSecundario = Color(0xFF777777)

@Composable
fun PerfilScreen(
    authViewModel: AuthViewModel,
    onSair: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    var dialogoAtual by remember { mutableStateOf<String?>(null) }

    val usuario = authViewModel.usuarioAutenticado

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
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
            onAlterarFoto = {
                dialogoAtual = "foto"
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloSecao("CONFIGURAÇÕES")

            GrupoConfiguracoes {
                ItemConfiguracao(
                    titulo = "Editar dados pessoais",
                    onClick = { dialogoAtual = "dados" }
                )

                DivisorPerfil()

                ItemConfiguracao(
                    titulo = "Notificações",
                    onClick = { dialogoAtual = "notificacoes" }
                )

                DivisorPerfil()

                ItemConfiguracao(
                    titulo = "Privacidade e segurança",
                    onClick = { dialogoAtual = "privacidade" }
                )

                DivisorPerfil()

                ItemConfiguracao(
                    titulo = "Clubes e permissões",
                    onClick = { dialogoAtual = "clubes" }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloSecao("PREFERÊNCIAS")

            GrupoConfiguracoes {
                ItemConfiguracao(
                    titulo = "Aparência",
                    valor = viewModel.aparencia,
                    onClick = { dialogoAtual = "aparencia" }
                )

                DivisorPerfil()

                ItemConfiguracao(
                    titulo = "Idioma",
                    valor = viewModel.idioma,
                    onClick = { dialogoAtual = "idioma" }
                )
            }
        }

        OutlinedButton(
            onClick = { dialogoAtual = "sair" },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sair da conta")
        }

        Spacer(modifier = Modifier.height(4.dp))
    }

    when (dialogoAtual) {
        "dados" -> {
            EditarDadosDialog(
                nomeAtual = usuario?.nome ?: "",
                emailAtual = usuario?.email ?: "",
                mensagemErro = authViewModel.mensagem,
                onSalvar = { nome, email ->
                    authViewModel.atualizarDadosPessoais(
                        nome = nome,
                        email = email
                    ) { sucesso ->
                        if (sucesso) {
                            dialogoAtual = null
                        }
                    }
                },
                onFechar = {
                    authViewModel.limparMensagem()
                    dialogoAtual = null
                }
            )
        }


        "aparencia" -> {
            SelecaoDialog(
                titulo = "Aparência",
                opcoes = listOf("Sistema", "Claro", "Escuro"),
                selecionado = viewModel.aparencia,
                onSelecionar = {
                    viewModel.alterarAparencia(it)
                    dialogoAtual = null
                },
                onFechar = { dialogoAtual = null }
            )
        }

        "idioma" -> {
            SelecaoDialog(
                titulo = "Idioma",
                opcoes = listOf("Português", "English", "Español"),
                selecionado = viewModel.idioma,
                onSelecionar = {
                    viewModel.alterarIdioma(it)
                    dialogoAtual = null
                },
                onFechar = { dialogoAtual = null }
            )
        }

        "notificacoes" -> {
            AlertDialog(
                onDismissRequest = { dialogoAtual = null },
                title = { Text("Notificações") },
                text = {
                    Text(
                        if (viewModel.notificacoesAtivas) {
                            "As notificações estão habilitadas nas preferências."
                        } else {
                            "As notificações estão desabilitadas nas preferências."
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.alterarNotificacoes(
                                !viewModel.notificacoesAtivas
                            )
                            dialogoAtual = null
                        }
                    ) {
                        Text(
                            if (viewModel.notificacoesAtivas) {
                                "Desativar"
                            } else {
                                "Ativar"
                            }
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { dialogoAtual = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        "privacidade" -> {
            MensagemDialog(
                titulo = "Privacidade e segurança",
                mensagem = "Esta área poderá reunir as opções de proteção e privacidade da sua conta.",
                onFechar = { dialogoAtual = null }
            )
        }

        "clubes" -> {
            MensagemDialog(
                titulo = "Clubes e permissões",
                mensagem = "Aqui poderão ser exibidos os clubes associados ao usuário e suas permissões.",
                onFechar = { dialogoAtual = null }
            )
        }

        "foto" -> {
            MensagemDialog(
                titulo = "Foto de perfil",
                mensagem = "A seleção de uma imagem será implementada em uma próxima etapa.",
                onFechar = { dialogoAtual = null }
            )
        }

        "sair" -> {
            AlertDialog(
                onDismissRequest = { dialogoAtual = null },
                title = { Text("Sair da conta") },
                text = {
                    Text("Deseja realmente encerrar sua sessão?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dialogoAtual = null
                            onSair()
                        }
                    ) {
                        Text("Sair")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { dialogoAtual = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
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
                .background(
                    color = Color(0xFFE8E8E8),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFAAAAAA),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(34.dp),
                tint = Color.Black
            )
        }

        Text(
            text = nome,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = email,
            fontSize = 14.sp,
            color = TextoSecundario
        )

        TextButton(onClick = onAlterarFoto) {
            Text(
                text = "Alterar foto"
            )
        }
    }
}

@Composable
private fun SelecaoDialog(
    titulo: String,
    opcoes: List<String>,
    selecionado: String,
    onSelecionar: (String) -> Unit,
    onFechar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onFechar,
        title = { Text(titulo) },
        text = {
            Column {
                opcoes.forEach { opcao ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelecionar(opcao) }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selecionado == opcao,
                            onClick = { onSelecionar(opcao) }
                        )

                        Text(
                            text = opcao,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onFechar) {
                Text("Fechar")
            }
        }
    )
}

@Composable
private fun EditarDadosDialog(
    nomeAtual: String,
    emailAtual: String,
    mensagemErro: String?,
    onSalvar: (String, String) -> Unit,
    onFechar: () -> Unit
) {
    var nome by remember(nomeAtual) {
        mutableStateOf(nomeAtual)
    }

    var email by remember(emailAtual) {
        mutableStateOf(emailAtual)
    }

    AlertDialog(
        onDismissRequest = onFechar,
        title = { Text("Editar dados pessoais") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    singleLine = true
                )

                if (mensagemErro != null) {
                    Text(
                        text = mensagemErro,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = nome.isNotBlank() &&
                        android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email.trim()).matches(),
                onClick = { onSalvar(nome.trim(), email.trim()) }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onFechar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun MensagemDialog(
    titulo: String,
    mensagem: String,
    onFechar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onFechar,
        title = { Text(titulo) },
        text = { Text(mensagem) },
        confirmButton = {
            TextButton(onClick = onFechar) {
                Text("Fechar")
            }
        }
    )
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

@Composable
private fun GrupoConfiguracoes(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.8.dp, BordaPerfil)
    ) {
        content()
    }
}

@Composable
private fun DivisorPerfil() {
    HorizontalDivider(
        thickness = 0.7.dp,
        color = BordaPerfil
    )
}

@Composable
private fun ItemConfiguracao(
    titulo: String,
    valor: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 51.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )

        if (valor != null) {
            Text(
                text = valor,
                fontSize = 11.sp,
                color = TextoSecundario
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(19.dp),
            tint = Color(0xFF999999)
        )
    }
}