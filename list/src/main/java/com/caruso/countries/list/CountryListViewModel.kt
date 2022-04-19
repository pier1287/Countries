package com.caruso.countries.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caruso.countries.repository.CountryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    countryRepository: CountryRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            countryRepository.observeCountries().collect()
        }
    }
}