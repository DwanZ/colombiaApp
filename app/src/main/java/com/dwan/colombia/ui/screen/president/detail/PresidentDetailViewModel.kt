package com.dwan.colombia.ui.screen.president.detail

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

    private val _viewState = MutableStateFlow<BaseViewState<PresidentModel>>(BaseViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getPresidentDetail(id).fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }

    private fun onErrorResponse(e: Exception) {
        _viewState.update {
            BaseViewState.Failure("Error retrieving the data")
        }
    }

    private fun onSuccessResponse(president: PresidentModel) {
        _viewState.update {
            BaseViewState.Success(president)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _viewState.update {
                BaseViewState.Loading
            }
            repository.getPresidentDetail(id).fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }
}