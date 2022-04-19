package com.caruso.countries.repository

import com.caruso.countries.repository.remote.CountryDto

internal fun List<CountryDto>.toEntities(): List<Country> = map {
    Country(id = it.cca3, name = it.name.commonName)
}