package com.dwan.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwan.common.BaseViewState
import com.dwan.domain.model.AttractionModel
import com.dwan.domain.model.DepartmentModel
import com.dwan.domain.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

data class MapUiState(
    val departments: BaseViewState<List<DepartmentModel>> = BaseViewState.Loading,
    val selectedDepartment: DepartmentModel? = null,
    val attractions: BaseViewState<List<AttractionModel>>? = null,
    val sheetVisible: Boolean = false
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private var departmentCache: List<DepartmentModel> = emptyList()

    init {
        loadDepartments()
    }

    fun refresh() = loadDepartments()

    fun onDepartmentTapped(geoName: String) {
        val match = findDepartment(geoName)

        if (match == null) {
            _uiState.update {
                it.copy(
                    selectedDepartment = null,
                    sheetVisible = true,
                    attractions = BaseViewState.Failure("Department \"$geoName\" not found in API")
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                selectedDepartment = match,
                sheetVisible = true,
                attractions = BaseViewState.Loading
            )
        }
        loadAttractions(match.id)
    }

    fun dismissSheet() {
        _uiState.update {
            it.copy(sheetVisible = false, selectedDepartment = null, attractions = null)
        }
    }

    private fun findDepartment(geoName: String): DepartmentModel? {
        val normalizedGeo = normalize(alias(geoName))
        return departmentCache.firstOrNull {
            normalize(it.name) == normalizedGeo
        } ?: departmentCache.firstOrNull {
            val apiName = normalize(it.name)
            apiName.contains(normalizedGeo) || normalizedGeo.contains(apiName)
        }
    }

    private fun loadDepartments() {
        viewModelScope.launch {
            _uiState.update { it.copy(departments = BaseViewState.Loading) }
            departmentRepository.getDepartments()
                .onSuccess { list ->
                    departmentCache = list
                    _uiState.update { it.copy(departments = BaseViewState.Success(list)) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            departments = BaseViewState.Failure(
                                error.message ?: "Unable to load departments"
                            )
                        )
                    }
                }
        }
    }

    private fun loadAttractions(departmentId: Int) {
        viewModelScope.launch {
            departmentRepository.getTouristicAttractions(departmentId)
                .onSuccess { list ->
                    _uiState.update { it.copy(attractions = BaseViewState.Success(list)) }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(attractions = BaseViewState.Failure("Unable to load attractions"))
                    }
                }
        }
    }

    private fun alias(value: String): String {
        val key = normalize(value)
        return GEO_ALIASES.entries.firstOrNull { (aliasKey, _) ->
            key == aliasKey || key.contains(aliasKey) || aliasKey.contains(key)
        }?.value ?: value
    }

    private fun normalize(value: String): String {
        val decomposed = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
        return decomposed
            .replace("\\p{Mn}+".toRegex(), "")
            .lowercase()
            .replace("departamento del ", "")
            .replace("departamento de ", "")
            .replace("departamento ", "")
            .replace("archipielago de ", "")
            .replace("d.c.", "")
            .replace(",", " ")
            .replace(".", " ")
            .replace("\\s+".toRegex(), " ")
            .trim()
    }

    companion object {
        private val GEO_ALIASES = mapOf(
            "san andres providencia y santa catalina" to "San Andrés y Providencia",
            "san andres y providencia" to "San Andrés y Providencia",
            "bogota" to "Bogotá",
            "quindio" to "Quindío",
            "valle del cauca" to "Valle del Cauca",
            "norte de santander" to "Norte de Santander"
        )
    }
}
