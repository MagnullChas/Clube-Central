
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.pucpr.clubecentral.ui.screens.CadastroScreen
import br.pucpr.clubecentral.ui.screens.ClubeScreen
import br.pucpr.clubecentral.ui.screens.EventosScreen
import br.pucpr.clubecentral.ui.screens.LoginScreen
import br.pucpr.clubecentral.ui.screens.PerfilScreen
import br.pucpr.clubecentral.ui.screens.ConfiguracoesScreen
import br.pucpr.clubecentral.ui.viewmodel.AuthViewModel
import br.pucpr.clubecentral.ui.viewmodel.PerfilViewModel
import br.pucpr.clubecentral.ui.screens.HomeScreen
import br.pucpr.clubecentral.ui.viewmodel.ClubeViewModel
import br.pucpr.clubecentral.ui.viewmodel.HomeViewModel
import androidx.compose.runtime.collectAsState

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubeCentralApp(perfilViewModel: PerfilViewModel) {
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
                    navController.navigate("Home") {
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
                        selected = rotaAtual == "Home" ||
                                rotaAtual?.startsWith("clube") == true ||
                                rotaAtual?.startsWith("evento") == true,
                        onClick = {
                            navController.navigate("Home") {
                                popUpTo("Home")
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home"
                            )
                        },
                        label = {
                            Text("Home")
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
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Perfil"
                            )
                        },
                        label = {
                            Text("Perfil")
                        }
                    )

                    NavigationBarItem(
                        selected = rotaAtual == "configuracao",
                        onClick = {
                            navController.navigate("configuracao") {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Configurações"
                            )
                        },
                        label = {
                            Text("CONFIG")
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

            composable(
                route = "clube/{clubeId}",
                arguments = listOf(navArgument("clubeId") { type = NavType.IntType })
            ) { entrada ->
                val clubeId = entrada.arguments?.getInt("clubeId") ?: 0
                val clubeViewModel: ClubeViewModel = hiltViewModel()

                // Recarrega ao entrar e ao voltar da tela de evento.
                DisposableEffect(entrada) {
                    val observador = LifecycleEventObserver { _, evento ->
                        if (evento == Lifecycle.Event.ON_RESUME) {
                            clubeViewModel.carregar(clubeId)
                        }
                    }
                    entrada.lifecycle.addObserver(observador)
                    onDispose { entrada.lifecycle.removeObserver(observador) }
                }

                ClubeScreen(
                    viewModel = clubeViewModel,
                    ehAdmin = usuario?.tipo == "ADMIN",
                    onDiaClick = { data ->
                        navController.navigate("evento/$clubeId/${data.replace("/", "-")}")
                    }
                )
            }

            composable(
                route = "evento/{clubeId}/{data}",
                arguments = listOf(
                    navArgument("clubeId") { type = NavType.IntType },
                    navArgument("data") { type = NavType.StringType }
                )
            ) { entrada ->
                EventosScreen(
                    clubeId = entrada.arguments?.getInt("clubeId") ?: 1,
                    dataInicial = entrada.arguments?.getString("data")?.replace("-", "/"),
                    onConcluido = { navController.popBackStack() }
                )
            }

            composable("Home") {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val clubes by homeViewModel.clubes.collectAsState()

                HomeScreen(
                    user = usuario?.nome ?: "",
                    clubes = clubes,
                    onClubeClick = { id -> navController.navigate("clube/$id") }
                )
            }

            composable("perfil") {
                PerfilScreen(
                    authViewModel = authViewModel,
                    viewModel = perfilViewModel,
                    onSair = {
                        authViewModel.sair()
                    }
                )
            }

            composable("configuracao") {
                ConfiguracoesScreen(
                    authViewModel = authViewModel,
                    viewModel = perfilViewModel,
                    onSair = {
                        authViewModel.sair()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
