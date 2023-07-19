package com.dwan.colombia.ui.screen.attraction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.AttractionPageModel
import com.dwan.domain.repository.AttractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttractionViewModel @Inject constructor(
    private val repository: AttractionRepository
) : ViewModel() {

    private val _viewState =
        MutableStateFlow<BaseViewState<List<AttractionModel>>>(BaseViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _loadingState = MutableSharedFlow<Boolean>()
    val loadingState = _loadingState.asSharedFlow()

    var page by mutableStateOf("1")
    var limit by mutableStateOf("10")
    var pageCount by mutableIntStateOf(1)

    init {
        getAttractionList()
    }

    private fun getAttractionList() {
        viewModelScope.launch {
            repository.getAttractionByPage(page, limit).fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }

    fun refresh() {
        getAttractionList()
    }

    fun search(word: String) {
        viewModelScope.launch {
            _viewState.update {
                BaseViewState.Loading
            }
            repository.getAttractionBySearch(word).fold(
                ::onErrorResponse,
                ::onSuccessSearchResponse
            )
        }
    }

    private fun onErrorResponse(e: Exception) {
        _viewState.update {
            BaseViewState.Failure("Error retrieving the list")
        }
    }

    private fun onSuccessResponse(list: AttractionPageModel) {
        _viewState.update {
            BaseViewState.Success(list.data)
        }
        pageCount = list.pageCount
    }

    private fun onSuccessSearchResponse(list: List<AttractionModel>) {
        _viewState.update {
            BaseViewState.Success(list)
        }
    }

}