package com.dwan.colombia.ui.screen.attraction.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.PresidentModel
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
    val repository: AttractionRepository
) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _viewState = MutableStateFlow<BaseViewState<AttractionModel>>(BaseViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAttractionDetail(id).fold(
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

    private fun onSuccessResponse(attraction: AttractionModel) {
        _viewState.update {
            BaseViewState.Success(attraction)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _viewState.update {
                BaseViewState.Loading
            }
            repository.getAttractionDetail(id).fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }


}