package com.dwan.feature.attractions

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

data class AttractionListUiState(
    val page: Int = 1,
    val pageSize: Int = 10,
    val pageCount: Int = 1,
    val content: BaseViewState<List<AttractionModel>> = BaseViewState.Loading
)

@HiltViewModel
class AttractionViewModel @Inject constructor(
    private val repository: AttractionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttractionListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPage()
    }

    fun refresh() = loadPage()

    fun setPage(page: Int) {
        _uiState.update { it.copy(page = page) }
        loadPage()
    }

    fun setPageSize(size: Int) {
        _uiState.update { it.copy(pageSize = size, page = 1) }
        loadPage()
    }

    fun search(word: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(content = BaseViewState.Loading) }
            repository.getAttractionBySearch(word)
                .onSuccess { list ->
                    _uiState.update { it.copy(content = BaseViewState.Success(list)) }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(content = BaseViewState.Failure("Error retrieving the list"))
                    }
                }
        }
    }

    private fun loadPage() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(content = BaseViewState.Loading) }
            repository.getAttractionByPage(state.page.toString(), state.pageSize.toString())
                .onSuccess { page ->
                    _uiState.update {
                        it.copy(
                            pageCount = page.pageCount,
                            content = BaseViewState.Success(page.data)
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(content = BaseViewState.Failure("Error retrieving the list"))
                    }
                }
        }
    }
}
