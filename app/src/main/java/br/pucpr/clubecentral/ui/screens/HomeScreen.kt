package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.pucpr.clubecentral.model.Clube
import br.pucpr.clubecentral.ui.t

@Composable
fun ClubeCard( clube: Clube, onDetalhesClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imagemId = remember(clube.imagem) {
        context.resources.getIdentifier(clube.imagem, "drawable", context.packageName)
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetalhesClick() },
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (imagemId != 0) {
                Image(
                    painter = painterResource(id = imagemId),
                    contentDescription = clube.nome,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(t("area_imagem"), style = MaterialTheme.typography.labelSmall)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = clube.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${clube.membros} ${t("clube_membros")} · ${clube.eventos} ${t("clube_eventos")}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                OutlinedButton(
                    onClick = onDetalhesClick,
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(">")
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    user: String,
    clubes: List<Clube>,
    onClubeClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var busca by remember { mutableStateOf("") }

    val filtrados = clubes.filter { it.nome.contains(busca, ignoreCase = true) }

    Surface (modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = t("home_espacos"),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "${t("home_ola")} $user",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text(t("home_buscar")) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = t("home_meus_clubes"),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = t("home_ativos").format(filtrados.size),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filtrados, key = { it.id }) { clube ->
                    ClubeCard(clube = clube, onDetalhesClick = { onClubeClick(clube.id) })
                }
            }
        }
    }
}