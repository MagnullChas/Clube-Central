package br.pucpr.clubecentral

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.pucpr.clubecentral.ui.screens.EventosScreen
import br.pucpr.clubecentral.ui.screens.PerfilScreen

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ClubeCentralApp() {

    val navController = rememberNavController()

    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    Scaffold(
        topBar = {
            if (rotaAtual != "perfil") {
                TopAppBar(
                    title = { Text("Clube Central") }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rotaAtual == "eventos",
                    onClick = { navController.navigate("eventos") { launchSingleTop = true } },
                    icon = { Text("E") }, label = { Text("Eventos") }
                )

                NavigationBarItem(
                    selected = rotaAtual == "tela 2",
                    onClick = { navController.navigate("tela 2") { launchSingleTop = true } },
                    icon = { Text("2") }, label = { Text("Tela 02") }
                )

                NavigationBarItem(
                    selected = rotaAtual == "tela 3",
                    onClick = { navController.navigate("tela 3") { launchSingleTop = true } },
                    icon = { Text("3") }, label = { Text("Tela 03") }
                )

                NavigationBarItem(
                    selected = rotaAtual == "perfil",
                    onClick = { navController.navigate("perfil") { launchSingleTop = true } },
                    icon = { Text("P") }, label = { Text("Perfil") }
                )

                NavigationBarItem(
                    selected = rotaAtual == "tela 5",
                    onClick = { navController.navigate("tela 5") { launchSingleTop = true } },
                    icon = { Text("5") }, label = { Text("Tela 05") }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "eventos",
            modifier = Modifier.padding(innerPadding)
        ) {

            // eventos
            composable("eventos") {
                EventosScreen()
            }

            // tela 2
            composable("tela 2") {
                Box {}
            }

            // tela 3
            composable("tela 3") {
                Box {}
            }

            // tela 4
            composable("perfil") {
                PerfilScreen{}
            }

            // tela 5
            composable("tela 5") {
                Box {}
            }
        }
    }
}