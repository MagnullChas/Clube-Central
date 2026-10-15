package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.pucpr.clubecentral.ui.viewmodel.EventosViewModel

@Composable
fun EventosScreen(
    modifier: Modifier = Modifier,
    viewModel: EventosViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = viewModel.nome,
            onValueChange = { viewModel.alterarNome(it) },
            label = { Text("Nome do evento") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.data,
            onValueChange = { viewModel.alterarData(it) },
            label = { Text("Data") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.descricao,
            onValueChange = { viewModel.alterarDescricao(it) },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Se estiver editando, mostra botão Cancelar
            if (viewModel.eventoEditando != null) {
                TextButton(
                    onClick = {
                        viewModel.cancelarEdicao()
                    }
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            Button(
                onClick = {
                    if (viewModel.eventoEditando == null) {
                        viewModel.adicionar()
                    } else {
                        viewModel.salvarEdicao()
                    }
                },
                enabled = viewModel.nome.isNotBlank() &&
                        viewModel.data.isNotBlank()
            ) {
                Text(
                    if (viewModel.eventoEditando == null)
                        "Adicionar"
                    else
                        "Salvar alterações"
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = viewModel.eventos,
                key = { evento -> evento.id }
            ) { evento ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {

                        Text(
                            text = evento.nome,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Data: ${evento.data}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = evento.descricao,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            TextButton(
                                onClick = {
                                    viewModel.iniciarEdicao(evento)
                                }
                            ) {
                                Text("Editar")
                            }

                            TextButton(
                                onClick = {
                                    viewModel.excluir(evento)
                                }
                            ) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }
    }
}