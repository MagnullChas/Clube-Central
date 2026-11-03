package br.pucpr.clubecentral.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import br.pucpr.clubecentral.model.Clube
import br.pucpr.clubecentral.data.local.ClubeDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
    class HomeViewModel @Inject constructor(
        private val clubeDao: ClubeDao
    ) : ViewModel() {

        private val _clubes = MutableStateFlow<List<Clube>>(emptyList())
        val clubes: StateFlow<List<Clube>> = _clubes.asStateFlow()

        private val _clube = MutableStateFlow<Clube?>(null)
        val clube = _clube.asStateFlow()

        init {
            carregar()
        }

        fun carregar() {
            viewModelScope.launch {
                _clubes.value = clubeDao.listar()
            }
        }

        fun carregarID(id: Int) {
            viewModelScope.launch { _clube.value = clubeDao.buscarPorId(id) }
        }

        fun excluir(clube: Clube) {
            viewModelScope.launch {
                clubeDao.excluir(clube)
                carregar()
            }
        }
    }
