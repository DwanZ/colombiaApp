package com.dwan.colombia.ui.screen.country

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

    private val _countryState = MutableStateFlow<BaseViewState<CountryModel>>(BaseViewState.Loading)
    val countryState = _countryState.asStateFlow()

    init {
        getCountry()
    }

    private fun getCountry() {
        viewModelScope.launch {
            countryRepository.getCountry().fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }

    private fun onErrorResponse(e: Exception) {
        _countryState.update {
            BaseViewState.Failure(e.message ?: "")
        }
    }

    private fun onSuccessResponse(country: CountryModel) {
        _countryState.update {
            BaseViewState.Success(country)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _countryState.update {
                BaseViewState.Loading
            }
            countryRepository.getCountry().fold(
                ::onErrorResponse,
                ::onSuccessResponse
            )
        }
    }
}

