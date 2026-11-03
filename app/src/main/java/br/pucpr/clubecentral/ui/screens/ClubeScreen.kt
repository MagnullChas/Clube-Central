package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.pucpr.clubecentral.model.Aviso
import br.pucpr.clubecentral.model.Clube
import br.pucpr.clubecentral.model.Evento
import br.pucpr.clubecentral.ui.viewmodel.ClubeViewModel
import java.util.Calendar

private val MESES = listOf(
    "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO",
    "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"
)

private val DIAS_SEMANA = listOf("D", "S", "T", "Q", "Q", "S", "S")

private class CelulaDia(
    val dia: Int,
    val mes: Int,
    val ano: Int,
    val doMesAtual: Boolean
)

private fun formatarData(dia: Int, mes: Int, ano: Int): String =
    "%02d/%02d/%04d".format(dia, mes + 1, ano)

// Aceita "9/10/2025" ou "09/10/2025" e devolve sempre "09/10/2025".
private fun normalizarData(texto: String): String? {
    val partes = texto.trim().split("/")
    if (partes.size != 3) return null
    val dia = partes[0].toIntOrNull() ?: return null
    val mes = partes[1].toIntOrNull() ?: return null
    val ano = partes[2].toIntOrNull() ?: return null
    return formatarData(dia, mes - 1, ano)
}

