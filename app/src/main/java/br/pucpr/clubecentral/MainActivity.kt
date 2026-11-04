package br.pucpr.clubecentral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import br.pucpr.clubecentral.ui.LocalIdioma
import br.pucpr.clubecentral.ui.theme.ClubeCentralTheme
import br.pucpr.clubecentral.ui.viewmodel.PerfilViewModel
import dagger.hilt.android.AndroidEntryPoint
import br.pucpr.clubecentral.ui.screens.HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val escuro = when (perfilViewModel.aparencia) {
                "Claro" -> false
                "Escuro" -> true
                else -> isSystemInDarkTheme() //"Tema do sistema do dispositivo"
            }

            CompositionLocalProvider(LocalIdioma provides perfilViewModel.idioma) {
                ClubeCentralTheme(darkTheme = escuro) {
                    ClubeCentralApp(perfilViewModel = perfilViewModel)
                }
            }

        }
    }
}