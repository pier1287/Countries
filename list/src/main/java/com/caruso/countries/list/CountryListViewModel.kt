package com.caruso.countries.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caruso.countries.domain.Country
import com.caruso.countries.domain.ErrorType
import com.caruso.countries.domain.ResultOf
import com.caruso.countries.domain.fold
import com.caruso.countries.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    countryRepository: CountryRepository
) : ViewModel() {

    private val countriesFlow = countryRepository.observeCountries().map(::mapToState)
    private val _uiState = MutableSharedFlow<CountryListState>()
    val uiState: StateFlow<CountryListState> = merge(countriesFlow, _uiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CountryListState(isLoading = true)
        )

    private fun mapToState(result: ResultOf<List<Country>>): CountryListState =
        result.fold(
            error = { errorType ->
                val current = uiState.value
                current.copy(isLoading = false, errors = current.errors + errorType)
            },
            success = {
                val current = uiState.value
                current.copy(isLoading = false, countries = it)
            }
        )

    fun onErrorMessageShown(errorType: ErrorType) = viewModelScope.launch {
        val current = uiState.value
        val errors = current.errors.filter { it != errorType }
        _uiState.emit(current.copy(errors = errors))
    }
}

data class CountryListState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val errors: List<ErrorType> = emptyList()
)
