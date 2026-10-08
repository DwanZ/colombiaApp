package com.dwan.feature.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.CountryModel
import com.dwan.domain.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BaseViewState<CountryModel>>(BaseViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCountry()
    }

    fun refresh() = loadCountry(showLoading = true)

    private fun loadCountry(showLoading: Boolean = false) {
        viewModelScope.launch {
            if (showLoading || _uiState.value !is BaseViewState.Success) {
                _uiState.update { BaseViewState.Loading }
            }
            countryRepository.getCountry()
                .onSuccess { country -> _uiState.update { BaseViewState.Success(country) } }
                .onFailure { error ->
                    _uiState.update {
                        BaseViewState.Failure(error.message ?: "Unable to load country data")
                    }
                }
        }
    }
}
