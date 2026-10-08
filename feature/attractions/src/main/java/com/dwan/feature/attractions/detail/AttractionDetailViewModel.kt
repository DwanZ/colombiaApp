package com.dwan.feature.attractions.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttractionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AttractionRepository
) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<BaseViewState<AttractionModel>>(BaseViewState.Loading)
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
            repository.getAttractionDetail(id)
                .onSuccess { attraction -> _uiState.update { BaseViewState.Success(attraction) } }
                .onFailure {
                    _uiState.update { BaseViewState.Failure("Error retrieving the data") }
                }
        }
    }
}
