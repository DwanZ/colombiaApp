package com.dwan.colombia.ui.screen.president

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.PresidentModel
import com.dwan.domain.repository.PresidentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresidentViewModel @Inject constructor(private val repository: PresidentRepository) : ViewModel() {

    private val _viewState = MutableStateFlow<BaseViewState<List<PresidentModel>>>(BaseViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _loadingState = MutableSharedFlow<Boolean>()
    val loadingState = _loadingState.asSharedFlow()

    private var search by mutableStateOf("")

    init {
        getPresidentList()
    }

    private fun getPresidentList() {
        viewModelScope.launch {
            repository.getPresidentList().fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }

    private fun onErrorResponse(e: Exception) {
        _viewState.update {
            BaseViewState.Failure("Error retrieving the list")
        }
    }

    private fun onSuccessResponse(list: List<PresidentModel>) {
        _viewState.update {
            BaseViewState.Success(list)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _viewState.update {
                BaseViewState.Loading
            }
            repository.getPresidentList().fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }

    fun search(word: String) {
        viewModelScope.launch {
            _viewState.update {
                BaseViewState.Loading
            }
            repository.getPresidentBySearch(word).fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }
}