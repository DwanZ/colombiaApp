package com.dwan.feature.presidents.detail

import androidx.lifecycle.SavedStateHandle
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
class PresidentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PresidentRepository
) : ViewModel() {

    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<BaseViewState<PresidentModel>>(BaseViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun refresh() = load(showLoading = true)

    private fun load(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading || _uiState.value !is BaseViewState.Success) {
                _uiState.update { BaseViewState.Loading }
            }
            repository.getPresidentDetail(id)
                .onSuccess { president -> _uiState.update { BaseViewState.Success(president) } }
                .onFailure {
                    _uiState.update { BaseViewState.Failure("Error retrieving the data") }
                }
        }
    }
}
