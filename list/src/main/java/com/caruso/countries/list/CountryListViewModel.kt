package com.caruso.countries.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caruso.countries.repository.Country
import com.caruso.countries.repository.CountryRepository
import com.caruso.countries.repository.getOrElse
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
        .map { CountryListState(it.getOrElse { emptyList() }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CountryListState(isLoading = true)
        )

}

data class CountryListState(
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false
)