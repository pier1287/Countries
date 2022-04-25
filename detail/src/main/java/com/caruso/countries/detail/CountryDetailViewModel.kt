package com.caruso.countries.detail

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
    countryRepository: CountryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = CountryDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val countryDetailFlow =
        countryRepository.observeCountryDetail(args.id).map(::mapToState)
    private val _uiState = MutableSharedFlow<CountryDetailState>()
    val uiState: StateFlow<CountryDetailState> = merge(countryDetailFlow, _uiState)
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CountryDetailState(isLoading = true)
        )

    private fun mapToState(result: ResultOf<Country>): CountryDetailState =
        result.fold(
            error = { errorType ->
                val current = uiState.value
                current.copy(isLoading = false, errors = current.errors + errorType)
            },
            success = { country ->
                val current = uiState.value
                current.copy(isLoading = false, country = country)
            }
        )

    fun onErrorMessageShown(errorType: ErrorType) = viewModelScope.launch {
        val current = uiState.value
        val errors = current.errors.filter { it != errorType }
        _uiState.emit(current.copy(errors = errors))
    }
}

data class CountryDetailState(
    val country: Country? = null,
    val isLoading: Boolean = false,
    val errors: List<ErrorType> = emptyList()
)