private fun gerarCelulas(ano: Int, mes: Int): List<CelulaDia> {
    val primeiro = Calendar.getInstance().apply {
        clear()
        set(ano, mes, 1)
    }
    val deslocamento = primeiro.get(Calendar.DAY_OF_WEEK) - 1
    val diasNoMes = primeiro.getActualMaximum(Calendar.DAY_OF_MONTH)

    val anterior = (primeiro.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
    val proximo = (primeiro.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
    val diasNoAnterior = anterior.getActualMaximum(Calendar.DAY_OF_MONTH)

    val total = ((deslocamento + diasNoMes + 6) / 7) * 7
    val celulas = mutableListOf<CelulaDia>()

    for (i in 0 until total) {
        val dia = i - deslocamento + 1
        celulas += when {
            dia < 1 -> CelulaDia(
                diasNoAnterior + dia,
                anterior.get(Calendar.MONTH),
                anterior.get(Calendar.YEAR),
                false
            )
            dia > diasNoMes -> CelulaDia(
                dia - diasNoMes,
                proximo.get(Calendar.MONTH),
                proximo.get(Calendar.YEAR),
                false
            )
            else -> CelulaDia(dia, mes, ano, true)
        }
    }
    return celulas
}

@Composable
fun ClubeScreen(
    viewModel: ClubeViewModel,
    ehAdmin: Boolean,
    onDiaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clube = viewModel.clube
    val eventos = viewModel.eventos

    var dialogoAviso by remember { mutableStateOf(false) }
    var avisoEmEdicao by remember { mutableStateOf<Aviso?>(null) }
    var dialogoInfo by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "VISÃO DO CLUBE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = clube?.nome ?: "",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Calendario(
                eventos = eventos,
                ehAdmin = ehAdmin,
                onDiaClick = onDiaClick
            )

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TituloSecao("AVISOS E ATUALIZAÇÕES")
                if (ehAdmin) {
                    TextButton(onClick = { avisoEmEdicao = Aviso(clubeId = 0, quando = "", texto = ""); dialogoAviso = true }) {
                        Text("Adicionar")
                    }
                }
            }
            if (viewModel.avisos.isEmpty()) {
                Text(
                    text = "Nenhum aviso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                CaixaBorda {
                    viewModel.avisos.forEachIndexed { indice, aviso ->
                        if (indice > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Row(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = aviso.quando,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(56.dp)
                            )
                            Text(
                                text = aviso.texto,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            if (ehAdmin) {
                                IconButton(onClick = { avisoEmEdicao = aviso; dialogoAviso = true }) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "Editar aviso")
                                }
                                IconButton(onClick = { viewModel.excluirAviso(aviso) }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = "Excluir aviso")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            TituloSecao("EVENTOS MARCADOS")
            if (eventos.isEmpty()) {
                Text(
                    text = "Nenhum evento marcado.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                CaixaBorda {
                    eventos.forEachIndexed { indice, evento ->
                        if (indice > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        ItemEvento(evento)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TituloSecao("INFORMAÇÕES DO CLUBE")
                if (ehAdmin) {
                    TextButton(onClick = { dialogoInfo = true }) { Text("Editar") }
                }
            }
            ItemInformacao(Icons.Outlined.CalendarMonth, "ENCONTROS SEMANAIS", clube?.encontros.orEmpty())
            ItemInformacao(Icons.Outlined.LocationOn, "LOCAL", clube?.local.orEmpty())
            ItemInformacao(Icons.Outlined.Person, "RESPONSÁVEL", clube?.responsavel.orEmpty())
        }
    }

    if (dialogoAviso) {
        DialogoAviso(
            aviso = avisoEmEdicao,
            onSalvar = { quando, texto ->
                viewModel.salvarAviso(avisoEmEdicao?.takeIf { it.id != 0 }, quando, texto)
                dialogoAviso = false
            },
            onCancelar = { dialogoAviso = false }
        )
    }

    if (dialogoInfo && clube != null) {
        DialogoInformacoes(
            clube = clube,
            onSalvar = { encontros, local, responsavel ->
                viewModel.salvarInformacoes(encontros, local, responsavel)
                dialogoInfo = false
            },
            onCancelar = { dialogoInfo = false }
        )
    }
}

@Composable
private fun DialogoAviso(
    aviso: Aviso?,
    onSalvar: (String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var quando by remember { mutableStateOf(aviso?.quando.orEmpty()) }
    var texto by remember { mutableStateOf(aviso?.texto.orEmpty()) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (aviso == null || aviso.id == 0) "Novo aviso" else "Editar aviso") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = quando,
                    onValueChange = { quando = it },
                    label = { Text("Quando (ex.: HOJE, 12 OUT)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    label = { Text("Aviso") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSalvar(quando, texto) }, enabled = texto.isNotBlank()) {
                Text("Salvar")
            }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun DialogoInformacoes(
    clube: Clube,
    onSalvar: (String, String, String) -> Unit,
    onCancelar: () -> Unit
) {
    var encontros by remember { mutableStateOf(clube.encontros) }
    var local by remember { mutableStateOf(clube.local) }
    var responsavel by remember { mutableStateOf(clube.responsavel) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Editar informações") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = encontros,
                    onValueChange = { encontros = it },
                    label = { Text("Encontros semanais") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = local,
                    onValueChange = { local = it },
                    label = { Text("Local") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = responsavel,
                    onValueChange = { responsavel = it },
                    label = { Text("Responsável") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSalvar(encontros, local, responsavel) }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun Calendario(
    eventos: List<Evento>,
    ehAdmin: Boolean,
    onDiaClick: (String) -> Unit
) {
    val hoje = remember { Calendar.getInstance() }
    var mes by rememberSaveable { mutableIntStateOf(hoje.get(Calendar.MONTH)) }
    var ano by rememberSaveable { mutableIntStateOf(hoje.get(Calendar.YEAR)) }

    val datasComEvento = remember(eventos) {
        eventos.mapNotNull { normalizarData(it.data) }.toSet()
    }
    val celulas = remember(mes, ano) { gerarCelulas(ano, mes) }

    fun mudarMes(delta: Int) {
        val novo = Calendar.getInstance().apply {
            clear()
            set(ano, mes, 1)
            add(Calendar.MONTH, delta)
        }
        mes = novo.get(Calendar.MONTH)
        ano = novo.get(Calendar.YEAR)
    }

    CaixaBorda {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { mudarMes(-1) }) {
                    Icon(Icons.Outlined.ChevronLeft, contentDescription = "Mês anterior")
                }
                Text(
                    text = "${MESES[mes]} $ano",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { mudarMes(1) }) {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = "Próximo mês")
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                DIAS_SEMANA.forEach { letra ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letra,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            celulas.chunked(7).forEach { semana ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    semana.forEach { celula ->
                        val data = formatarData(celula.dia, celula.mes, celula.ano)
                        CelulaCalendario(
                            celula = celula,
                            temEvento = data in datasComEvento,
                            clicavel = ehAdmin,
                            onClick = { onDiaClick(data) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = "  Evento",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (ehAdmin) {
                    Text(
                        text = "   ·   Toque em um dia para marcar um evento",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun CelulaCalendario(
    celula: CelulaDia,
    temEvento: Boolean,
    clicavel: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val corTexto = when {
        temEvento -> MaterialTheme.colorScheme.surface
        celula.doMesAtual -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                if (temEvento) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.surface
            )
            .then(if (clicavel) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = celula.dia.toString(),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (temEvento) FontWeight.Bold else FontWeight.Normal,
            color = corTexto
        )
    }
}

@Composable
private fun ItemEvento(evento: Evento) {
    Row(modifier = Modifier.padding(16.dp)) {
        Text(
            text = evento.data,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(72.dp)
        )
        Column {
            Text(
                text = evento.nome,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            if (evento.descricao.isNotBlank()) {
                Text(
                    text = evento.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ItemInformacao(icone: ImageVector, rotulo: String, valor: String) {
    CaixaBorda {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(icone, contentDescription = null, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = rotulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = valor.ifBlank { "Não informado" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TituloSecao(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun CaixaBorda(conteudo: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant))
    ) {
        conteudo()
    }
}
