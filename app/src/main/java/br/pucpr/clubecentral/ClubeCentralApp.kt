
package br.pucpr.clubecentral

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.pucpr.clubecentral.ui.screens.CadastroScreen
import br.pucpr.clubecentral.ui.screens.EventosScreen
import br.pucpr.clubecentral.ui.screens.LoginScreen
import br.pucpr.clubecentral.ui.screens.PerfilScreen
import br.pucpr.clubecentral.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubeCentralApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    val usuario = authViewModel.usuarioAutenticado
    val carregando = authViewModel.carregando

    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    // Aguarda a verificação da sessão antes de navegar.
    LaunchedEffect(usuario, carregando) {
        if (!carregando) {
            if (usuario != null) {
                if (rotaAtual == "login" || rotaAtual == "cadastro") {
                    navController.navigate("eventos") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            } else if (
                rotaAtual != "cadastro" &&
                rotaAtual != "login"
            ) {
                navController.navigate("login") {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    // Mostra carregamento enquanto a sessão está sendo verificada.
    if (carregando && usuario == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val exigeAutenticacao =
        rotaAtual == "login" ||
                rotaAtual == "cadastro" ||
                usuario == null

    Scaffold(
        topBar = {
            if (!exigeAutenticacao && rotaAtual != "perfil") {
                TopAppBar(
                    title = {
                        Text("Clube Central")
                    }
                )
            }
        },
        bottomBar = {
            if (!exigeAutenticacao) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rotaAtual == "eventos",
                        onClick = {
                            navController.navigate("eventos") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("E")
                        },
                        label = {
                            Text("Eventos")
                        }
                    )

                    NavigationBarItem(
                        selected = rotaAtual == "tela 2",
                        onClick = {
                            navController.navigate("tela 2") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("2")
                        },
                        label = {
                            Text("Tela 02")
                        }
                    )

                    NavigationBarItem(
                        selected = rotaAtual == "tela 3",
                        onClick = {
                            navController.navigate("tela 3") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("3")
                        },
                        label = {
                            Text("Tela 03")
                        }
                    )

                    NavigationBarItem(
                        selected = rotaAtual == "perfil",
                        onClick = {
                            navController.navigate("perfil") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("P")
                        },
                        label = {
                            Text("Perfil")
                        }
                    )

                    NavigationBarItem(
                        selected = rotaAtual == "tela 5",
                        onClick = {
                            navController.navigate("tela 5") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Text("5")
                        },
                        label = {
                            Text("Tela 05")
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onCriarConta = {
                        authViewModel.limparMensagem()
                        navController.navigate("cadastro")
                    }
                )
            }

            composable("cadastro") {
                CadastroScreen(
                    viewModel = authViewModel,
                    onVoltar = {
                        authViewModel.limparMensagem()
                        navController.popBackStack()
                    }
                )
            }

            composable("eventos") {
                EventosScreen()
            }

            composable("tela 2") {
                Box(
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable("tela 3") {
                Box(
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable("perfil") {
                PerfilScreen(
                    authViewModel = authViewModel,
                    onSair = {
                        authViewModel.sair()
                    }
                )
            }

            composable("tela 5") {
                Box(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
