package com.dwan.feature.presidents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.PresidentModel
import com.dwan.domain.repository.PresidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresidentViewModel @Inject constructor(
    private val repository: PresidentRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<BaseViewState<List<PresidentModel>>>(BaseViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load(showLoading = true)

    fun search(word: String) {
        viewModelScope.launch {
            _uiState.update { BaseViewState.Loading }
            repository.getPresidentBySearch(word)
                .onSuccess { list -> _uiState.update { BaseViewState.Success(list) } }
                .onFailure {
                    _uiState.update { BaseViewState.Failure("Error retrieving the list") }
                }
        }
    }

    private fun load(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading || _uiState.value !is BaseViewState.Success) {
                _uiState.update { BaseViewState.Loading }
            }
            repository.getPresidentList()
                .onSuccess { list -> _uiState.update { BaseViewState.Success(list) } }
                .onFailure {
                    _uiState.update { BaseViewState.Failure("Error retrieving the list") }
                }
        }
    }
}
