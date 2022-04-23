package com.caruso.countries.list

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
class CountryListViewModel @Inject constructor(
    countryRepository: CountryRepository
) : ViewModel() {

    val state: StateFlow<CountryListState> = countryRepository.observeCountries()
        .map(::mapToState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CountryListState.Loading
        )


    private fun mapToState(result: ResultOf<List<Country>>): CountryListState =
        result.fold(
            error = { CountryListState.Error(it) },
            success = { country -> CountryListState.Success(country) }
        )
}

sealed interface CountryListState {
    data class Success(val countries: List<Country> = emptyList()) : CountryListState
    data class Error(val errorType: ErrorType) : CountryListState
    object Loading : CountryListState
}