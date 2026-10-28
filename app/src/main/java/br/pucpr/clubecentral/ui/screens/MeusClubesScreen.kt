package br.pucpr.clubecentral.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Clube(val nome: String, val membros: Int, val eventos: Int)

private val clubes = listOf(
    Clube("Clube de RPG", 24, 2),
    Clube("Clube de Xadrez", 15, 1),
    Clube("Clube de Fotografia", 31, 3),
    Clube("Clube de Video Games", 27, 2)
)

@Composable
fun MeusClubesScreen(
    modifier: Modifier = Modifier,
    nomeUsuario: String = "",
    onClubeClick: (Clube) -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(Modifier.height(16.dp))
            Text("MEUS CLUBES", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("${clubes.size} ativos", color = Color.Gray, fontSize = 13.sp)

            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = busca,
                onValueChange = { busca = it },
                placeholder = { Text("Buscar clube") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {}
        }

        items(clubes.filter { it.nome.contains(busca, ignoreCase = true) }) { clube ->
            CardClube(clube = clube, onClick = { onClubeClick(clube) })
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun CardClube(clube: Clube, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFDDDDDD))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFDDDDDD)),
            contentAlignment = Alignment.Center
        ) {
            Text("ÁREA DE IMAGEM", fontSize = 11.sp, color = Color.Gray)
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(clube.nome, fontWeight = FontWeight.Bold)
                Text(
                    text = "${clube.membros} membros - ${clube.eventos} eventos",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            OutlinedIconButton(
                onClick = onClick,
                shape = RoundedCornerShape(2.dp),
                border = BorderStroke(1.dp, Color(0xFFDDDDDD))
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Abrir")
            }
        }
    }
}

