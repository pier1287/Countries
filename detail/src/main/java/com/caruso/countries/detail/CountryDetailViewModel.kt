package com.caruso.countries.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caruso.countries.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
    countryRepository: CountryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = CountryDetailFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val state: StateFlow<CountryDetailState> = countryRepository.observeCountryDetail(args.id)
        .map(::mapToState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CountryDetailState.Loading
        )

    private fun mapToState(result: ResultOf<Country>): CountryDetailState =
        result.fold(
            error = { CountryDetailState.Error(it) },
            success = { country -> CountryDetailState.Success(country) }
        )
}

sealed interface CountryDetailState {
    data class Success(val country: Country) : CountryDetailState
    data class Error(val type: ErrorType) : CountryDetailState
    object Loading : CountryDetailState
}
