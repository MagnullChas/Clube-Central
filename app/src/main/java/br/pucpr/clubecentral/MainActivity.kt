package br.pucpr.clubecentral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import br.pucpr.clubecentral.ui.theme.ClubeCentralTheme
import br.pucpr.clubecentral.ui.viewmodel.PerfilViewModel
import dagger.hilt.android.AndroidEntryPoint

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

            ClubeCentralTheme(darkTheme = escuro) {
                ClubeCentralApp(perfilViewModel = perfilViewModel)
            }
        }
    }
}